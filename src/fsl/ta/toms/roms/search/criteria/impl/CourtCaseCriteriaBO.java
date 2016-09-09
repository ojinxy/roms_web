/**
 * Created By: oanguin
 * Date: Jul 11, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

/**
 * @author oanguin
 * Created Date: Jul 11, 2013
 */
public class CourtCaseCriteriaBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String statusCode;
	
	Integer verdictCode;
	
	Integer courtId;
	Date courtDate; 
	String courtTime;
	
	String courtCaseNo;
	
	Integer summonsId;
	Integer warningNoticeId;
	
	Integer courtCaseId;
	
	Integer roadOperationId;
	
	String staffId;
	
	String officeLocCode;
	
	
	//offender
	String defendantFirstName;
	String defendantLastName;
	
	//ta officer
	String inspectorFirstName;
	String inspectorLastName;
	

	public CourtCaseCriteriaBO(String statusCode, Integer verdictCode,
			Integer courtId, String courtCaseNo, Integer summonsId,
			Integer courtCaseId,Integer roadOperationId,
			String staffId, String officeLocCode) {
		super();
		this.statusCode = statusCode;
		this.verdictCode = verdictCode;
		this.courtId = courtId;
		this.courtCaseNo = courtCaseNo;
		this.summonsId = summonsId;
		this.courtCaseId = courtCaseId;
		this.roadOperationId = roadOperationId;
		this.staffId = staffId;
		this.officeLocCode = officeLocCode;
	}

	public CourtCaseCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getVerdictCode() {
		return verdictCode;
	}

	public void setVerdictCode(Integer verdictCode) {
		this.verdictCode = verdictCode;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}

	public Integer getSummonsId() {
		return summonsId;
	}

	public void setSummonsId(Integer summonsId) {
		this.summonsId = summonsId;
	}

	public Integer getCourtCaseId() {
		return courtCaseId;
	}

	public void setCourtCaseId(Integer courtCaseId) {
		this.courtCaseId = courtCaseId;
	}


	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getOfficeLocCode() {
		return officeLocCode;
	}

	public void setOfficeLocCode(String officeLocCode) {
		this.officeLocCode = officeLocCode;
	}

	/**
	 * @return the courtDate
	 */
	public Date getCourtDate() {
		return courtDate;
	}

	/**
	 * @param courtDate the courtDate to set
	 */
	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	/**
	 * @return the courtTime
	 */
	public String getCourtTime() {
		return courtTime;
	}

	/**
	 * @param courtTime the courtTime to set
	 */
	public void setCourtTime(String courtTime) {
		this.courtTime = courtTime;
	}

	/**
	 * @return the warningNoticeId
	 */
	public Integer getWarningNoticeId() {
		return warningNoticeId;
	}

	/**
	 * @param warningNoticeId the warningNoticeId to set
	 */
	public void setWarningNoticeId(Integer warningNoticeId) {
		this.warningNoticeId = warningNoticeId;
	}

	/**
	 * @return the defendantFirstName
	 */
	public String getDefendantFirstName() {
		return defendantFirstName;
	}

	/**
	 * @param defendantFirstName the defendantFirstName to set
	 */
	public void setDefendantFirstName(String defendantFirstName) {
		this.defendantFirstName = defendantFirstName;
	}

	/**
	 * @return the defendantLastName
	 */
	public String getDefendantLastName() {
		return defendantLastName;
	}

	/**
	 * @param defendantLastName the defendantLastName to set
	 */
	public void setDefendantLastName(String defendantLastName) {
		this.defendantLastName = defendantLastName;
	}

	/**
	 * @return the inspectorFirstName
	 */
	public String getInspectorFirstName() {
		return inspectorFirstName;
	}

	/**
	 * @param inspectorFirstName the inspectorFirstName to set
	 */
	public void setInspectorFirstName(String inspectorFirstName) {
		this.inspectorFirstName = inspectorFirstName;
	}

	/**
	 * @return the inspectorLastName
	 */
	public String getInspectorLastName() {
		return inspectorLastName;
	}

	/**
	 * @param inspectorLastName the inspectorLastName to set
	 */
	public void setInspectorLastName(String inspectorLastName) {
		this.inspectorLastName = inspectorLastName;
	}
	
	
	
	
	

}
