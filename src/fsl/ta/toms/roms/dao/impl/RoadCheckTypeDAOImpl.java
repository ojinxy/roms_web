/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.dao.SpringHibernateDAOSupport;
import fsl.ta.toms.roms.dao.RoadCheckTypeDAO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class RoadCheckTypeDAOImpl extends ParentDAOImpl implements
		RoadCheckTypeDAO, SpringHibernateDAOSupport {


	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.RoadCheckTypeDAO#getRoadCheckTypeReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDRoadCheckTypeDO> getRoadCheckTypeReference(
			 HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDRoadCheckTypeDO>)super.getReference(CDRoadCheckTypeDO.class, status, filter);
	}

}
