/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.LMISApplicationBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 * @category This class works with the LMIS_licenceView and LMIS_licenceOwnerView Data Objects.
 */
public interface RoadLicenceDAO extends DAO 
{
	/**
	 * 
	 * @param roadLicenceNo
	 * @return RoadLicenceBO if record is found null otherwise.
	 * @category This method takes an integer variable road licence and try to find related road licence as well as a list of licence owners.
	 */
	public RoadLicenceBO getRoadLicence(Integer roadLicenceNo);

	
	/**
	 * @author Rackenee Brooks
	 * Date: 12 December 2013
	 * @param plateRegNo
	 * @return RoadLicenceBO
	 * Searches for road license info by plate number
	 */
	public RoadLicenceBO getRoadLicence(String plateRegNo);
	
	RoadLicenceBO getRoadLicenceByPlateNo(String roadLicenceNo);


	List<VehicleOwnerBO> findVehicleOwnersByPlateForDocumentView(String plateNo);


	RoadLicenceBO getRoadLicenceByPlateNoForDocumentView(String licencePlate);


	List<VehicleOwnerBO> findVehicleOwnersByVehicleIDForDocumentView(Integer vehId);


	
}
