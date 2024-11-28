package com.flowable.demo.configuration;

import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowableProcessEngineServicesConfiguration {
    private final ProcessEngine processEngine;

    @Autowired
    public FlowableProcessEngineServicesConfiguration(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService() {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService() {
        return processEngine.getHistoryService();
    }

    @Bean
    public IdentityService identityService() {
        return processEngine.getIdentityService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine.getManagementService();
    }

    @Bean
    public FormService formService() {
        return processEngine.getFormService();
    }

    @Bean
    public ProcessDiagramGenerator diagramGenerator() {
        return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
    }
}
