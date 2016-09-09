/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface CourtDAO extends DAO 
{
	public List<CourtDO> getCourtReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	List<CourtDO> lookupCourt(
			CourtCriteriaBO courtCriteriaBO);

	boolean courtExists(Integer courtId);
	
	boolean courtDescriptionExists(CourtBO court);

	boolean courtShortDescExists(CourtBO court);

}
