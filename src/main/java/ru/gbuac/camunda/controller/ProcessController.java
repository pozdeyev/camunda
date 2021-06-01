package ru.gbuac.camunda.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gbuac.camunda.dto.AccessRequest;
import ru.gbuac.camunda.service.AccessRequestService;

import java.util.List;

/**
 * @author Pozdeyev Dmitry
 * @since 27.05.2021 12:09
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/process")
@AllArgsConstructor
public class ProcessController {

  private final AccessRequestService accessRequestService;

  @PostMapping
  public ResponseEntity<String> startProcessApprove(@RequestBody AccessRequest request) {
    log.info("startProcessApprove");
    return accessRequestService.startProcessApprove(request);
  }

  @PostMapping("/approve/")
  public ResponseEntity<String> sendToProcessIsApproved(
      @RequestParam(name = "processInstanceId") String processInstanceId,
      @RequestParam(name = "approver") String approver,
      @RequestParam(name = "isApprove") Boolean isApprove) {
    log.info("sendToProcessIsApproved");
    return accessRequestService.sendToProcessIsApproved(processInstanceId, approver, isApprove);
  }

  @GetMapping("/current_task_name")
  public ResponseEntity<String> getTaskNameByProcessInstanceId(
      @RequestParam(name = "processInstanceId") String processInstanceId) {
    log.info("getTaskNameByProcessInstanceId()");
    return accessRequestService.getTaskNameByProcessInstanceId(processInstanceId);
  }

  @GetMapping("/active_processes")
  public ResponseEntity<List<String>> getActiveProcessInstances() {
    log.info("getActiveProcessInstances()");
    return accessRequestService.getActiveProcessInstances();
  }

  @DeleteMapping("/delete_process")
  public ResponseEntity<String> deleteProcessByInstanceId(
      @RequestParam(name = "processInstanceId") String processInstanceId) {
    log.info("deleteProcessByInstanceId()");
    return accessRequestService.deleteProcessByInstanceId(processInstanceId);
  }
}
