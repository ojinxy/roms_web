/**
 * Created By: oanguin
 * Date: Jul 17, 2014
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/**
 * @author oanguin
 * Created Date: Jul 17, 2014
 */
public class RoadLicVehCheckResultBO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VehicleCheckResultBO vehicleCheckResult;
	
	RoadLicCheckResultBO roadLicCheckResult;
	
	

	public RoadLicVehCheckResultBO()
	{
		super();
		
	}

	public RoadLicVehCheckResultBO(VehicleCheckResultBO vehicleCheckResult,
			RoadLicCheckResultBO roadLicCheckResult)
	{
		super();
		this.vehicleCheckResult = vehicleCheckResult;
		this.roadLicCheckResult = roadLicCheckResult;
	}

	public VehicleCheckResultBO getVehicleCheckResult()
	{
		return vehicleCheckResult;
	}

	public void setVehicleCheckResult(VehicleCheckResultBO vehicleCheckResult)
	{
		this.vehicleCheckResult = vehicleCheckResult;
	}

	public RoadLicCheckResultBO getRoadLicCheckResult()
	{
		return roadLicCheckResult;
	}

	public void setRoadLicCheckResult(RoadLicCheckResultBO roadLicCheckResult)
	{
		this.roadLicCheckResult = roadLicCheckResult;
	}
	
	
	
}
