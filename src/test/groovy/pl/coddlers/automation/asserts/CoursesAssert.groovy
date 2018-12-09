package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import io.swagger.models.auth.In
import pl.coddlers.automation.model.response.Course

@InheritConstructors
class CoursesAssert extends Assert {

    List<Course> asList() {
        new Gson().fromJson(toJson(), new TypeToken<List<Course>>() {}.getType())
    }

    Course getCourse(Integer courseId){
        this.asList().find {
            course -> course.id == courseId
        }
    }

    Course getCourse(String title, String description){
        this.asList().find {
            course -> course.title == title && course.description == description
        }
    }
}
