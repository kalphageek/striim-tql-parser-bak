package me.kalpha.catalog.striim.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
//@Data
public class CatJobsrctagInfKey implements Serializable {
    private String jobSysIpAddr;
    private String jobNm;
    private String seqno;

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
}