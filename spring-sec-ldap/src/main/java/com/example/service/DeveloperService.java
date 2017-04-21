/* Created Apr 20, 2017
 *
 * @(#)DeveloperService.java
 *
 * (C)2017 - Emmanuel Akolly
 */
package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.DeveloperDao;
import com.example.model.Developer;

/**
 * @author efoeakolly
 *
 */
@Service
public class DeveloperService implements IDeveloperService {
	
	@Autowired
	private DeveloperDao devDao;
	

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#getAllDevelopers()
	 */
	@Override
	public List<Developer> getAllDevelopers() {
		return devDao.getAllDevelopers();
	}

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#getDeveloperByLastName(java.lang.String)
	 */
	@Override
	public List<Developer> getDeveloperByLastName(String lastName) {
		return devDao.getDeveloperByLastName(lastName);
	}

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#getDeveloperByUserName(java.lang.String)
	 */
	@Override
	public Developer getDeveloperByUserName(String userName) {
		return devDao.getDeveloperByUserName(userName);
	}

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#createDeveloper(com.example.model.Developer)
	 */
	@Override
	public boolean createDeveloper(Developer developer) {
		return devDao.createDeveloper(developer);
	}

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#updateDeveloper(com.example.model.Developer)
	 */
	@Override
	public boolean updateDeveloper(Developer developer) {
		return devDao.updateDeveloper(developer);
	}

	/* (non-Javadoc)
	 * @see com.example.service.IDeveloperService#deleteDeveloper(com.example.model.Developer)
	 */
	@Override
	public boolean deleteDeveloper(Developer developer) {
		return devDao.deleteDeveloper(developer);
	}

}
