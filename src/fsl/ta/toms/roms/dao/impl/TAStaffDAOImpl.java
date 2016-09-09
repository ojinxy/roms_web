/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.TAStaffDAO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class TAStaffDAOImpl extends ParentDAOImpl implements
		TAStaffDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.TAStaff#getTAStaffReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TAStaffDO> getTAStaffReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<TAStaffDO>)super.getReference(TAStaffDO.class, status, filter);
	}

}
