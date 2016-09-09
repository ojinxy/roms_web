package fsl.ta.toms.roms.dao;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.dataobjects.PersonDO;

/**
 * 
 * @author jreid
 * Created Date: Feb 27, 2014
 */
public interface PersonDAO extends DAO{

		
	public boolean personExistsById(Integer personId);	
	
	public boolean personExistsByTRN(String trn);

	public Integer savePersonDO(PersonDO personDO);
	
	public Integer savePerson(PersonBO personBO);
	
	public PersonDO findPersonByTRN(String trn);
	
	public PersonDO findPersonById(Integer id);
	
	public PersonDO findPersonByDriversLicence(String dlNo);
	
}
