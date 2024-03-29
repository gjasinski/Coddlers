package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseEditionLessonDto;
import pl.coddlers.core.models.dto.CourseWithCourseEditionDto;
import pl.coddlers.core.models.dto.InvitationDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.models.dto.InvitationLinkDto;
import pl.coddlers.core.services.CourseEditionService;
import pl.coddlers.core.services.UserDetailsServiceImpl;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("api/editions")
public class CourseEditionController {

    private final CourseEditionService courseEditionService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionController(CourseEditionService courseEditionService, UserDetailsServiceImpl userDetailsService) {
        this.courseEditionService = courseEditionService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseEditionDto> getCourseEdition(@PathVariable Long id) {
        return ResponseEntity.ok(courseEditionService.getCourseEditionById(id));
    }

    @GetMapping(value = "/{id}/course-edition-lessons", params = {"lessonId"})
    public ResponseEntity<Collection<CourseEditionLessonDto>> getCourseEditionLessonList(@PathVariable Long id,
                                                                                         @RequestParam(value = "lessonId", required = false) Long lessonId) {
        if (lessonId != null) {
            return ResponseEntity.ok(Collections.singletonList(courseEditionService.getCourseEditionLesson(lessonId, id)));
        }

        return ResponseEntity.ok(courseEditionService.getCourseEditionLessonList(id));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/course-edition-lessons/{id}")
    public ResponseEntity<Void> updateCourseEditionLesson(@PathVariable Long id,
                                                          @Valid @RequestBody CourseEditionLessonDto courseEditionLessonDto) {
        courseEditionService.updateCourseEditionLesson(id, courseEditionLessonDto);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createEdition(@Valid @RequestBody CourseEditionDto courseEditionDto) {
        CourseEdition courseEdition = courseEditionService.createCourseEdition(courseEditionDto);
        courseEditionService.createCourseEditionLessons(courseEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(courseEdition.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/invitations/emails")
    public ResponseEntity<Void> sendInvitationLinkByMail(@RequestBody InvitationDto invitationDto) {
        List<InternetAddress> studentEmails = new LinkedList<>();
        try {
            for (String studentEmail : invitationDto.getStudentEmails()) {
                studentEmails.add(new InternetAddress(studentEmail));
            }
            courseEditionService.sendInvitationLinkByMail(invitationDto.getInvitationLink().getLink(), studentEmails);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/invitations")
    public ResponseEntity<Boolean> addStudentToCourseEdition(@RequestBody String invitationToken) {
        boolean result = false;
        try {
            result = courseEditionService.addStudentToCourseEdition(invitationToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/invitation-link", params = "courseEditionId")
    public ResponseEntity<InvitationLinkDto> getInvitationLinkForCourseEdition(HttpServletRequest request, @RequestParam(value = "courseEditionId") Long courseEditionId) {
        try {
            return ResponseEntity.ok(courseEditionService.getInvitationLink(request.getHeader("host"), courseEditionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO it need to be refactored and rethinked
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping
    public ResponseEntity<List<CourseWithCourseEditionDto>> getCourses() {
        User currentUser = userDetailsService.getCurrentUserEntity();
        return ResponseEntity.ok(courseEditionService.getAllEditionsWithEnrolledStudent(currentUser));
    }
}
