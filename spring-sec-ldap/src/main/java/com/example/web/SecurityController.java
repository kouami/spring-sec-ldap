package com.example.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.DeveloperService;

@Controller
public class SecurityController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeveloperService devService;
	
	@RequestMapping("/secure/apps")
	public String home(Principal currentUser) {
		logger.debug("Current User " + currentUser.getName() + " logged in...");
		return "secure/apps";
	}
	
	@RequestMapping("/register")
	public String register() {
		logger.debug("Redirect to index page for now");
		return "index"; // for now
	}
}
