/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDCourtRulingDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface CourtRulingDAO extends DAO 
{
	public List<CDCourtRulingDO> getCourtRulingReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

}
