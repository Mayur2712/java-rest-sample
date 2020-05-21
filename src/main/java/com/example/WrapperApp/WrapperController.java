package com.example.WrapperApp;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/front")
public class WrapperController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

    @Value("${backend.url}")
	private String backend_url;
	   
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
    @GetMapping(path="/getBackendCount", produces = "application/json")
    public String getBackendCount() {
    	RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(backend_url + "/employees/count", String.class);
    }
	
    @PostMapping(path= "/addBackendCount", consumes = "application/json", produces = "application/json")
    public String addBackendCount(@RequestBody String payload) 
                 throws Exception {       
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        return restTemplate.postForObject(backend_url + "/employees/", entity, String.class);
    }
}
