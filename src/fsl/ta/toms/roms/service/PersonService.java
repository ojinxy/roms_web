package fsl.ta.toms.roms.service;

import fsl.ta.toms.roms.bo.PersonBO;

/**
 * 
 * @author jreid
 * Created Date: Feb 27, 2014
 */
public interface PersonService {
	
	public PersonBO findPersonByTRN(String trn);
	
	public PersonBO findPersonById(Integer id);
	

}
