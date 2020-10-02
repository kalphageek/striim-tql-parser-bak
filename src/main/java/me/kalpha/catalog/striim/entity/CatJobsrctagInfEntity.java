package me.kalpha.catalog.striim.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cat_Jobsrctag_Inf")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@Data
public class CatJobsrctagInfEntity implements Serializable {
    @EmbeddedId
    private CatJobsrctagInfKey key;

    private String crtUserId;
    private LocalDateTime crtTm;
    private String srcObjIpAddr;
    private String srcObjGbnCd;
    private String srcObjNm;
    private String srcObjSchemaNm;
    private String targetObjIpAddr;
    private String targetObjGbnCd;
    private String targetObjNm;
    private String targetObjSchemaNm;
    private String linPrivateYn;
    private String jobSysId;

    public CatJobsrctagInfKey getKey() {
        return key;
    }

    public void setKey(CatJobsrctagInfKey key) {
        this.key = key;
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

    public String getSrcObjIpAddr() {
        return srcObjIpAddr;
    }

    public void setSrcObjIpAddr(String srcObjIpAddr) {
        this.srcObjIpAddr = srcObjIpAddr;
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

    public String getSrcObjSchemaNm() {
        return srcObjSchemaNm;
    }

    public void setSrcObjSchemaNm(String srcObjSchemaNm) {
        this.srcObjSchemaNm = srcObjSchemaNm;
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

    public String getTargetObjSchemaNm() {
        return targetObjSchemaNm;
    }

    public void setTargetObjSchemaNm(String targetObjSchemaNm) {
        this.targetObjSchemaNm = targetObjSchemaNm;
    }

    public String getLinPrivateYn() {
        return linPrivateYn;
    }

    public void setLinPrivateYn(String linPrivateYn) {
        this.linPrivateYn = linPrivateYn;
    }

    public String getJobSysId() {
        return jobSysId;
    }

    public void setJobSysId(String jobSysId) {
        this.jobSysId = jobSysId;
    }
}