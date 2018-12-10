package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.Lesson

@InheritConstructors
class LessonAssert extends Assert {

    Lesson asObject() {
        new Gson().fromJson(toJson(), new TypeToken<Lesson>() {}.getType())
    }
}