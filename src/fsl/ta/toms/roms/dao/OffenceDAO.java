/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceLawBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.OffenceLawDO;
import fsl.ta.toms.roms.dataobjects.OffenceParamDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface OffenceDAO extends DAO 
{
	public List<OffenceDO> getOffenceReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<OffenceDO> lookupOffence(OffenceCriteriaBO offenceCriteriaBO);
	
	public boolean offenceDescriptionExist(String description, Integer offenceId);
	
	public boolean offenceShortDescriptionExist(String shortDescription, Integer offenceId);
	
	public List<OffenceParamBO> findOffenceParams(Integer offenceId);
	
	public List<OffenceLawBO> findOffenceLaws(Integer offenceId);
	
	public OffenceParamDO findOffenceParamsById(Integer paramId, Integer offenceId);
	
	public OffenceLawDO findOffenceLawsById(Integer lawId, Integer offenceId);
}
