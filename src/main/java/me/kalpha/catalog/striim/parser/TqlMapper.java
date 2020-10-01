package me.kalpha.catalog.striim.parser;

import me.kalpha.catalog.striim.entity.CatJobsrctagInfEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TqlMapper {
    @Autowired
    ModelMapper modelMapper;

    public CatJobsrctagInfEntity convert(ToKfTql toKfTql, String downstreamHostname, String striimHostname) {
        PropertyMap<ToKfTql, CatJobsrctagInfEntity> toKfTqlMap = new PropertyMap<ToKfTql, CatJobsrctagInfEntity>() {
            @Override
            protected void configure() {
                map().setSrcObjGbnCd("Trail File");
                map().setTargetObjGbnCd("topic");
                map().getKey().setJobSysIpAddr(striimHostname);
                map().setSrcObjIpAddr(downstreamHostname);
                skip().setLinPrivateYn(null);
                skip().setJobSysId(null);
            }
        };
        modelMapper.addMappings(toKfTqlMap);
        return modelMapper.map(toKfTql, CatJobsrctagInfEntity.class);
    }
}