package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.CourseEdition

@InheritConstructors
class CourseEditionAssert extends Assert{

    CourseEdition asObject() {
        new Gson().fromJson(toJson(), new TypeToken<CourseEdition>() {}.getType())
    }
}