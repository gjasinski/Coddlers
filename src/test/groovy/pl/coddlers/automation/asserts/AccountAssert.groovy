package pl.coddlers.automation.asserts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.transform.InheritConstructors
import pl.coddlers.automation.model.Account

@InheritConstructors
class AccountAssert extends Assert {

    Account asObject() {
        new Gson().fromJson(toJson(), new TypeToken<Account>() {}.getType())
    }
}