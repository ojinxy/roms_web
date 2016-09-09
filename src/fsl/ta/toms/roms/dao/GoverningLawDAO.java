/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.dataobjects.GoverningLawDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface GoverningLawDAO extends DAO 
{
	public List<GoverningLawDO> getGoverningLawReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	List<GoverningLawDO> lookupGoverningLaw(
			GoverningLawCriteriaBO governingLawCriteriaBO);

	boolean governingLawExists(Integer governingLawId);

	boolean governingLawDescriptionExists(GoverningLawBO govLaw);

	boolean governingLawShortDescriptionExists(GoverningLawBO govLaw);
}
