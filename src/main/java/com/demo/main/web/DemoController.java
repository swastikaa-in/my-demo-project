package com.demo.main.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	@PostMapping("/postdata")
	public String postData(@RequestBody String str) {
		return "Posted Data";
	}

	@PostMapping("/users/postdata")
	public String postUsersData(@RequestBody String str) {
		return "Posted Users Data";
	}
}
