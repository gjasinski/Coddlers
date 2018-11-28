package pl.coddlers.automation.auth

class Credentials {

    static User teacher(){
        new User('teacher.automation@coddlers.pl', 'zaq1@WSX')
    }

    static User student(){
        new User('student.automation@coddlers.pl', 'zaq1@WSX')
    }

    static User unregisteredUser(){
        new User('unregistered-user@coddlers.pl', 'zaq1@WSX')
    }
}
