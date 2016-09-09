/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.PersonTypeDAO;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class PersonTypeDAOImpl extends ParentDAOImpl implements
		PersonTypeDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.PersonTypeDAO#getPersonTypeReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDPersonTypeDO> getPersonTypeReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDPersonTypeDO>)super.getReference(CDPersonTypeDO.class, status, filter);
	}

}
