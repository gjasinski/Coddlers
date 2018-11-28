package pl.coddlers.automation.auth

class User {
    String userMail
    String password

    User(String userMail, String password) {
        this.userMail = userMail
        this.password = password
    }
}
