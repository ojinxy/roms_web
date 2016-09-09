package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;

public interface ITAExaminerService {
	
	public List<ITAExaminerBO> lookupITAExaminer(ITAExaminerCriteriaBO itaExaminerCriteriaBO);
	
	public PersonBO savePerson(PersonBO personBO) throws Exception;
	
	public boolean saveITAExaminer(ITAExaminerBO itaExaminerBO);
	
	public boolean updateITAExaminer(ITAExaminerBO itaExaminerBO);
	
	public boolean itaExaminerExistWithTRN(String trnNbr);
	
	public ITAExaminerDO findITAExaminerByIdNumber(Integer idNumber);
	
	public ITAExaminerDO findITAExaminerByExaminerId(String idNumber);
}
