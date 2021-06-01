package ru.gbuac.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.gbuac.camunda.configuration.ProcessVariableConstants;
import ru.gbuac.camunda.service.AccessRequestService;

/**
 * @author Pozdeyev Dmitry
 * @since 26.05.2021 11:11
 */
@Component
public class UpdateAccessRequest implements JavaDelegate {

  private final AccessRequestService accessRequestService;

  public UpdateAccessRequest(AccessRequestService accessRequestService) {
    this.accessRequestService = accessRequestService;
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    Long entityId = (Long) delegateExecution.getVariable(ProcessVariableConstants.ENTITY_ID);
    String approver = (String) delegateExecution.getVariable(ProcessVariableConstants.APPROVER);
    Boolean isApproved =
        (Boolean) delegateExecution.getVariable(ProcessVariableConstants.IS_APPROVED);

    accessRequestService.update(entityId, approver, isApproved);
  }


  //public void execute(DelegateExecution delegateExecution, String approver, Boolean isApprove) throws Exception {
  //  Long entityId = (Long) delegateExecution.getVariable(ProcessVariableConstants.ENTITY_ID);
//    accessRequestService.update(entityId, approver, isApprove);
//  }
}
