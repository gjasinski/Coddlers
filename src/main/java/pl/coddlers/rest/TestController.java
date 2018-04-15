package pl.coddlers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path="/api")
public class TestController {
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "{ " +
                "\"message\": \"Hello world man!\"" +
                "}";
    }

    @GetMapping(path = "/greetings/{name}")
    public String greetings(@PathVariable(name = "name") String name){
        return "{ " +
                "\"message\": \"Hello world " + name + "!\"" +
                "}";
    }
}
