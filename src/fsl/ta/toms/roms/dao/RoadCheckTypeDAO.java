/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface RoadCheckTypeDAO extends DAO 
{
	public List<CDRoadCheckTypeDO> getRoadCheckTypeReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
