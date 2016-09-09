/**
 * Created By: oanguin
 * Date: Apr 24, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDVerdictDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 24, 2013
 */
public interface VerdictDAO extends DAO {
	public List<CDVerdictDO> getVerdictReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
