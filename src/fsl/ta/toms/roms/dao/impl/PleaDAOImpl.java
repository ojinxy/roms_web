/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.PleaDAO;
import fsl.ta.toms.roms.dataobjects.CDPleaDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class PleaDAOImpl extends ParentDAOImpl implements
		PleaDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.PleaDAO#getPleaReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDPleaDO> getPleaReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDPleaDO>)super.getReference(CDPleaDO.class, status, filter);
	}

}
