package hr.vgsoft.cookbook.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/manja") 
public class ManjaTestController {
	
	@Value("${cookbook.manja.test-string}")
	private String testString;
	@GetMapping("/test")
	public String getString() {	
		System.out.println(testString);
		return testString;
		
	}

}
