package pl.coddlers.automation.model


import org.apache.commons.lang3.RandomStringUtils
import pl.coddlers.automation.Commons

class Account {
    String firstname
    String lastname
    String password
    String userMail
    Collection<String> userRoles

    Account(String firstname, String lastname, String password, String userMail, Collection<String> userRoles) {
        this.firstname = firstname
        this.lastname = lastname
        this.password = password
        this.userMail = userMail
        this.userRoles = userRoles
    }

    static Account sampleTeacher(String firstname = "${Commons.uniqueName('teacher-firstname')}",
                                 String lastname = "${Commons.uniqueName('teacher-lastname')}",
                                 String password = 'zaq1@WSX',
                                 String userMail = "${RandomStringUtils.randomAlphanumeric(20).toLowerCase()}@coddlers.pl",
                                 Collection<String> userRoles = ['ROLE_TEACHER']) {
        new Account(firstname, lastname, password, userMail, userRoles)
    }

    static Account sampleStudent(String firstname = "${Commons.uniqueName('student-firstname')}",
                                 String lastname = "${Commons.uniqueName('student-lastname')}",
                                 String password = 'zaq1@WSX',
                                 String userMail = "${RandomStringUtils.randomAlphanumeric(20).toLowerCase()}@coddlers.pl",
                                 Collection<String> userRoles = ['ROLE_STUDENT']) {
        new Account(firstname, lastname, password, userMail, userRoles)
    }

    static Account sample(String firstname = "${Commons.uniqueName('student-firstname')}",
                          String lastname = "${Commons.uniqueName('student-lastname')}",
                          String password = 'zaq1@WSX',
                          String userMail = "${RandomStringUtils.randomAlphanumeric(20).toLowerCase()}@coddlers.pl",
                          Collection<String> userRoles = []){
        new Account(firstname, lastname, password, userMail, userRoles)
    }

    Account withFirstname(String firstname){
        this.firstname = firstname
        this
    }

    Account withLastname(String lastname){
        this.lastname = lastname
        this
    }

    Account withPassword(String password){
        this.password = password
        this
    }

    Account withUserMail(String userMail){
        this.userMail = userMail
        this
    }
    Account withUserRoles(Collection<String> userRoles){
        this.userRoles = userRoles
        this
    }
}