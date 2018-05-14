package pl.coddlers.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createAssignment(@Valid @RequestBody AssignmentDto assignmentDto) {

    }

    public ResponseEntity getAllAssignments() {

    }
}
