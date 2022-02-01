package hr.vgsoft.cookbook.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/manja") 
public class ManjaTestController {
	
	private final Logger log = LoggerFactory.getLogger(UserResource.class);
	
	@Value("${cookbook.manja.test-string}")
	private String testString;
	
	@GetMapping("/test")
	public String getString() {	
		log.debug(testString);
		return testString;		
	}

}
