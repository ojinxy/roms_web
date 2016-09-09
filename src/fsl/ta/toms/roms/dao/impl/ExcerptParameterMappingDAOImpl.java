/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.ExcerptParameterMappingDAO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class ExcerptParameterMappingDAOImpl extends ParentDAOImpl implements
		ExcerptParameterMappingDAO {


	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.ExcerptParameterMappingDAO#getExcerptParameterMappingReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ExcerptParamMappingDO> getExcerptParameterMappingReference(
			 HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<ExcerptParamMappingDO>)super.getReference(ExcerptParamMappingDO.class, status, filter);
	}

}
