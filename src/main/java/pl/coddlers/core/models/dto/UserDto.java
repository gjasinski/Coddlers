package pl.coddlers.core.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @JsonProperty("userMail")
    @Size(min = 4, max = 50)
    @NotNull
    private String userMail;

    @JsonIgnore
    @JsonProperty("password")
    @Size(min = 4, max = 100)
    @NotNull
    private String password;

    @Size(min = 1, max = 100)
    @NotNull
    private String firstname;

    @Size(min = 1, max = 100)
    @NotNull
    private String lastname;

    @NotNull
    private String[] userRoles;
}
