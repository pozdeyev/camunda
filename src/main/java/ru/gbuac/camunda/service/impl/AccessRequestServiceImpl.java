package ru.gbuac.camunda.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gbuac.camunda.configuration.ProcessVariableConstants;
import ru.gbuac.camunda.dto.AccessRequest;
import ru.gbuac.camunda.entity.AccessApprove;
import ru.gbuac.camunda.repository.AccessRequestRepository;
import ru.gbuac.camunda.service.AccessRequestService;

import java.util.*;

/**
 * @author Pozdeyev Dmitry
 * @since 25.05.2021 10:12
 */
@Slf4j
@Service
public class AccessRequestServiceImpl implements AccessRequestService {

  private final AccessRequestRepository accessRequestRepository;
  private final RuntimeService runtimeService;
  private final TaskService taskService;

  public AccessRequestServiceImpl(
      AccessRequestRepository accessRequestRepository,
      RuntimeService runtimeService,
      TaskService taskService) {
    this.accessRequestRepository = accessRequestRepository;
    this.runtimeService = runtimeService;
    this.taskService = taskService;
  }

  @Override
  public ResponseEntity<String> startProcessApprove(AccessRequest accessRequest) {
    Map<String, Object> vars = new HashMap<>();
    vars.put(ProcessVariableConstants.ENTITY_ID, accessRequest.getEntityId());
    vars.put(ProcessVariableConstants.USERNAME, accessRequest.getUsername());
    vars.put(ProcessVariableConstants.COMMENT, accessRequest.getComment());
    String processInstanceId =
        runtimeService
            .createProcessInstanceByKey("accessRequestHandling")
            .setVariables(vars)
            .execute()
            .getProcessInstanceId();
    ActivityInstance activityInstance = runtimeService.getActivityInstance(processInstanceId);

    return new ResponseEntity<>(
        "processInstanceId: " + processInstanceId + "\n" + activityInstance.getActivityName(),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> getTaskNameByProcessInstanceId(String processInstanceId) {
    try {
      if (isProcessInstanceExist(processInstanceId)) {
        return new ResponseEntity<>(
            getTaskByProcessInstanceId(processInstanceId).getName(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Process instance not found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @Override
  public ResponseEntity<String> sendToProcessIsApproved(
      String processInstanceId, String approver, Boolean isApprove) {
    try {
      if (isProcessInstanceExist(processInstanceId)) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(ProcessVariableConstants.APPROVER, approver);
        variables.put(ProcessVariableConstants.IS_APPROVED, isApprove);
        Task task = getTaskByProcessInstanceId(processInstanceId);
        taskService.claim(task.getId(), "Dmitriy");
        taskService.complete(task.getId(), variables);
        return new ResponseEntity<>(
            getTaskByProcessInstanceId(processInstanceId).getName(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Process instance not found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @Override
  public ResponseEntity<String> deleteProcessByInstanceId(String processInstanceId) {
    try {
      if (isProcessInstanceExist(processInstanceId)) {
        runtimeService.deleteProcessInstance(processInstanceId, "complete");
        return new ResponseEntity<>(
            "processInstanceId: " + processInstanceId + " is deleted", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Process instance not found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @Override
  public ResponseEntity<List<String>> getActiveProcessInstances() {

    List<ProcessInstance> processInstances = getAllRunningProcessInstances();
    List<String> processInstancesString = new ArrayList<>();
    for (ProcessInstance processInstance : processInstances) {
      processInstancesString.add(processInstance.toString());
    }

    return new ResponseEntity<>(processInstancesString, HttpStatus.OK);
  }

  private List<ProcessInstance> getAllRunningProcessInstances() {
    return runtimeService.createProcessInstanceQuery().active().list();
  }

  private Task getTaskByProcessInstanceId(String processInstanceId) {
    List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    return taskList.get(0);
  }

  private Boolean isProcessInstanceExist(String processInstanceId) {
    ActivityInstance activityInstance = runtimeService.getActivityInstance(processInstanceId);
    return activityInstance != null;
  }

  @Override
  public Integer create(Long entityId, String username, String comment) {
    AccessApprove accessApprove = new AccessApprove();
    accessApprove.setEntityId(entityId);
    accessApprove.setUsername(username);
    accessApprove.setComment(comment);

    return accessRequestRepository.saveAndFlush(accessApprove).getId();
  }

  @Override
  public void update(Long entityId, String approver, Boolean isApproved) {
    Optional<AccessApprove> accessRequestOptional =
        accessRequestRepository.findOneByEntityId(entityId);
    log.info(approver);
    log.info(isApproved.toString());
    if (accessRequestOptional.isPresent()) {
      AccessApprove accessApprove = accessRequestOptional.get();
      accessApprove.setApprover(approver);
      accessApprove.setIsApproved(isApproved);
      accessRequestRepository.saveAndFlush(accessApprove);
    }
  }
}
