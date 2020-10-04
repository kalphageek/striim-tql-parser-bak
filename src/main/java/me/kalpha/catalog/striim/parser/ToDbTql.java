package me.kalpha.catalog.striim.parser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter @Setter @ToString
@NoArgsConstructor
public class ToDbTql {
    private String jobSysIpAddr;//[striim cluster ip]--parameter로 전달
    private String jobNm;//appName
    private String seqno = "1";

    private String crtUserId = "striim-tql-parser";
    private LocalDateTime crtTm = LocalDateTime.now();
    private String srcObjGbnCd = "topic";
    private String srcObjNm;//topicName
    private String targetObjIpAddr;//hubdb
    private String targetObjSchemaNm;//username
    private String targetObjGbnCd = "table";
    private List<String> targetObjNm;//tableNames

    @Override
    public String toString() {
        return "ToDbTql{" +
                "jobSysIpAddr='" + jobSysIpAddr + '\'' +
                ", jobNm='" + jobNm + '\'' +
                ", seqno='" + seqno + '\'' +
                ", crtUserId='" + crtUserId + '\'' +
                ", crtTm=" + crtTm +
                ", srcObjGbnCd='" + srcObjGbnCd + '\'' +
                ", srcObjNm='" + srcObjNm + '\'' +
                ", targetObjIpAddr='" + targetObjIpAddr + '\'' +
                ", targetObjSchemaNm='" + targetObjSchemaNm + '\'' +
                ", targetObjGbnCd='" + targetObjGbnCd + '\'' +
                ", targetObjNm=" + targetObjNm +
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

    public String getTargetObjSchemaNm() {
        return targetObjSchemaNm;
    }

    public void setTargetObjSchemaNm(String targetObjSchemaNm) {
        this.targetObjSchemaNm = targetObjSchemaNm;
    }

    public String getTargetObjGbnCd() {
        return targetObjGbnCd;
    }

    public void setTargetObjGbnCd(String targetObjGbnCd) {
        this.targetObjGbnCd = targetObjGbnCd;
    }

    public List<String> getTargetObjNm() {
        return targetObjNm;
    }

    public void setTargetObjNm(List<String> targetObjNm) {
        this.targetObjNm = targetObjNm;
    }
}
