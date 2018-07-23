package pl.coddlers.core.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthenticationDto {

    @JsonProperty("userMail")
    @NotNull
    private String mail;

    @JsonProperty("password")
    @NotNull
    private String password;
}
