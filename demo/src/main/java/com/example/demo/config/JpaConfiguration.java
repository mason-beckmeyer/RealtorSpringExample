package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class JpaConfiguration {

    @Value("${spring.datasourceNew.url}")
    private String urlNew;

    @Value("${spring.datasourceNew.username}")
    private String usernameNew;

    @Value("${spring.datasourceNew.password}")
    private String passwordNew;

    @Value("${spring.datasourceNew.driver-class-name}")
    private String driverClassNameNew;

    @Value("${spring.datasourceOld.url}")
    private String urlOld;

    @Value("${spring.datasourceOld.username}")
    private String usernameOld;

    @Value("${spring.datasourceOld.password}")
    private String passwordOld;

    @Value("${spring.datasourceOld.driver-class-name}")
    private String driverClassNameOld;

    /**
     * The new Database we are migrating to
     * @return
     */
    @Bean(name = "newDataSourceNew")
    @Primary
    public DataSource dataSourceNew() {
        return DataSourceBuilder.create()
                .url(urlNew)
                .username(usernameNew)
                .password(passwordNew)
                .driverClassName(driverClassNameNew)
                .build();
    }

    /**
     * Our Old Database Connection
     * @return
     */
    @Bean(name = "oldDataSourceOld")
    public DataSource dataSourceOld() {
        return DataSourceBuilder.create()
                .url(urlOld)
                .username(usernameOld)
                .password(passwordOld)
                .driverClassName(driverClassNameOld)
                .build();
    }
}
