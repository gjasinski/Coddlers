package pl.coddlers.automation.rest.authentication

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.auth.Credentials
import spock.lang.Specification

class AuthenticationSpec extends Specification {

    def "Should login as a student"(){
        given:
            def user = Credentials.student()
            def coddlers = new CoddlersService(user)

        when:
            def resp = coddlers.authenticate()
            def token = resp.parse().token as String

        then:
            assert resp.code() == 200
            // todo check if valid jwt token
            assert token.length() >= 0
    }

    def "Should login as a teacher"(){
        given:
            def user = Credentials.teacher()
            def coddlers = new CoddlersService(user)

        when:
            def resp = coddlers.authenticate()
            def token = resp.parse().token as String

        then:
            assert resp.code() == 200
            // todo check if valid jwt token
            assert token.length() >= 0
    }

    def "Should NOT login as an unregistered user"(){
        given:
            def user = Credentials.unregisteredUser()
            def coddlers = new CoddlersService(user)

        when:
            def resp = coddlers.authenticate()

        then:
            assert resp.code() == 403
    }
}
