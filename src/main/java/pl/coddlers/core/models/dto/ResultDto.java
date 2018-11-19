package pl.coddlers.core.models.dto;

import java.util.Collection;
import lombok.Data;

@Data
public class ResultDto {

  private final String courseName;
  private final Collection<String> teachers;

  public ResultDto(String courseName, Collection<String> teachers) {
    this.courseName = courseName;
    this.teachers = teachers;
  }
}
