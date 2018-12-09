package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.CourseEdition

@InheritConstructors
class CourseEditionsAssert extends Assert{

    List<CourseEdition> asList() {
        new Gson().fromJson(toJson(), new TypeToken<List<CourseEdition>>() {}.getType())
    }
}
