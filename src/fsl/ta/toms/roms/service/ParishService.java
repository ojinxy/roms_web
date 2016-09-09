package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;

/**
 * 
 * @author jreid
 * Created Date: May 21, 2013
 */
public interface ParishService {

	public List<ParishBO> getParishReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public boolean parishExists(String parishCode);

	public List<ParishBO> lookupParishes(ParishCriteriaBO parishCriteriaBO);

	String saveParish(ParishBO parishBO) throws ErrorSavingException;

	void updateParish(ParishBO parishBO) throws ErrorSavingException;

	boolean descriptionExists(ParishBO parishBO);

}
