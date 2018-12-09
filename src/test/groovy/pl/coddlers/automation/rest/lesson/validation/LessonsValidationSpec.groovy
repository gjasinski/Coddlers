package pl.coddlers.automation.rest.lesson.validation

import pl.coddlers.automation.CoddlersService
import spock.lang.Shared
import spock.lang.Specification

class LessonsValidationSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()

}
