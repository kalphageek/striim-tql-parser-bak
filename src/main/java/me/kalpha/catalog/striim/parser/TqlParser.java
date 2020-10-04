package me.kalpha.catalog.striim.parser;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class TqlParser {
    Logger logger  = LoggerFactory.getLogger(this.getClass());

    /*
     * downstream -> kafka 처리하는 .tql 파일 처리
     */
    public List<ToPsKfTql> parseToPsKfTqls(File[] files) {
        Stream<File> tqlFiles = Arrays.stream(files)
                .filter(f -> f.getName().toLowerCase().contains("_to_kf_"));
        List<ToPsKfTql> list = tqlFiles
                .map(f -> parseToPsKfTql(f.getAbsolutePath()))
                .collect(Collectors.toList());

        return list;
    }

    public List<ToDbTql> parseToDbTqls(File[] files) {
        Stream<File> tqlFiles = Arrays.stream(files)
                .filter(f -> f.getName().toLowerCase().contains("_to_HUB_"));
        List<ToDbTql> list = tqlFiles
                .map(f -> parseToDbTql(f.getAbsolutePath()))
                .collect(Collectors.toList());

        return list;
    }

    private ToPsKfTql parseToPsKfTql(String filePath) {
        ToPsKfTql tqlApp = new ToPsKfTql();
        String line = null;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            Optional<String> optional = allLines.stream()
                    .reduce((a, b) -> (a.replace("\n", " ")+" "+b.replace("\n", " ")));
            line = optional.get();
            logger.debug(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String regex = "CREATE +APPLICATION +(?<application>\\w+).*directory *: *'(?<directory>[\\w\\/]+)?'.*wildcard *: *'(?<wildcard>[\\w*]+)?'.*.*tables: +'(?<owner>\\w+)?.*CREATE[\\w ]+STREAM (?<topic>\\w+).*USING (?<kafkaCluster>[\\w.]+);";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            tqlApp.setJobNm(matcher.group("application"));
            String sourceFile = matcher.group("directory")+"/";
            sourceFile += matcher.group("wildcard");
            tqlApp.setSrcObjNm(sourceFile);
            tqlApp.setTargetObjNm(matcher.group("topic"));
            tqlApp.setTargetObjIpAddr(matcher.group("kafkaCluster"));
        }

        logger.debug(tqlApp.toString());
        return tqlApp;
    }

    private ToDbTql parseToDbTql(String filePath) {
        ToDbTql tqlApp = new ToDbTql();
        String line = null;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            Optional<String> optional = allLines.stream()
                    .reduce((a, b) -> (a.replace("\n", " ")+" "+b.replace("\n", " ")));
            line = optional.get();
            logger.debug(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String regex = "CREATE +APPLICATION +(?<application>\\w+).*username *: *'(?<username>[\\w\\/]+)?'.*\\(\\s*HOST=(?<hostname>\\w+)\\s*\\).*tables\\s*:\\s*'(?<tables>.+)'\\s*.*input\\s+from\\s+(?<topicName>\\w+)\\s*;?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            tqlApp.setJobNm(matcher.group("application"));
            tqlApp.setTargetObjSchemaNm(matcher.group("username"));
            tqlApp.setSrcObjNm(matcher.group("topicName"));
            tqlApp.setTargetObjIpAddr(matcher.group("hostname"));
            Stream<String> stream = Arrays.stream(matcher.group("tables").split(";"))
                    .map(s -> Arrays.asList(s.split(",")).get(1))
                    .map(s -> Arrays.asList(s.toLowerCase().split(" columnmap")).get(0))
                    .map(s -> Arrays.asList(s.split(".")).get(1));
            tqlApp.setTargetObjNm(stream.collect(Collectors.toList()));
        }

        logger.debug(tqlApp.toString());
        return tqlApp;
    }


}