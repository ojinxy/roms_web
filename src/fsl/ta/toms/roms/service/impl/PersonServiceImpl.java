package fsl.ta.toms.roms.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.service.PersonService;

/**
 * 
 * @author jreid
 * Created Date: Feb 27, 2014
 */
public class PersonServiceImpl implements PersonService  {

	@Autowired
	DAOFactory daoFactory;
	
	
	@Override
	@Transactional(readOnly = true)
	public PersonBO findPersonByTRN(String trn) {
		PersonBO person = null;
		
		if(StringUtils.isBlank(trn)){
			return null;
		}
		
		PersonDO personDO = daoFactory.getPersonDAO().findPersonByTRN(trn);
		if(personDO != null){
			person = new PersonBO(personDO);
		}
		
		return person;		
	}

	@Override
	@Transactional(readOnly = true)
	public PersonBO findPersonById(Integer id) {
		PersonBO person = null;
		
		if(id == null){
			return null;
		}
		
		PersonDO personDO = daoFactory.getPersonDAO().findPersonById(id);
		if(personDO != null){
			person = new PersonBO(personDO);
		}
		
		return person;	
	}

}
