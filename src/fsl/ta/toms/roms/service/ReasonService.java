package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;

public interface ReasonService {

	public List<ReasonBO> lookupReason(ReasonCriteriaBO reasonCriteriaBO);
	
	public boolean saveReason(ReasonBO reasonBO);
	
	public boolean updateReason(ReasonBO reasonBO);
	
	public ReasonDO findReasonById(Integer reasonId);
	
	public boolean reasonDescExistForSelectedType(ReasonBO reasonBO);
}
