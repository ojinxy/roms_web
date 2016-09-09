/**
 * Created By: oanguin
 * Date: May 15, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author oanguin
 * Created Date: May 15, 2013
 */
public class CourtScheduleReportResultsBO implements Serializable 
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
		   courtCaseStatus,
		   courtAppearanceStatus;
	
	Date offenceDate,
		 courtDate;
	
	CourtBO courtBO;

	
	public String getCourtAppearanceStatus() {
		return courtAppearanceStatus;
	}

	public void setCourtAppearanceStatus(String courtAppearanceStatus) {
		this.courtAppearanceStatus = courtAppearanceStatus;
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


	public String getCourtCaseStatus() {
		return courtCaseStatus;
	}

	public void setCourtCaseStatus(String courtCaseStatus) {
		this.courtCaseStatus = courtCaseStatus;
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
	
	

	public CourtBO getCourtBO() {
		return courtBO;
	}

	public void setCourtBO(CourtBO courtBO) {
		this.courtBO = courtBO;
	}

	public CourtScheduleReportResultsBO(String offenderFullName,
			String tAStaffFullName, String roadOperationName,
			String offenceDetails, String tAOfficeRegion,
			String tAOfficeRegionDescription,
			String courtCaseStatus, String courtAppearanceStatus,Date offenceDate, Date courtDate,CourtBO courtBO) {
		super();
		this.offenderFullName = offenderFullName;
		TAStaffFullName = tAStaffFullName;
		this.roadOperationName = roadOperationName;
		this.offenceDetails = offenceDetails;
		TAOfficeRegion = tAOfficeRegion;
		TAOfficeRegionDescription = tAOfficeRegionDescription;
		this.courtBO = courtBO;
		this.courtCaseStatus = courtCaseStatus;
		this.courtAppearanceStatus = courtAppearanceStatus;
		this.offenceDate = offenceDate;
		this.courtDate = courtDate;
	}

	public CourtScheduleReportResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
