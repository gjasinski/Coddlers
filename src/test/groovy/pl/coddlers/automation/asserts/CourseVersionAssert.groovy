package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.CourseVersion

@InheritConstructors
class CourseVersionAssert extends Assert {

    CourseVersion asObject() {
        new Gson().fromJson(toJson(), new TypeToken<CourseVersion>() {}.getType())
    }
}