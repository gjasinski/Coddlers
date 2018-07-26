package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.EditionDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.Edition;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class EditionConverter implements BaseConverter<Edition, EditionDto> {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public EditionDto convertFromEntity(Edition entity) {
        EditionDto editionDto = new EditionDto();
        editionDto.setId(entity.getId());
        editionDto.setTitle(entity.getTitle());
        editionDto.setVersion(entity.getVersion());
        editionDto.setStartDate(entity.getStartDate());
        editionDto.setCourseId(entity.getCourseId());

        Course course = courseRepository.getById(editionDto.getCourseId()).get();
        editionDto.setEndDate(course.getEndDate());

        return editionDto;
    }

    @Override
    public Edition convertFromDto(EditionDto dto) {
        Edition edition = new Edition();
        edition.setTitle(dto.getTitle());
        edition.setStartDate(dto.getStartDate());
        edition.setVersion(dto.getVersion());
        edition.setCourseId(dto.getCourseId());

        return edition;
    }
}
