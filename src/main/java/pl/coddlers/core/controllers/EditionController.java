package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.EditionDto;
import pl.coddlers.core.services.EditionService;

@RestController
@RequestMapping("api/editions")
public class EditionController {

	private final EditionService editionService;

	@Autowired
	public EditionController(EditionService editionService) {
		this.editionService = editionService;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<EditionDto> getEdition(@PathVariable Long id) {
		return ResponseEntity.ok(editionService.getEditionById(id));
	}
}
