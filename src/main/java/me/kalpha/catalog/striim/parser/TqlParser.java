package me.kalpha.catalog.striim.parser;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
public class TqlParser {
    Logger logger  = LoggerFactory.getLogger(this.getClass());

    public ToKfTql parseToKfTql(String filePath) {
        ToKfTql tqlApp = new ToKfTql();
        String tql = null;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            Optional<String> line = allLines.stream()
                    .reduce((a, b) -> (a.replace("\n", " ")+" "+b.replace("\n", " ")));
            tql = line.get();
            logger.debug(tql);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String regex = "CREATE +APPLICATION +(?<application>\\w+).*directory *: *'(?<directory>[\\w\\/]+)?'.*wildcard *: *'(?<wildcard>[\\w*]+)?'.*.*tables: +'(?<owner>\\w+)?.*CREATE[\\w ]+STREAM (?<topic>\\w+).*USING (?<kafkaCluster>[\\w.]+);";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setJobNm(matcher.group("application"));
            String sourceFile = matcher.group("directory")+"/";
            sourceFile += matcher.group("wildcard");
            tqlApp.setSrcObjNm(sourceFile);
            tqlApp.setTargetObjNm(matcher.group("topic"));
            tqlApp.setTargetObjIpAddr(matcher.group("kafkaCluster"));
        }

        logger.info(tqlApp.toString());
        return tqlApp;
    }

    public ToKfTql parseToKfFile2(String downstreamHostname, String filePath) {
        ToKfTql tqlApp = new ToKfTql();
        String tql = null;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            Optional<String> line = allLines.stream()
                    .reduce((a, b) -> (a.replace("\n", " ")+" "+b.replace("\n", " ")));
            tql = line.get();
            logger.debug(tql);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String regex = "CREATE +APPLICATION +(?<appName>\\w+).*directory *: *'(?<srcDirName>[\\w\\/]+)?'.*wildcard *: *'(?<srcFileName>[\\w*]+)?'.*.*tables: +'(?<ownerName>\\w+)?.*CREATE[\\w ]+STREAM (?<topicName>\\w+).*USING (?<kafkaName>[\\w.]+);";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setJobNm(matcher.group("appName"));
            String srcName = matcher.group("srcDirName")+"/";
            srcName += matcher.group("srcFileName");
            tqlApp.setSrcObjNm(srcName);
            tqlApp.setTargetObjNm(matcher.group("topicName"));
            tqlApp.setTargetObjIpAddr(matcher.group("kafkaName"));
//            tqlApp.setOwnerName(matcher.group("ownerName"));
        }
/*
//appName
        final String appNameRegex = "CREATE +APPLICATION +(?<appName>\\w+)";
        Pattern pattern = Pattern.compile(appNameRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setAppName(matcher.group("appName"));
        }
//srcName
        final String srcDirNameRegex = "directory *: *'(?<srcDirName>[\\w\\/]+)?'";
        pattern = Pattern.compile(srcDirNameRegex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setSrcName(matcher.group("srcDirName")+"/");
        }
        final String srcFileNameRegex = "wildcard *: *'(?<srcFileName>[\\w*]+)?'";
        pattern = Pattern.compile(srcFileNameRegex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setSrcName(tqlApp.getSrcName()+matcher.group("srcFileName"));
        }
//ownerName
        final String ownerNameRegex = "tables: +'(?<ownerName>\\w+)?\\.*";
        pattern = Pattern.compile(ownerNameRegex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setOwnerName(matcher.group("ownerName"));
        }
//topicName, kafkaName
        final String topicKafkaNameRegex = "CREATE[\\w ]+STREAM (?<topicName>\\w+).*USING (?<kafkaName>[\\w.]+);";
        pattern = Pattern.compile(topicKafkaNameRegex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(tql);
        while (matcher.find()) {
            tqlApp.setTopicName(matcher.group("topicName"));
            tqlApp.setKafkaName(matcher.group("kafkaName"));
        }
//tableName
        final String tableNameRegex = "tables: +'(?<tableName>.*?)', .*";
        pattern = Pattern.compile(tableNameRegex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(tql);
        while (matcher.find()) {
            Stream<String> stream = Arrays.stream(matcher.group("tableName").split(";"))
                    .map(s -> Arrays.asList(s.split("\\.")).get(1));

            List<String> tableNames = new ArrayList<String>();
            stream.forEach(tableNames::add);
            tqlApp.setTableNames(tableNames);
        }
*/

        logger.info(tqlApp.toString());
        return tqlApp;
    }
}