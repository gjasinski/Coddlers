package pl.coddlers.models.dto;

public class ErrorInfoDto {
    private String message;

    public ErrorInfoDto(String message) {
        this.message = message;
    }

    public ErrorInfoDto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
