package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.ReasonTypeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;

public interface ReasonDAO extends DAO {

	public List<ReasonDO> lookupReason(ReasonCriteriaBO reasonCriteriaBO);
	
	public Integer saveReasonDO(ReasonDO reasonDO);
	
	public List<ReasonTypeDO> getReasonTypeReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public boolean reasonDescExistForSelectedType(ReasonBO reasonBO);

	List<ReasonDO> getReasonReference(HashMap<String, String> filter, String status) throws InvalidFieldException;
}
