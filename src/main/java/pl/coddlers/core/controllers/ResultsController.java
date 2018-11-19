package pl.coddlers.core.controllers;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.ResultDto;
import pl.coddlers.core.services.ResultsService;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

  @Autowired
  public ResultsController(ResultsService resultsService) {
    this.resultsService = resultsService;
  }

  private final ResultsService resultsService;

  @GetMapping
  public ResponseEntity<Collection<ResultDto>> getResults() {
    return ResponseEntity.ok(resultsService.getResults());
  }
}
