package ru.gbuac.camunda.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Pozdeyev Dmitry
 * @since 25.05.2021 10:12
 */
@Data
public class AccessRequest {

    @JsonProperty("EntityID")
    private Long entityId;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Comment")
    private String comment;
}
