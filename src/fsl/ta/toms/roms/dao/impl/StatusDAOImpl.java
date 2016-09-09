/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.StatusDAO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class StatusDAOImpl extends ParentDAOImpl implements
		StatusDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.StatusDAO#getStatusReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StatusDO> getStatusReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException {
			
			List<StatusDO> statuses = (List<StatusDO>)super.getReference(StatusDO.class, status, filter);
	
			Collections.sort(statuses, new Comparator<StatusDO>() {
			    public int compare(StatusDO result1, StatusDO result2) {
			        return result1.getDescription().compareTo(result2.getDescription());
			    }
			});
	
			return statuses;
	}

}
