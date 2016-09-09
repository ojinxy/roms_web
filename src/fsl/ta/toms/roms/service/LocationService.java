package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;

/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public interface LocationService {
	public List<LocationBO> getLocationReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	public List<LocationBO> lookupLocations(LocationCriteriaBO locationCriteriaBO);

	boolean locationExists(Integer locationId);

	Integer saveLocation(LocationBO locationBO) throws ErrorSavingException;

	void updateLocation(LocationBO locationBO) throws ErrorSavingException;

	boolean shortDescriptionExists(LocationBO locationBO);

	boolean descriptionExists(LocationBO locationBO);

}
