package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.response.Task

@InheritConstructors
class TaskAssert extends Assert{

    Task asObject(){
        new Gson().fromJson(toJson(), new TypeToken<Task>() {}.getType())
    }
}
