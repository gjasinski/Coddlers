package pl.coddlers.automation

import com.sun.jndi.toolkit.url.Uri

import java.time.Instant

class Commons {

    static final maxLength = 255

    static uniqueName(Class aClass, int maxLength = Commons.maxLength){
        def now = Instant.now()
        def concat = "${aClass.getSimpleName()}-${now.toString()}"
        def result = concat.length() < maxLength ? concat : concat.substring(0, maxLength)
        result.toLowerCase()
    }

    static uniqueName(String prefix, int maxLength = Commons.maxLength){
        def now = Instant.now()
        def concat = "${prefix}-${now.toString()}"
        def result = concat.length() < maxLength ? concat : concat.substring(0, maxLength)
        result.toLowerCase()
    }

    static def retrieveLessonId(String location){
        (location =~ /lessons\/(\w*)/)[0][1] as Integer
    }

    static def retrieveTaskId(String location){
        (location =~ /tasks\/(\w*)/)[0][1] as Integer
    }

    static def retrieveCourseId(String location){
        (location =~ /courses\/(\w*)/)[0][1] as Integer
    }

    static def retrieveCourseEditionId(String location){
        (location =~ /editions\/(\w*)/)[0][1] as Integer
    }

    static def retrieveInvitationToken(String url){
        new URL(url.replace('#/','')).getQuery().split('=')[1]
    }
}