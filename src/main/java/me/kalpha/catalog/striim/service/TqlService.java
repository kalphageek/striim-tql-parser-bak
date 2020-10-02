package me.kalpha.catalog.striim.service;

import me.kalpha.catalog.striim.parser.ToKfTql;
import me.kalpha.catalog.striim.parser.TqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TqlService {
    @Autowired
    TqlParser tqlParser;

    /*
     * downstream -> kafka 처리하는 .tql 파일 처리
     */
    public List<ToKfTql> serialize(File[] files) {
        Stream<File> tqlFiles = Arrays.stream(files)
                .filter(f -> f.getName().toLowerCase().contains("_to_kf_"));
        List<ToKfTql> list = tqlFiles
                .map(f -> tqlParser.serializeToKfTql(f.getAbsolutePath()))
                .collect(Collectors.toList());

        return list;
    }
}