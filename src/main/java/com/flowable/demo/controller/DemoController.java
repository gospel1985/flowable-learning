package com.flowable.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Log4j2
@RestController("/demo")
public class DemoController {
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final ProcessDiagramGenerator diagramGenerator;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;

    @Autowired
    public DemoController(
        RuntimeService runtimeService,
        RepositoryService repositoryService,
        ProcessDiagramGenerator diagramGenerator,
        TaskService taskService,
        HistoryService historyService,
        ProcessEngine processEngine) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.diagramGenerator = diagramGenerator;
        this.taskService = taskService;
        this.historyService = historyService;
        this.processEngine = processEngine;
    }

    @PostMapping("/start")
    public void start(String businessKey) {
        ProcessDefinition definition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey("test")
            .latestVersion()
            .singleResult();

        ProcessInstanceBuilder processInstanceBuilder = runtimeService
            .createProcessInstanceBuilder()
            .processDefinitionId(definition.getId())
            .businessKey(businessKey);

        processInstanceBuilder.start();
    }

    @GetMapping(value = "/diagram", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getProcessDiagram(String businessKey) throws IOException {
        String processDefinitionId;
        List<String> activeActivityIds;

        ProcessInstance processInstance = runtimeService
            .createProcessInstanceQuery()
            .processDefinitionCategory("TEST")
            .processInstanceBusinessKey(businessKey)
            .singleResult();

        processDefinitionId = processInstance.getProcessDefinitionId();
        activeActivityIds = runtimeService.getActiveActivityIds(processInstance.getProcessInstanceId());

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        InputStream stream = diagramGenerator.generateDiagram(
            bpmnModel, "png", activeActivityIds, Collections.emptyList(), 1.0D, true);

        return IOUtils.toByteArray(stream);
    }
}
