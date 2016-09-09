/**
 * Created By: oanguin
 * Date: May 13, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * This object contains all the information needed for the Outstanding summons report.
 * @author oanguin
 * Created Date: May 13, 2013
 */
public class SummonsOutstandingReportResultsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String offenderFullName,
		   offenceDescription,
		   TAStaffFullName,
		   vehicleDetails,
		   locationOfOffence,
		   TAOfficeRegion,
		   JPFullName,
		   roadOperationDetails,
		   courtDetails,
		   TAOfficeRegionDescription;
	
	Date dateOfOffence;

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

	public String getTAStaffFullName() {
		return TAStaffFullName;
	}

	public void setTAStaffFullName(String tAStaffFullName) {
		TAStaffFullName = tAStaffFullName;
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

	public String getJPFullName() {
		return JPFullName;
	}

	public void setJPFullName(String jPFullName) {
		JPFullName = jPFullName;
	}


	public String getRoadOperationDetails() {
		return roadOperationDetails;
	}

	public void setRoadOperationDetails(String roadOperationDetails) {
		this.roadOperationDetails = roadOperationDetails;
	}

	public String getCourtDetails() {
		return courtDetails;
	}

	public void setCourtDetails(String courtDetails) {
		this.courtDetails = courtDetails;
	}

	public Date getDateOfOffence() {
		return dateOfOffence;
	}

	public void setDateOfOffence(Date dateOfOffence) {
		this.dateOfOffence = dateOfOffence;
	}


	


	public SummonsOutstandingReportResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getTAOfficeRegionDescription() {
		return TAOfficeRegionDescription;
	}

	public void setTAOfficeRegionDescription(String tAOfficeRegionDescription) {
		TAOfficeRegionDescription = tAOfficeRegionDescription;
	}

	public SummonsOutstandingReportResultsBO(String offenderFullName,
			String offenceDescription, String tAStaffFullName,
			String vehicleDetails, String locationOfOffence,
			String tAOfficeRegion, String jPFullName,
			String roadOperationDetails, String courtDetails,
			String tAOfficeRegionDescription, Date dateOfOffence) {
		super();
		this.offenderFullName = offenderFullName;
		this.offenceDescription = offenceDescription;
		TAStaffFullName = tAStaffFullName;
		this.vehicleDetails = vehicleDetails;
		this.locationOfOffence = locationOfOffence;
		TAOfficeRegion = tAOfficeRegion;
		JPFullName = jPFullName;
		this.roadOperationDetails = roadOperationDetails;
		this.courtDetails = courtDetails;
		TAOfficeRegionDescription = tAOfficeRegionDescription;
		this.dateOfOffence = dateOfOffence;
	}
	
	
	
	
}
