package com.example.demo.config;

import com.example.demo.dao.House;
import com.example.demo.dao.HouseRepository;
import com.example.demo.dao.OldHouse;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
@Configuration
@EnableBatchProcessing
public class HouseMigrationJobConfig {



    /**
     * 1.
     * Reads old house records from old database
     * @param dataSourceOld
     * @return
     */
    @Bean
    public JdbcCursorItemReader<OldHouse> oldHouseItemReader(@Qualifier("oldDataSourceOld")DataSource dataSourceOld){
        JdbcCursorItemReader<OldHouse> oldReader = new JdbcCursorItemReader<>(dataSourceOld,"SELECT id, address, owner, price FROM old_houses",new BeanPropertyRowMapper<>(OldHouse.class));
        oldReader.setName("oldHouseItemReader");

        return oldReader;

    }

    /**
     * 2.
     * Transforms OldHouse to House objects for the new db
     * @return
     */
    @Bean
    public ItemProcessor<OldHouse, House> processor(){
        return oldHouse -> {
            House house = new House();
            house.setAddress(oldHouse.getAddress());
            house.setOwner(oldHouse.getOwner());
            house.setPrice(oldHouse.getPrice());

            return house;
        };
    }

    /**
     * 3.
     * Writes Houses to the new db
     * @param houseRepository
     * @return
     */
    @Bean
    public RepositoryItemWriter<House> houseWriter(HouseRepository houseRepository){
        return new RepositoryItemWriter<>(houseRepository);
    }

    /**
     * 4.
     * Our step Chunk is # of entities we will process before a commit
     * We also bring all the components together here (reader, processor, writer)
     * @param jobRepository
     * @param transactionManager
     * @param oldHouseItemReader
     * @param processor
     * @param houseWriter
     * @return
     */
    @Bean
    public Step houseMigrationStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcCursorItemReader<OldHouse> oldHouseItemReader,
            ItemProcessor<OldHouse, House> processor,
            RepositoryItemWriter<House> houseWriter
    ){
        return new StepBuilder("migrationStep",jobRepository)
                .<OldHouse,House>chunk(10)
                .transactionManager(transactionManager)
                .reader(oldHouseItemReader)
                .processor(processor)
                .writer(houseWriter)
                .build();
    }

    /**
     *
     * @param jobRepository
     * @param houseMigrationStep
     * @return
     */
    @Bean
    public Job MigrationJob(JobRepository jobRepository, Step houseMigrationStep){
        return new JobBuilder("migrationJob",jobRepository)
                .start(houseMigrationStep)
                .build();
    }



}
