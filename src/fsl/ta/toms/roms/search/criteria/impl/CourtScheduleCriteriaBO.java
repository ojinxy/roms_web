/**
 * Created By: oanguin
 * Date: May 15, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.search.criteria.ReportSearchCriteria;


/**
 * @author oanguin
 * Created Date: May 15, 2013
 */
public class CourtScheduleCriteriaBO extends ParentReportCriteria implements
		Serializable, ReportSearchCriteria
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Date trialStartDate,
		 trialEndDate;
	
	String TAStaffId,
		   roadOperationName,
		   TAStaffTRN,
		   offenderTRN,
		   offenderFirstName,
		   offenderLastName;
	
	Integer courtId,
			offenderId,
			roadOperationId;
	
	List<String> TAOfficeRegions = new ArrayList<String>();
	
	private String TAOfficeDescription,TAStaffFullName,CourtDescription,OffenderFullName;
	
	
		  

	public String getRoadOperationName() {
		return roadOperationName;
	}


	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
	}


	public void setTAOfficeDescription(String tAOfficeDescription) {
		TAOfficeDescription = tAOfficeDescription;
	}


	public void setTAStaffFullName(String tAStaffFullName) {
		TAStaffFullName = tAStaffFullName;
	}


	public void setCourtDescription(String courtDescription) {
		CourtDescription = courtDescription;
	}


	public void setOffenderFullName(String offenderFullName) {
		OffenderFullName = offenderFullName;
	}


	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.search.criteria.ReportSearchCriteria#getSearchCriteriaString()
	 */
	@Override
	public String getSearchCriteriaString() 
	{
		StringBuilder criteriaString = new StringBuilder("");
		
		criteriaString.append(this.getCriterionString("TA-Office Region", this.TAOfficeDescription));
		criteriaString.append(this.getCriterionString("TA-Staff Name", this.TAStaffFullName));
		criteriaString.append(this.getCriterionString("Court Description", this.CourtDescription));
		criteriaString.append(this.getCriterionString("Offender Name", this.OffenderFullName));
		criteriaString.append(this.getCriterionString("Offender First Name", this.offenderFirstName));
		criteriaString.append(this.getCriterionString("Offender Last Name", this.offenderLastName));
		criteriaString.append(this.getCriterionString("Road Operation Id", this.roadOperationId));
		criteriaString.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		
		return criteriaString.toString();
	}


	public CourtScheduleCriteriaBO(Date trialStartDate, Date trialEndDate,
			List<String> tAOfficeRegion, String tAStaffId, Integer courtId,
			Integer offenderId, Integer roadOperationId) {
		super();
		this.trialStartDate = trialStartDate;
		this.trialEndDate = trialEndDate;
		this.TAOfficeRegions = tAOfficeRegion;
		TAStaffId = tAStaffId;
		this.courtId = courtId;
		this.offenderId = offenderId;
		this.roadOperationId = roadOperationId;
	}


	public CourtScheduleCriteriaBO() 
	{
		super();
		
	}


	public Date getTrialStartDate() {
		return trialStartDate;
	}


	public void setTrialStartDate(Date trialStartDate) {
		this.trialStartDate = trialStartDate;
	}


	public Date getTrialEndDate() {
		return trialEndDate;
	}


	public void setTrialEndDate(Date trialEndDate) {
		this.trialEndDate = trialEndDate;
	}




	public String getTAStaffId() {
		return TAStaffId;
	}


	public void setTAStaffId(String tAStaffId) {
		TAStaffId = tAStaffId;
	}


	public Integer getCourtId() {
		return courtId;
	}


	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}


	public Integer getOffenderId() {
		return offenderId;
	}


	public void setOffenderId(Integer offenderId) {
		this.offenderId = offenderId;
	}


	public Integer getRoadOperationId() {
		return roadOperationId;
	}


	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}


	public String getTAStaffTRN() {
		return TAStaffTRN;
	}


	public void setTAStaffTRN(String tAStaffTRN) {
		TAStaffTRN = tAStaffTRN;
	}


	public String getOffenderTRN() {
		return offenderTRN;
	}


	public void setOffenderTRN(String offenderTRN) {
		this.offenderTRN = offenderTRN;
	}


	public String getOffenderFirstName() {
		return offenderFirstName;
	}


	public void setOffenderFirstName(String offenderFirstName) {
		this.offenderFirstName = offenderFirstName;
	}


	public String getOffenderLastName() {
		return offenderLastName;
	}


	public void setOffenderLastName(String offenderLastName) {
		this.offenderLastName = offenderLastName;
	}


	public List<String> getTAOfficeRegions() {
		return TAOfficeRegions;
	}


	public void setTAOfficeRegions(List<String> tAOfficeRegions) {
		TAOfficeRegions = tAOfficeRegions;
	}

	
	
}
