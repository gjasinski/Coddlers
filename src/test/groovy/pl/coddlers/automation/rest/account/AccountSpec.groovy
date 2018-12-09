package pl.coddlers.automation.rest.account

import org.apache.commons.lang3.StringUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.auth.Credentials
import pl.coddlers.automation.auth.User
import pl.coddlers.automation.model.Account
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

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

    @Unroll
    def "Should get proper user when logged as #userDescription"(){
        when:
            def resp = service.getAccount()

        and:
            def respAccount = resp.asObject() as Account

        then:
            assert StringUtils.isNotEmpty(respAccount.firstname)
            assert StringUtils.isNotEmpty(respAccount.lastname)
            assert StringUtils.isEmpty(respAccount.password)
            assert respAccount.userMail == user.userMail

        where:
            userDescription     | user                  | service
            'student'           | Credentials.student() | CoddlersService.asStudent()
            'teacher'           | Credentials.teacher() | CoddlersService.asTeacher()
    }

    @PendingFeature // todo investigate why 500 is returned
    def "Should get proper user after register"(){
        given:
            def service = new CoddlersService()
            def account = Account.sample()

        when: "register"
            service.register(account).successful()

        and: "authenticate"
            service = new CoddlersService(new User(account.userMail.toLowerCase(), account.password))
                    .withHeaders()

        and: "get current user"
            def resp = service.getAccount().successful()
            def respAccount = resp.asObject() as Account

        then:
            assert respAccount.firstname == account.firstname
            assert respAccount.lastname == account.lastname
            assert respAccount.userRoles == account.userRoles
            assert respAccount.userMail == account.userMail
    }
}
