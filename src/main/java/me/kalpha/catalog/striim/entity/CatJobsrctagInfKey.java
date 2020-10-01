package me.kalpha.catalog.striim.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class CatJobsrctagInfKey implements Serializable {
    private String jobSysIpAddr;
    private String jobNm;
    private String seqno;
}