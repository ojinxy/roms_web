/**
 * Created By: oanguin
 * Date: Apr 24, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.TAOfficeLocationDAO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 24, 2013
 */
public class TAOfficeLocationDAOImpl extends ParentDAOImpl implements
		TAOfficeLocationDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.TAOfficeLocation#getTAOfficeLocationReference(java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LMIS_TAOfficeLocationViewDO> getTAOfficeLocationReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<LMIS_TAOfficeLocationViewDO>)super.getReference(LMIS_TAOfficeLocationViewDO.class, status, filter);
	}

}
