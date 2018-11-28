package pl.coddlers.automation

import java.time.Instant

class Commons {

    static final maxLength = 255

    static uniqueName(Class aClass, int maxLength = Commons.maxLength){
        def now = Instant.now()
        def concat = "${aClass.getSimpleName()}-${now.toString()}"
        return concat.length() < maxLength ? concat : concat.substring(0, maxLength)
    }

    static uniqueName(String prefix, int maxLength = Commons.maxLength){
        def now = Instant.now()
        def concat = "${prefix}-${now.toString()}"
        return concat.length() < maxLength ? concat : concat.substring(0, maxLength)
    }
}
