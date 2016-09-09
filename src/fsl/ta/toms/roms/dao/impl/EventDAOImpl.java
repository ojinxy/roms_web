/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.EventDAO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public class EventDAOImpl extends ParentDAOImpl implements
		EventDAO {

	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.EventDAO#getEventReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDEventDO> getEventReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDEventDO>)super.getReference(CDEventDO.class, status, filter);
	}

	
	
	public List<EventAuditDO> findEventByCriteria(EventAuditCriteriaBO criteria)
	{
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" from EventAuditDO e where 1=1 ");
			
			//Event Code
			if ((criteria.getEventCode()) != null) 
			{queryString.append(" and upper(e.event.eventCode) = ").append(criteria.getEventCode());}
			
			
			//Others may be included
			//
			
			return hibernateTemplate.find(queryString.toString());
		
	}
	
	
	


}
