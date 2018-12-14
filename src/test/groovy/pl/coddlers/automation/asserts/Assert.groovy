package pl.coddlers.automation.asserts

import com.google.gson.Gson
import groovy.json.JsonSlurper
import io.restassured.response.Response

class Assert {

    Response response

    Assert(Response response) {
        this.response = response
    }

    def parse() {
        new JsonSlurper().parseText(response.asString())
    }

    def toJson() {
        new Gson().toJson(parse())
    }

    def code() {
        this.response.statusCode()
    }

    def location(){
        this.response.getHeader('Location')
    }

    def successful() {
        assert this.response.statusCode() in [200, 201, 202, 203, 204, 205, 206]
        this
    }
}