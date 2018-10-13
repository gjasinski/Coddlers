package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.services.CourseEditionService;

import java.net.URI;

@RestController
@RequestMapping("api/editions")
public class CourseEditionController {

    private final CourseEditionService courseEditionService;

    @Autowired
    public CourseEditionController(CourseEditionService courseEditionService) {
        this.courseEditionService = courseEditionService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseEditionDto> getCourseEdition(@PathVariable Long id) {
        return ResponseEntity.ok(courseEditionService.getCourseEditionById(id));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createEdition(@RequestBody CourseEditionDto courseEditionDto) {
        CourseEdition courseEdition = courseEditionService.createCourseEdition(courseEditionDto);
        courseEditionService.cloneLessons(courseEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(courseEdition.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/invite", params = "courseEdition")
    public ResponseEntity<Void> addStudentToCourseEdition(@RequestParam(value="courseEdition") String invitationLink) {
        try {
            courseEditionService.addStudentToCourseEdition(invitationLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value="/getInvitationLink", params = "courseEditionId")
    public ResponseEntity<String> getInvitationLinkForCourseEdition(@RequestParam(value = "courseEditionId") Long invitationLink) {
        try {
            return ResponseEntity.ok(courseEditionService.getInvitationLink(invitationLink));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
