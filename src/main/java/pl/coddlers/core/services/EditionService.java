package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.EditionNotFoundException;
import pl.coddlers.core.models.converters.EditionConverter;
import pl.coddlers.core.models.dto.EditionDto;
import pl.coddlers.core.models.entity.Edition;
import pl.coddlers.core.repositories.EditionRepository;

@Service
public class EditionService {

    private final EditionRepository editionRepository;

    private final EditionConverter editionConverter;

    @Autowired
    public EditionService(EditionRepository editionRepository, EditionConverter editionConverter) {
        this.editionRepository = editionRepository;
        this.editionConverter = editionConverter;
    }

    public EditionDto getEditionById(Long id) {
        Edition edition = validateEdition(id);
        return editionConverter.convertFromEntity(edition);
    }

    private Edition validateEdition(Long id) throws EditionNotFoundException {
        return editionRepository.findById(id).orElseThrow(() -> new EditionNotFoundException(id));
    }
}
