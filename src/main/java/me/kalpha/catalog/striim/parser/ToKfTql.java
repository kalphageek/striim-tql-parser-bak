package me.kalpha.catalog.striim.parser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class ToKfTql {
    //    CatJobsrctagInfKey
    private String jobSysIpAddr;//[striim cluster ip]--parameter로 전달
    private String jobNm;//appName
    private String seqno = "1";

    private String crtUserId = "striim-tql-parser";
    private LocalDateTime crtTm = LocalDateTime.now();
    private String srcObjGbnCd;
    private String srcObjNm;//srcName
    private String targetObjIpAddr;//kafkaCluster
    private String targetObjGbnCd;
    private String targetObjNm;//topicName
}