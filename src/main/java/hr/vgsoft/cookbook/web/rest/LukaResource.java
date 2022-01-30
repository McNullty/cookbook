package hr.vgsoft.cookbook.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/luka")
public class LukaResource {

    @Value("${cookbook.luka.test-string}")
    private String testValue;

    @GetMapping("/test")
    public String getTestString() {
        return testValue;
    }

}
