package hr.vgsoft.cookbook.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/luka")
public class LukaResource {


    @GetMapping("/test")
    public String getTestString(@Value("${cookbook.luka.test-string}") String testValue) {
        return testValue;
    }

}
