package pl.coddlers.core.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.EditionDto;
import pl.coddlers.core.models.entity.Edition;

@Component
public class EditionConverter implements BaseConverter<Edition, EditionDto> {
    @Override
    public EditionDto convertFromEntity(Edition entity) {
        EditionDto editionDto = new EditionDto();
        editionDto.setId(entity.getId());
        editionDto.setTitle(entity.getTitle());
        editionDto.setVersion(entity.getVersion());
        editionDto.setStartDate(entity.getStartDate());
        editionDto.setEndDate(entity.getEndDate());
        editionDto.setCourseId(entity.getCourseId());

        return editionDto;
    }

    @Override
    public Edition convertFromDto(EditionDto dto) {
        Edition edition = new Edition();
        edition.setTitle(dto.getTitle());
        edition.setStartDate(dto.getStartDate());
        edition.setEndDate(dto.getEndDate());
        edition.setVersion(dto.getVersion());
        edition.setCourseId(dto.getCourseId());

        return edition;
    }
}
