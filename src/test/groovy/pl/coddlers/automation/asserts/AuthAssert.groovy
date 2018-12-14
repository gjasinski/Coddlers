package pl.coddlers.automation.asserts

import groovy.transform.InheritConstructors

@InheritConstructors
class AuthAssert extends Assert {

    String getToken() {
        parse().token as String
    }
}