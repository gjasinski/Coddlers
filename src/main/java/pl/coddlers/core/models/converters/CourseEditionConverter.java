package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.CourseVersionRepository;

@Component
public class CourseEditionConverter implements BaseConverter<CourseEdition, CourseEditionDto> {

    private final CourseRepository courseRepository;
    private final CourseVersionRepository courseVersionRepository;
    private final CourseVersionConverter courseVersionConverter;

    @Autowired
    public CourseEditionConverter(CourseRepository courseRepository, CourseVersionRepository courseVersionRepository, CourseVersionConverter courseVersionConverter) {
        this.courseRepository = courseRepository;
        this.courseVersionRepository = courseVersionRepository;
        this.courseVersionConverter = courseVersionConverter;
    }

    @Override
    public CourseEditionDto convertFromEntity(CourseEdition entity) {
        CourseEditionDto courseEditionDto = new CourseEditionDto();
        courseEditionDto.setId(entity.getId());
        courseEditionDto.setTitle(entity.getTitle());
        courseEditionDto.setCourseVersion(courseVersionConverter.convertFromEntity(entity.getCourseVersion()));
        courseEditionDto.setStartDate(entity.getStartDate());

        return courseEditionDto;
    }

    @Override
    public CourseEdition convertFromDto(CourseEditionDto dto) {
        System.out.println(dto);
        CourseEdition courseEdition = new CourseEdition();
        courseEdition.setTitle(dto.getTitle());
        courseEdition.setStartDate(dto.getStartDate());
        courseEdition.setCourseVersion(courseVersionRepository.getOne(dto.getCourseVersion().getId()));

        return courseEdition;
    }
}
