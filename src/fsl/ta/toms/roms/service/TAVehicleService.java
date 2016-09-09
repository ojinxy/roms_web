package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;

public interface TAVehicleService {

	public List<TAVehicleBO> lookupTAVehicle(TAVehicleCriteriaBO taVehicleCriteriaBO);
	
	public boolean saveTAVehicle(TAVehicleBO taVehicleBO);
	
	public boolean updateTAVehicle(TAVehicleBO taVehicleBO);
	
	public boolean taVehicleExistWithVehicleId(VehicleBO vehicleBO);
	
	public TAVehicleDO findTAVehicleById(Integer taVehicleId);
	
	public boolean taVehiclePlateRegNoExists(String plateRegNo);
	
	public boolean activeTAVehicleWithPlateRegNoExists(String plateRegNo);
	
}
