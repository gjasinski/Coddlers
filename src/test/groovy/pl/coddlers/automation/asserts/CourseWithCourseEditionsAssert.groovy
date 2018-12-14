package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.CourseWithCourseEdition

@InheritConstructors
class CourseWithCourseEditionsAssert extends Assert{

    List<CourseWithCourseEdition> asList() {
        new Gson().fromJson(toJson(), new TypeToken<List<CourseWithCourseEdition>>() {}.getType())
    }
}