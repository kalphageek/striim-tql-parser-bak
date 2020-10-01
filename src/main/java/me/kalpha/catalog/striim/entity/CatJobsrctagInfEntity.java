package me.kalpha.catalog.striim.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cat_Jobsrctag_Inf")
@NoArgsConstructor
@Data
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
}