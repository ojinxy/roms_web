/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.CourtRulingDAO;
import fsl.ta.toms.roms.dataobjects.CDCourtRulingDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class CourtRulingDAOImpl extends ParentDAOImpl implements
		 CourtRulingDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.CourtDAO#getCourtReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDCourtRulingDO> getCourtRulingReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDCourtRulingDO>)super.getReference(CDCourtRulingDO.class, status, filter);
	}


}
