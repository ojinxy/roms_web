/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface PoundDAO extends DAO 
{
	public List<PoundDO> getPoundReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	boolean poundExists(Integer poundId);

	List<PoundDO> lookupPounds(PoundCriteriaBO poundDOCriteriaBO);

	boolean poundNameExists(PoundBO pound);

	boolean poundShortDescriptionExists(PoundBO pound);
}
