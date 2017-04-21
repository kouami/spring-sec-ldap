package com.example.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.DeveloperService;

@Controller
public class SecurityController {
	
	@Autowired
	private DeveloperService devService;
	
	@RequestMapping("/secure/apps")
	public String home(Principal currentUser) {
		return "secure/apps";
	}
	
	@RequestMapping("/register")
	public String register() {
		return "index"; // for now
	}
}
