package pl.coddlers.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AuthenticationDto {

    @JsonProperty("userMail")
    @NotNull
    private String mail;

    @JsonProperty("password")
    @NotNull
    private String password;

    public AuthenticationDto() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
