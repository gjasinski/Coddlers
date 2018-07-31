package pl.coddlers.git.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseWithPathWithNamespaceDto {
    @JsonProperty("path_with_namespace")
    private String pathWithNamespace;
}
