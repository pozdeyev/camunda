package ru.gbuac.camunda.service;

import org.springframework.http.ResponseEntity;
import ru.gbuac.camunda.dto.AccessRequest;

import java.util.List;

public interface AccessRequestService {

  Integer create(Long entityId, String username, String comment);

  void update(Long entityId, String approver, Boolean isApproved);

  ResponseEntity<String> startProcessApprove(AccessRequest accessRequest);

  ResponseEntity<List<String>> getActiveProcessInstances();

  ResponseEntity<String> getTaskNameByProcessInstanceId(String processInstanceId);

  ResponseEntity<String> sendToProcessIsApproved(
      String processInstanceId, String approver, Boolean isApprove);

  ResponseEntity<String> deleteProcessByInstanceId(String processInstanceId);
}
