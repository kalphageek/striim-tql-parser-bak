package me.kalpha.catalog.striim.config;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.catalog.striim.entity.CatJobsrctagInfEntity;
import me.kalpha.catalog.striim.parser.ToPsKfTql;
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
import java.util.List;

@Slf4j
@Configuration
@NoArgsConstructor
public class ToPsKfTqlConfig {
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
    public Job toPsKfTqlJob() {
        return jobBuilderFactory.get("toPsKfTqlJob")
                .start(toPsKfTqlStep(null, null))
                .incrementer(new RunIdIncrementer()) //자동으로 run.id를 파라미터로 할당해 줌으로 재실행이 가능하도록 한다
                .build();
    }

    @Bean
    @JobScope
    public Step toPsKfTqlStep(@Value("#{jobParameters[downstreamHostname]}") String downstreamHostname, @Value("#{jobParameters[tqlDirectory]}") String tqlDirectory) {
        return stepBuilderFactory.get("toPsKfTqlStep")
                .<ToPsKfTql, CatJobsrctagInfEntity>chunk(chunkSize)
                .reader(itemReader(tqlDirectory))
                .processor(itemProcessor(downstreamHostname))
                .writer(itemWriter())
                .build();
    }

    /*
     * ListItemReader : Paging이 필요없는 경우 메모리에 모두 올려서 처리. 데이터량이 많으면 Out of memory가 발생할 수 있다.
     */
    @Bean
    @StepScope
    public ListItemReader<ToPsKfTql> itemReader(String tqlDirectory) {
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

        List<ToPsKfTql> list = tqlParser.parseToPsKfTqls(files);
        return new ListItemReader<>(list);
    }
    @Bean
    @StepScope
    public ItemProcessor<ToPsKfTql, CatJobsrctagInfEntity> itemProcessor(String downstreamHostname) {
        return item -> {
            if ( modelMapper.getTypeMap(ToPsKfTql.class, CatJobsrctagInfEntity.class) == null) {
                PropertyMap<ToPsKfTql, CatJobsrctagInfEntity> propertyMap = new PropertyMap<ToPsKfTql, CatJobsrctagInfEntity>() {
                    @Override
                    protected void configure() {
                        map().getKey().setJobSysIpAddr(downstreamHostname);
                        map().setSrcObjIpAddr(downstreamHostname);
                        skip().setSrcObjSchemaNm(null);
                        skip().setTargetObjSchemaNm(null);
                        skip().setLinPrivateYn(null);
                        skip().setJobSysId(null);
                    }
                };
                modelMapper.addMappings(propertyMap);
            }

            CatJobsrctagInfEntity entity = modelMapper.map(item, CatJobsrctagInfEntity.class);

            logger.info("CatJobsrctagInfEntity : " + entity.toString());
            return entity;
        };
    }
    @Bean
    @StepScope
    public JpaItemWriter<CatJobsrctagInfEntity> itemWriter() {
        JpaItemWriter<CatJobsrctagInfEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}