/**
 * 
 */
package com.example.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;


/**
 * @author efoeakolly
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication()
		.userSearchBase("ou=devs")
		.userSearchFilter("uid={0}")
		.groupSearchBase("ou=apps")
		.groupSearchFilter("uniqueMember={0}")
		.contextSource(contextSource())
		.passwordCompare()
		.passwordEncoder(new LdapShaPasswordEncoder())
		.passwordAttribute("userPassword");
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		return new DefaultSpringSecurityContextSource(Arrays.asList("ldap://localhost:10389"), "dc=applications,dc=com");
	}
	
	@Bean
	public LdapTemplate getLdapTemplate() {
		return new LdapTemplate(contextSource());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**","/js/**","/fonts/**","/img/**", "/", "/index","/registration").permitAll();
		http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/secure/apps").failureUrl("/login?error=true")
		.and().logout().logoutSuccessUrl("/login.html").invalidateHttpSession(true).deleteCookies("JSESSIONID");
		http.authorizeRequests().anyRequest().authenticated();
	}
}
