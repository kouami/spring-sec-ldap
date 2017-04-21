/**
 * 
 */
package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author efoeakolly
 *
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry reg) {
		reg.addViewController("/index").setViewName("index");
		reg.addViewController("/").setViewName("index");
		reg.addViewController("/login").setViewName("login");
		reg.addViewController("/registration").setViewName("registration");
	}
}
