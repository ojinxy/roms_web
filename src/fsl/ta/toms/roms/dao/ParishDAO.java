/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface ParishDAO extends DAO 
{
	public List<ParishDO> getParishReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	List<ParishDO> lookupParish(ParishCriteriaBO parishCriteria);

	boolean parishExist(String parishCode);

	boolean parishDescriptionExist(ParishBO parish);
}
