package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.CourseVersion

@InheritConstructors
class CourseVersionsAssert extends Assert {

    List<CourseVersion> asList() {
        new Gson().fromJson(toJson(), new TypeToken<List<CourseVersion>>() {}.getType())
    }

    CourseVersion last(){
        asList().toSorted().last()
    }
}