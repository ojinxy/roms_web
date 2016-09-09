package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.dataobjects.LocationDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;

public interface LocationDAO extends DAO {

	public List<LocationDO> getLocationReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	boolean locationExists(Integer locationId);

	List<LocationDO> lookupLocations(LocationCriteriaBO locationCriteriaBO);

	boolean locationShortDescriptionExists(LocationBO location);

	boolean locationDescriptionExists(LocationBO location);
}
