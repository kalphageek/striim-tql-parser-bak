package me.kalpha.catalog.striim.config;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.catalog.striim.entity.CatJobsrctagInfEntity;
import me.kalpha.catalog.striim.parser.ToKfTql;
import me.kalpha.catalog.striim.parser.TqlMapper;
import me.kalpha.catalog.striim.parser.TqlParser;
import me.kalpha.catalog.striim.repository.CatJobsrctagInfRepository;
import me.kalpha.catalog.striim.service.TqlService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.amqp.builder.AmqpItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
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
public class TqlParserConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    CatJobsrctagInfRepository catJobsrctagInfRepository;
    @Autowired
    TqlService tqlService;
    @Autowired
    TqlMapper tqlMapper;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean
    public Job TqlParserJob() {
        return jobBuilderFactory.get("tqlParserJob")
                .preventRestart()
                .start(toKfParserStep(null, null))
                .build();
    }

    @Bean
    @JobScope
    public Step toKfParserStep(@Value("#{jobParameters[downstreamHostname]}") String downstreamHostname, @Value("#{jobParameters[tqlDirectory]}") String tqlDirectory) {
        return stepBuilderFactory.get("toKfParserStep")
                .<ToKfTql, CatJobsrctagInfEntity>chunk(chunkSize)
                .reader(reader(tqlDirectory))
                .processor(processor(downstreamHostname))
                .writer(writer())
                .build();
    }

    /*
     * ListItemReader : Paging이 필요없는 경우 메모리에 모두 올려서 처리. 데이터량이 많으면 Out of memory가 발생할 수 있다.
     */
    @Bean
    @StepScope
    public ListItemReader<ToKfTql> reader(String tqlDirectory) {
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

        List<ToKfTql> list = tqlService.processToKfTql(files);
        return new ListItemReader<>(list);
    }

    @Bean
    @StepScope
    public ItemProcessor<ToKfTql, CatJobsrctagInfEntity> processor(String downstreamHostname) {
        return item -> {
            return tqlMapper.convert(item, downstreamHostname, downstreamHostname);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<CatJobsrctagInfEntity> writer() {
        return catJobsrctagInfRepository::saveAll;
    }

//    public JpaItemWriter<CatJobsrctagInfEntity> writer() {
//        JpaItemWriter<CatJobsrctagInfEntity> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
}