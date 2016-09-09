package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public interface CourtService {
	public List<CourtBO> getCourtReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public boolean courtExists(Integer courtCode);
	
	List<CourtBO> lookupCourts(CourtCriteriaBO courtCriteriaBO);

	Integer saveCourt(CourtBO courtBO) throws ErrorSavingException;

	void updateCourt(CourtBO courtBO) throws ErrorSavingException;

	boolean shortDescriptionExists(CourtBO courtBO);

	boolean descriptionExists(CourtBO courtBO);

}
