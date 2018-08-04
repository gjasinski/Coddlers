package pl.coddlers.core.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.models.entity.CourseVersion;

@Component
public class CourseVersionConverter implements BaseConverter<CourseVersion, CourseVersionDto> {

    @Override
    public CourseVersionDto convertFromEntity(CourseVersion entity) {
        CourseVersionDto courseVersionDto = new CourseVersionDto();
        courseVersionDto.setId(entity.getId());
        courseVersionDto.setVersionNumber(entity.getVersionNumber());

        return courseVersionDto;
    }

    @Override
    public CourseVersion convertFromDto(CourseVersionDto dto) {
        CourseVersion courseVersion = new CourseVersion();
        courseVersion.setVersionNumber(dto.getVersionNumber());

        return courseVersion;
    }
}
