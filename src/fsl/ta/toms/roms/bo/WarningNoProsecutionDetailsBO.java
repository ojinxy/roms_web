/**
 * Created By: oanguin
 * Date: Aug 8, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: Aug 8, 2013
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class WarningNoProsecutionDetailsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String TAStaffFullName;
	String TAStaffID;
	String TATeamLeadFullName;
	
	String offenderFullName;
	String offendersDriverLicence;
	String offenderVehicleDetials;
	String vehicleOwnerFullNames;
	
	String roadOperationName;
	String locationOfOffence;
	
	Date dateOfOffence;
	
	String manualSerialNumber;
	String status;
	
	String offenceDescription;
	String offenceShortDescription;
	String allegation;

	
	
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

	public String getTAStaffID() {
		return TAStaffID;
	}

	public void setTAStaffID(String tAStaffID) {
		TAStaffID = tAStaffID;
	}

	public String getTATeamLeadFullName() {
		return TATeamLeadFullName;
	}

	public void setTATeamLeadFullName(String tATeamLeadFullName) {
		TATeamLeadFullName = tATeamLeadFullName;
	}

	public String getOffenderFullName() {
		return offenderFullName;
	}

	public void setOffenderFullName(String offenderFullName) {
		this.offenderFullName = offenderFullName;
	}

	public String getOffendersDriverLicence() {
		return offendersDriverLicence;
	}

	public void setOffendersDriverLicence(String offendersDriverLicence) {
		this.offendersDriverLicence = offendersDriverLicence;
	}

	public String getOffenderVehicleDetials() {
		return offenderVehicleDetials;
	}

	public void setOffenderVehicleDetials(String offenderVehicleDetials) {
		this.offenderVehicleDetials = offenderVehicleDetials;
	}

	public String getVehicleOwnerFullNames() {
		return vehicleOwnerFullNames;
	}

	public void setVehicleOwnerFullNames(String vehicleOwnerFullNames) {
		this.vehicleOwnerFullNames = vehicleOwnerFullNames;
	}

	public String getRoadOperationName() {
		return roadOperationName;
	}

	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
	}

	public String getLocationOfOffence() {
		return locationOfOffence;
	}

	public void setLocationOfOffence(String locationOfOffence) {
		this.locationOfOffence = locationOfOffence;
	}

	public Date getDateOfOffence() {
		return dateOfOffence;
	}

	public void setDateOfOffence(Date dateOfOffence) {
		this.dateOfOffence = dateOfOffence;
	}

	public String getManualSerialNumber() {
		return manualSerialNumber;
	}

	public void setManualSerialNumber(String manualSerialNumber) {
		this.manualSerialNumber = manualSerialNumber;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

	public String getAllegation() {
		return allegation;
	}

	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}

	public WarningNoProsecutionDetailsBO() {
		super();
		
	}

	public WarningNoProsecutionDetailsBO(String tAStaffFullName,
			String tAStaffID, String tATeamLeadFullName,
			String offenderFullName, String offendersDriverLicence,
			String offenderVehicleDetials,String vehicleOwnerFullNames,
			String roadOperationName, String locationOfOffence,
			Date dateOfOffence, String manualSerialNumber, String status,
			String offenceDescription, String allegation, String offenceShortDescription) {
		super();
		TAStaffFullName = tAStaffFullName;
		TAStaffID = tAStaffID;
		TATeamLeadFullName = tATeamLeadFullName;
		this.offenderFullName = offenderFullName;
		this.offendersDriverLicence = offendersDriverLicence;
		this.offenderVehicleDetials = offenderVehicleDetials;
		this.vehicleOwnerFullNames = vehicleOwnerFullNames;
		this.roadOperationName = roadOperationName;
		this.locationOfOffence = locationOfOffence;
		this.dateOfOffence = dateOfOffence;
		this.manualSerialNumber = manualSerialNumber;
		this.status = status;
		this.offenceDescription = offenceDescription;
		this.allegation = allegation;
		this.offenceShortDescription = offenceShortDescription;
	}
	
	
	
	
	
	
}
