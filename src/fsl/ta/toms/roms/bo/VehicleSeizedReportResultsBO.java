/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * This object has all the data needed for the vehicle seized report
 * @author oanguin
 * Created Date: May 8, 2013
 */
public class VehicleSeizedReportResultsBO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String offenderFullName,
		   offenceDescription,
		   offenceShortDescription,
		   TAStaffFullName,
		   poundName,
		   wreckingCompanyName,
		   vehicleDetails,
		   locationOfOffence,
		   TAOfficeRegion,
		   roadOperationDetails,
		   TAOfficeRegionDescription;
	
	List<String> witnessesFullName;
	
	Date dateOfOffence;
	
	public String getRoadOperationDetails() {
		return roadOperationDetails;
	}

	public void setRoadOperationDetails(String roadOperationDetails) {
		this.roadOperationDetails = roadOperationDetails;
	}

	


	public String getTAOfficeRegionDescription() {
		return TAOfficeRegionDescription;
	}

	public void setTAOfficeRegionDescription(String tAOfficeRegionDescription) {
		TAOfficeRegionDescription = tAOfficeRegionDescription;
	}






	public String getOffenderFullName() {
		return offenderFullName;
	}

	public void setOffenderFullName(String offenderFullName) {
		this.offenderFullName = offenderFullName;
	}

	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

	public String getOffenceShortDescription() {
		return offenceShortDescription;
	}

	public void setOffenceShortDescription(String offenceShortDescription) {
		this.offenceShortDescription = offenceShortDescription;
	}

	public String getTAStaffFullName() {
		return TAStaffFullName;
	}

	public void setTAStaffFullName(String tAStaffFullName) {
		TAStaffFullName = tAStaffFullName;
	}

	public List<String> getWitnessesFullName() {
		return witnessesFullName;
	}

	public void setWitnessesFullName(List<String> witnessesFullName) {
		this.witnessesFullName = witnessesFullName;
	}

	public String getPoundName() {
		return poundName;
	}

	public void setPoundName(String poundName) {
		this.poundName = poundName;
	}

	public String getWreckingCompanyName() {
		return wreckingCompanyName;
	}

	public void setWreckingCompanyName(String wreckingCompanyName) {
		this.wreckingCompanyName = wreckingCompanyName;
	}

	public String getVehicleDetails() {
		return vehicleDetails;
	}

	public void setVehicleDetails(String vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public String getLocationOfOffence() {
		return locationOfOffence;
	}

	public void setLocationOfOffence(String locationOfOffence) {
		this.locationOfOffence = locationOfOffence;
	}

	public String getTAOfficeRegion() {
		return TAOfficeRegion;
	}

	public void setTAOfficeRegion(String tAOfficeRegion) {
		TAOfficeRegion = tAOfficeRegion;
	}

	public Date getDateOfOffence() {
		return dateOfOffence;
	}

	public void setDateOfOffence(Date dateOfOffence) {
		this.dateOfOffence = dateOfOffence;
	}

	public VehicleSeizedReportResultsBO(String offenderFullName,
			String offenceDescription, String offenceShortDescription,
			String tAStaffFullName, String poundName,
			String wreckingCompanyName, String vehicleDetails,
			String locationOfOffence, String tAOfficeRegion,
			String roadOperationDetails, List<String> witnessesFullName,
			Date dateOfOffence,String TAOfficeRegionDescription) {
		super();
		this.offenderFullName = offenderFullName;
		this.offenceDescription = offenceDescription;
		this.offenceShortDescription = offenceShortDescription;
		TAStaffFullName = tAStaffFullName;
		this.poundName = poundName;
		this.wreckingCompanyName = wreckingCompanyName;
		this.vehicleDetails = vehicleDetails;
		this.locationOfOffence = locationOfOffence;
		TAOfficeRegion = tAOfficeRegion;
		this.roadOperationDetails = roadOperationDetails;
		this.witnessesFullName = witnessesFullName;
		this.dateOfOffence = dateOfOffence;
		this.TAOfficeRegionDescription =  TAOfficeRegionDescription;
	}

	public VehicleSeizedReportResultsBO(String offenderFullName,
			String offenceDescription, String offenceShortDescription,
			String tAStaffFullName, String poundName,
			String wreckingCompanyName, String vehicleDetails,
			String locationOfOffence, String tAOfficeRegion,
			String roadOperationDetails, List<String> witnessesFullName,
			Date dateOfOffence) {
		super();
		this.offenderFullName = offenderFullName;
		this.offenceDescription = offenceDescription;
		this.offenceShortDescription = offenceShortDescription;
		TAStaffFullName = tAStaffFullName;
		this.poundName = poundName;
		this.wreckingCompanyName = wreckingCompanyName;
		this.vehicleDetails = vehicleDetails;
		this.locationOfOffence = locationOfOffence;
		TAOfficeRegion = tAOfficeRegion;
		this.roadOperationDetails = roadOperationDetails;
		this.witnessesFullName = witnessesFullName;
		this.dateOfOffence = dateOfOffence;
	}

	public VehicleSeizedReportResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	

		   

}
