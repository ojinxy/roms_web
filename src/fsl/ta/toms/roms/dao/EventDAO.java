/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface EventDAO extends DAO {
	public List<CDEventDO> getEventReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<EventAuditDO> findEventByCriteria(EventAuditCriteriaBO criteria);
	
}
