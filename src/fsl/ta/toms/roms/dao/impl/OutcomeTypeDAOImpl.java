/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.OutcomeTypeDAO;
import fsl.ta.toms.roms.dataobjects.CDOutcomeTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class OutcomeTypeDAOImpl extends ParentDAOImpl implements
		OutcomeTypeDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.OutcomeTypeDAO#getOutcomeTypeReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDOutcomeTypeDO> getOutcomeTypeReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDOutcomeTypeDO>)super.getReference(CDOutcomeTypeDO.class, status, filter);
	}

}
