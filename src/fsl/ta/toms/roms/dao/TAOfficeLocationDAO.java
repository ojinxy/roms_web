/**
 * Created By: oanguin
 * Date: Apr 24, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;


/**
 * @author oanguin
 * Created Date: Apr 24, 2013
 */
public interface TAOfficeLocationDAO extends DAO 
{
	public List<LMIS_TAOfficeLocationViewDO> getTAOfficeLocationReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
