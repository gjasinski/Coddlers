package pl.coddlers.automation.rest.account

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Account
import spock.lang.Shared
import spock.lang.Specification

class AccountSpec extends Specification {

    @Shared coddlers = new CoddlersService()

    def "Should register as a teacher"() {
        given:
            def user = Account.sampleTeacher()

        when:
            def resp = coddlers.register(user)

        then:
            assert resp.code() == 201
    }

    def "Should register as a student"() {
        given:
            def user = Account.sampleStudent()

        when:
            def resp = coddlers.register(user)

        then:
            assert resp.code() == 201
    }

    def "Should register as a user without role"() {
        given:
            def user = Account.sample()

        when:
            def resp = coddlers.register(user)

        then:
            assert resp.code() == 201
    }
}
