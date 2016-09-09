/**
 * Created By: oanguin
 * Date: May 17, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.amvswebservice.ArrayOfVehOwner;
import fsl.ta.toms.roms.amvswebservice.FslMotorVehicle;
import fsl.ta.toms.roms.amvswebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.amvswebservice.VehInfo;
import fsl.ta.toms.roms.amvswebservice.Vehicle;
import fsl.ta.toms.roms.bo.VehicleBO;

/**
 * @author oanguin
 * Created Date: May 17, 2013
 */
public class AMVSWebService extends SpringBeanAutowiringSupport implements Serializable 
{	

	/**
	 * 
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	FslMotorVehicle motorVehicleWS;
	
	public VehicleBO getVehicleBOByPlateNumber(String plateNumber) throws FslWebServiceException_Exception
	{
		Vehicle vehicle = motorVehicleWS.getVehicleDetails(plateNumber, "", "");
		
		VehInfo vInfo = vehicle.getVehInfo();
		
		ArrayOfVehOwner owners = vehicle.getVehOwners();
		
		VehicleBO vehicleBO = new VehicleBO(null/*vehicleId*/, 
										    vInfo.getPlateRegistrationNo()/*plateRegNo*/, 
										    vInfo.getEngineNo()/*engineNo*/, 
										    vInfo.getChassisNo()/*chassisNo*/, 
										    vInfo.getVehicleColour()/*colour*/, 
										    vInfo.getVehicleMakeDesc()/*makeDescription*/, 
										    vInfo.getVehicleModel()/*model*/, 
										    vInfo.getVehicleTypeDesc()/*typeDesc*/, 
										    (owners != null && ! owners.getVehOwner().isEmpty() && owners.getVehOwner().get(0) != null) 
										    	?owners.getVehOwner().get(0).getFirstName() + " " +  owners.getVehOwner().get(0).getLastName()
										    	:null /*ownerName*/,
										    	 vInfo.getVehicleYear(),	
										    ""/*currentUser*/);
		
		return vehicleBO;
	}
	
	
	public Vehicle getVehicleByPlateNumber(String plateNumber) throws FslWebServiceException_Exception
	{
		return motorVehicleWS.getVehicleDetails(plateNumber, "", "");
		

	}
	
}
