/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDOutcomeTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface OutcomeTypeDAO extends DAO 
{
	public List<CDOutcomeTypeDO> getOutcomeTypeReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
