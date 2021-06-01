package ru.gbuac.camunda.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Pozdeyev Dmitry
 * @since 27.05.2021 19:06
 */
@Data
@Entity
@Table(name = "access_approve")
public class AccessApprove {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "entity_id")
  private Long entityId;

  @Column(name = "username")
  private String username;

  @Column(name = "comment")
  private String comment;

  @Column(name = "approver")
  private String approver;

  @Column(name = "is_approved")
  private Boolean isApproved;
}
