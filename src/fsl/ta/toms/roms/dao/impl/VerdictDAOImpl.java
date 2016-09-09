/**
 * Created By: oanguin
 * Date: Apr 24, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.VerdictDAO;
import fsl.ta.toms.roms.dataobjects.CDVerdictDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 24, 2013
 */
public class VerdictDAOImpl extends ParentDAOImpl implements
		VerdictDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.VerdictDAO#getArteryReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDVerdictDO> getVerdictReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDVerdictDO>)super.getReference(CDVerdictDO.class, status, filter);
	}

}
