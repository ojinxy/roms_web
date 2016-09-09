/**
 * Created By: oanguin
 * Date: May 15, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: May 15, 2013
 */
public class CourtCasesOpenedReportResultsBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String offenderFullName,
		   TAStaffFullName,
		   roadOperationName,
		   offenceDetails,
		   TAOfficeRegion,
		   TAOfficeRegionDescription,
		   courtDetails,
		   courtCaseStatus,
		   verdict;
	
	Date offenceDate,
		 courtDate;
	
	List<CourtAppearanceReportResultsBO> courtAppearanceList;

	public String getOffenderFullName() {
		return offenderFullName;
	}

	public void setOffenderFullName(String offenderFullName) {
		this.offenderFullName = offenderFullName;
	}

	public String getTAStaffFullName() {
		return TAStaffFullName;
	}

	public void setTAStaffFullName(String tAStaffFullName) {
		TAStaffFullName = tAStaffFullName;
	}

	public String getRoadOperationName() {
		return roadOperationName;
	}

	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
	}

	public String getOffenceDetails() {
		return offenceDetails;
	}

	public void setOffenceDetails(String offenceDetails) {
		this.offenceDetails = offenceDetails;
	}

	public String getTAOfficeRegion() {
		return TAOfficeRegion;
	}

	public void setTAOfficeRegion(String tAOfficeRegion) {
		TAOfficeRegion = tAOfficeRegion;
	}

	public String getTAOfficeRegionDescription() {
		return TAOfficeRegionDescription;
	}

	public void setTAOfficeRegionDescription(String tAOfficeRegionDescription) {
		TAOfficeRegionDescription = tAOfficeRegionDescription;
	}

	public String getCourtDetails() {
		return courtDetails;
	}

	public void setCourtDetails(String courtDetails) {
		this.courtDetails = courtDetails;
	}

	public String getCourtCaseStatus() {
		return courtCaseStatus;
	}

	public void setCourtCaseStatus(String courtCaseStatus) {
		this.courtCaseStatus = courtCaseStatus;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

	public Date getOffenceDate() {
		return offenceDate;
	}

	public void setOffenceDate(Date offenceDate) {
		this.offenceDate = offenceDate;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public List<CourtAppearanceReportResultsBO> getCourtAppearanceList() {
		return courtAppearanceList;
	}

	public void setCourtAppearanceList(
			List<CourtAppearanceReportResultsBO> courtAppearanceList) {
		this.courtAppearanceList = courtAppearanceList;
	}

	public CourtCasesOpenedReportResultsBO(String offenderFullName,
			String tAStaffFullName, String roadOperationName,
			String offenceDetails, String tAOfficeRegion,
			String tAOfficeRegionDescription, String courtDetails,
			String courtCaseStatus, String verdict, Date offenceDate,
			Date courtDate,
			List<CourtAppearanceReportResultsBO> courtAppearanceList) {
		super();
		this.offenderFullName = offenderFullName;
		TAStaffFullName = tAStaffFullName;
		this.roadOperationName = roadOperationName;
		this.offenceDetails = offenceDetails;
		TAOfficeRegion = tAOfficeRegion;
		TAOfficeRegionDescription = tAOfficeRegionDescription;
		this.courtDetails = courtDetails;
		this.courtCaseStatus = courtCaseStatus;
		this.verdict = verdict;
		this.offenceDate = offenceDate;
		this.courtDate = courtDate;
		this.courtAppearanceList = courtAppearanceList;
	}

	public CourtCasesOpenedReportResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

	

	
	
}
