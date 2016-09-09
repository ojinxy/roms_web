package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;

public interface GoverningLawService {
	public List<GoverningLawBO> getGoverningLawReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public List<GoverningLawBO> lookupGoverningLaws(GoverningLawCriteriaBO governingLawCriteriaBO);

	boolean governingLawExists(Integer governingLawCode);

	Integer saveGoverningLaw(GoverningLawBO governingLawBO) throws ErrorSavingException;

	void updateGoverningLaw(GoverningLawBO governingLawBO) throws ErrorSavingException;

	boolean shortDescriptionExists(GoverningLawBO governingLawBO);

	boolean descriptionExists(GoverningLawBO governingLawBO);


}
