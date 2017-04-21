/* Created Apr 20, 2017
 *
 * @(#)IDeveloperDao.java
 *
 * (C)2017 - Emmanuel Akolly
 */
package com.example.dao;

import java.util.List;

import com.example.model.Developer;

/**
 * @author efoeakolly
 *
 */
public interface IDeveloperDao {
	
	/**
	 * 
	 * @return
	 */
	public List<Developer> getAllDevelopers();
	
	/**
	 * 
	 * @param lastName
	 * @return
	 */
	public List<Developer> getDeveloperByLastName(String lastName);
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public Developer getDeveloperByUserName(String userName);
	
	/**
	 * 
	 * @param developer
	 * @return
	 */
	public boolean createDeveloper(Developer developer);
	
	/**
	 * 
	 * @param developer
	 * @return
	 */
	public boolean updateDeveloper(Developer developer);
	
	/**
	 * 
	 * @param developer
	 * @return
	 */
	public boolean deleteDeveloper(Developer developer);
	
}
