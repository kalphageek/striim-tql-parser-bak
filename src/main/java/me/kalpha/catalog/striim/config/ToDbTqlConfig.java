package me.kalpha.catalog.striim.config;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.catalog.striim.common.JpaItemListWriter;
import me.kalpha.catalog.striim.entity.CatJobsrctagInfEntity;
import me.kalpha.catalog.striim.parser.ToDbTql;
import me.kalpha.catalog.striim.parser.TqlParser;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@NoArgsConstructor
public class ToDbTqlConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    TqlParser tqlParser;
    @Autowired
    ModelMapper modelMapper;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean
    public Job tqlParserJob() {
        return jobBuilderFactory.get("toDbTqlJob")
                .start(tqlParserStep(null, null))
                .incrementer(new RunIdIncrementer()) //자동으로 run.id를 파라미터로 할당해 줌으로 재실행이 가능하도록 한다
                .build();
    }

    @Bean
    @JobScope
    public Step tqlParserStep(@Value("#{jobParameters[striimHostname]}") String striimHostname, @Value("#{jobParameters[tqlDirectory]}") String tqlDirectory) {
        return stepBuilderFactory.get("toDbTqlStep")
                .<ToDbTql, List<CatJobsrctagInfEntity>>chunk(chunkSize)
                .reader(itemReader(tqlDirectory))
                .processor(itemProcessor(striimHostname))
                .writer(itemListWriter())
                .build();
    }

    /*
     * ListItemReader : Paging이 필요없는 경우 메모리에 모두 올려서 처리. 데이터량이 많으면 Out of memory가 발생할 수 있다.
     */
    @StepScope
    public ListItemReader<ToDbTql> itemReader(String tqlDirectory) {
        /*
         * 전체 .tql 파일 찾기
         */
        File dir = new File(tqlDirectory);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".tql");
            }
        });

        List<ToDbTql> list = tqlParser.parseToDbTqls(files);

        return new ListItemReader<>(list);
    }

    @StepScope
    public ItemProcessor<ToDbTql, List<CatJobsrctagInfEntity>> itemProcessor(String striimHostname) {
        return item -> {
            if ( modelMapper.getTypeMap(ToDbTql.class, CatJobsrctagInfEntity.class) == null) {
                PropertyMap<ToDbTql, CatJobsrctagInfEntity> propertyMap = new PropertyMap<ToDbTql, CatJobsrctagInfEntity>() {
                    @Override
                    protected void configure() {
                        map().getKey().setJobSysIpAddr(striimHostname);
                        map().setSrcObjIpAddr(striimHostname);
                        skip().setSrcObjSchemaNm(null);
                        skip().setLinPrivateYn(null);
                        skip().setJobSysId(null);
                        skip().setTargetObjNm(null);
                    }
                };
                modelMapper.addMappings(propertyMap);
            }

            List<CatJobsrctagInfEntity> list = new ArrayList<CatJobsrctagInfEntity>();
            CatJobsrctagInfEntity mappedEntity = modelMapper.map(item, CatJobsrctagInfEntity.class);
            for (String tableName:item.getTargetObjNm()) {
                CatJobsrctagInfEntity entity = mappedEntity;
                entity.setTargetObjNm(tableName);
                list.add(entity);
                logger.info("CatJobsrctagInfEntity : " + entity.toString());
            }
            return list;
        };
    }

    @StepScope
    public JpaItemListWriter<CatJobsrctagInfEntity> itemListWriter() {
        JpaItemWriter<CatJobsrctagInfEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return new JpaItemListWriter<>(writer);
    }
}