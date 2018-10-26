package pl.coddlers.git.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseForProject {
    // TODO: 29.09.18 replace with ProjectDto
    private Long id;

    @JsonProperty("path_with_namespace")
    private String pathWithNamespace;

}
