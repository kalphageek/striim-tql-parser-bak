package me.kalpha.catalog.striim.parser;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@NoArgsConstructor
//@Getter @Setter
@ToString
@EqualsAndHashCode
@Component
public class ToPsKfTql {
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

    @Override
    public String toString() {
        return "ToPsKfTql{" +
                "jobSysIpAddr='" + jobSysIpAddr + '\'' +
                ", jobNm='" + jobNm + '\'' +
                ", seqno='" + seqno + '\'' +
                ", crtUserId='" + crtUserId + '\'' +
                ", crtTm=" + crtTm +
                ", srcObjGbnCd='" + srcObjGbnCd + '\'' +
                ", srcObjNm='" + srcObjNm + '\'' +
                ", targetObjIpAddr='" + targetObjIpAddr + '\'' +
                ", targetObjGbnCd='" + targetObjGbnCd + '\'' +
                ", targetObjNm='" + targetObjNm + '\'' +
                '}';
    }

    public String getJobSysIpAddr() {
        return jobSysIpAddr;
    }

    public void setJobSysIpAddr(String jobSysIpAddr) {
        this.jobSysIpAddr = jobSysIpAddr;
    }

    public String getJobNm() {
        return jobNm;
    }

    public void setJobNm(String jobNm) {
        this.jobNm = jobNm;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getCrtUserId() {
        return crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    public LocalDateTime getCrtTm() {
        return crtTm;
    }

    public void setCrtTm(LocalDateTime crtTm) {
        this.crtTm = crtTm;
    }

    public String getSrcObjGbnCd() {
        return srcObjGbnCd;
    }

    public void setSrcObjGbnCd(String srcObjGbnCd) {
        this.srcObjGbnCd = srcObjGbnCd;
    }

    public String getSrcObjNm() {
        return srcObjNm;
    }

    public void setSrcObjNm(String srcObjNm) {
        this.srcObjNm = srcObjNm;
    }

    public String getTargetObjIpAddr() {
        return targetObjIpAddr;
    }

    public void setTargetObjIpAddr(String targetObjIpAddr) {
        this.targetObjIpAddr = targetObjIpAddr;
    }

    public String getTargetObjGbnCd() {
        return targetObjGbnCd;
    }

    public void setTargetObjGbnCd(String targetObjGbnCd) {
        this.targetObjGbnCd = targetObjGbnCd;
    }

    public String getTargetObjNm() {
        return targetObjNm;
    }

    public void setTargetObjNm(String targetObjNm) {
        this.targetObjNm = targetObjNm;
    }
}