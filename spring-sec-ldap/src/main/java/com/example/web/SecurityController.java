package com.example.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
	
	@RequestMapping("/secure/apps")
	public String home(Principal currentUser) {
		return "secure/apps";
	}
}
