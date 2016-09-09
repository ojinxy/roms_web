/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface TAVehicleDAO extends DAO 
{
	public List<TAVehicleDO> getTAVehicleReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<TAVehicleBO> lookupTAVehicle(TAVehicleCriteriaBO taVehicleCriteriaBO);
	
	public TAVehicleDO findTAVehicleByVehicleId(Integer vehicleId);
	
	public boolean taVehiclePlateRegNoExists(String plateRegNo);
	
	public boolean activeTAVehicleWithPlateRegNoExists(String plateRegNo);
	
	public Integer saveTAVehicleDO(TAVehicleDO taVehicleDO);
}
