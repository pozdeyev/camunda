package ru.gbuac.camunda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gbuac.camunda.entity.AccessApprove;

import java.util.Optional;

/**
 * @author Pozdeyev Dmitry
 * @since 25.05.2021 10:12
 */
public interface AccessRequestRepository extends JpaRepository<AccessApprove, Long> {

  Optional<AccessApprove> findOneByEntityId(Long entityId);
}
