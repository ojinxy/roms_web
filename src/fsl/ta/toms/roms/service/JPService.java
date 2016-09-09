package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;

public interface JPService {

	public List<JPBO> lookupJP(JPCriteriaBO jpCriteriaBO);
	
	public boolean jpExistWithTRN(String trnNbr);
	
	public boolean saveJP(JPBO jpBO);
	
	public boolean updateJP(JPBO jpBO);
	
	public JPDO findJPByRegNumber(String regNumber);
	
	public boolean resetPin(JPBO jpBO);

	List<JPBO> getJPListByRegion(String region);

	JPDO findJPById(Integer id);

	
}
