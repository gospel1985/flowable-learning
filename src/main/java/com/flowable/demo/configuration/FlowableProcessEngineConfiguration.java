package com.flowable.demo.configuration;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.job.service.impl.asyncexecutor.AsyncJobExecutorConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class FlowableProcessEngineConfiguration {
    private static final String AUTO_DEPLOY_RESOURCE_LOCATION = "classpath*:flowable/autodeploy/*.bpmn";

    @Value(AUTO_DEPLOY_RESOURCE_LOCATION)
    private Resource[] processDefinitionResources;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource());
        config.setTransactionManager(dataSourceTransactionManager());
        config.setDatabaseSchemaUpdate(
            ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE); // If DB schema doesn't match, throw the exception.
        config.setDeploymentResources(processDefinitionResources); // Auto deploy bpmn files
        config.setAsyncExecutorActivate(true); // Enable async executor for external service task.
        config.setAsyncExecutorConfiguration(new AsyncJobExecutorConfiguration());
        config.setDisableIdmEngine(true);

        return config;
    }

    @Bean
    public ProcessEngine processEngine() {
        SpringProcessEngineConfiguration config = springProcessEngineConfiguration();
        return config.buildProcessEngine();
    }
}
