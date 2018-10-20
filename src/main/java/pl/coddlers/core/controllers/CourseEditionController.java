package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.services.CourseEditionService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

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
        courseEditionService.createCourseEditionLessons(courseEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(courseEdition.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/invitations", params = {"invitationToken"})
    public ResponseEntity<Void> sendInvitationLinkByMail(HttpServletRequest request, @RequestParam(value = "invitationToken") String invitationToken, @RequestBody List<String> students) {
        List<InternetAddress> studentsEmails = new LinkedList<>();
        students.forEach(s -> {
            try {
                studentsEmails.add(new InternetAddress(s));
            } catch (AddressException e) {
                e.printStackTrace();
            }
        });

        try {
            courseEditionService.sendInvitationLinkByMail(request.getHeader("host"), invitationToken, studentsEmails);
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
    @GetMapping(value = "/invitation-tokens", params = "courseEditionId")
    public ResponseEntity<String> getInvitationTokenForCourseEdition(HttpServletRequest request, @RequestParam(value = "courseEditionId") Long courseEditionId) {
        try {
            return ResponseEntity.ok(courseEditionService.getInvitationToken(request.getHeader("host"), courseEditionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
