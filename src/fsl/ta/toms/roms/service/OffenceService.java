package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceLawBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.OffenceLawDO;
import fsl.ta.toms.roms.dataobjects.OffenceParamDO;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;

public interface OffenceService {

	public List<OffenceBO> lookupOffence(OffenceCriteriaBO offenceCriteriaBO);
	
	public boolean saveOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList);
	
	public boolean updateOffence(OffenceBO offenceBO, List<OffenceParamBO> offenceParamList);
	
	public OffenceDO findOffenceById(Integer offenceId);
	
	public boolean offenceDescriptionExist(String description, Integer offenceId);
	
	public boolean offenceShortDescriptionExist(String shortDescription, Integer offenceId);
	
	public List<OffenceParamBO> findOffenceParams(Integer offenceId);
	
	public List<OffenceLawBO> findOffenceLaws(Integer offenceId);
}
