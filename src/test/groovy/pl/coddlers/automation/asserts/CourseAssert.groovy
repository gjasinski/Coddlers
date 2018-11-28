package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.asserts.response.Course

@InheritConstructors
class CourseAssert extends Assert {

    def asObject() {
        new Gson().fromJson(toJson(), new TypeToken<Course>() {}.getType())
    }
}
