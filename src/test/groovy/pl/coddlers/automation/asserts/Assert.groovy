package pl.coddlers.automation.asserts

import com.google.gson.*
import groovy.json.JsonSlurper
import io.restassured.response.Response

import java.lang.reflect.Type
import java.sql.Timestamp

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

    def toCustomJson() {
        new GsonBuilder().registerTypeAdapter(Timestamp.class, new JsonSerializer() {
            @Override
            JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
                return
            }
        })
    }

    def code() {
        this.response.statusCode()
    }

    def successful() {
        assert this.response.statusCode() in [200, 201, 202, 203, 204, 205, 206]
        this
    }
}
