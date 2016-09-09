package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * 
 * @author oanguin
 * Created Date: August 7, 2013
 */
public class WarningNoProsecutionCriteriaBO implements SearchCriteria 
{

	
	private static final long serialVersionUID = 1L;

	
	private Integer warningNoProsecutionId;
	Date warningNoProsecutionStartDateRange;
	Date warningNoProsecutionEndDateRange;
	
	Integer roadOperationId;
	String operationName;
	Date operationStartDateRange;
	Date operationEndDateRange;
	String operationCategory;
	String operationOffice;
	
	String trnNbr;
	String offenderFirstName;
	String offenderLastName;
	
	Integer offenceId;
	String offenceDescription;
	Date offenceStartDateRange;
	Date offenceEndDateRange;
	
	String staffId;
	String staffFirstName;
	String staffLastName;
	String staffTypeCode;
	
	
	

	public Integer getWarningNoProsecutionId() {
		return warningNoProsecutionId;
	}
	public void setWarningNoProsecutionId(Integer warningNoProsecutionId) {
		this.warningNoProsecutionId = warningNoProsecutionId;
	}

	
	
	public Date getWarningNoProsecutionStartDateRange() {
		return warningNoProsecutionStartDateRange;
	}
	public void setWarningNoProsecutionStartDateRange(
			Date warningNoProsecutionStartDateRange) {
		this.warningNoProsecutionStartDateRange = warningNoProsecutionStartDateRange;
	}
	public Date getWarningNoProsecutionEndDateRange() {
		return warningNoProsecutionEndDateRange;
	}
	public void setWarningNoProsecutionEndDateRange(
			Date warningNoProsecutionEndDateRange) {
		this.warningNoProsecutionEndDateRange = warningNoProsecutionEndDateRange;
	}
	/**
	 * @return the roadOperationId
	 */
	public Integer getRoadOperationId() {
		return roadOperationId;
	}
	/**
	 * @param roadOperationId the roadOperationId to set
	 */
	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}
	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}
	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	/**
	 * @return the operationStartDateRange
	 */
	public Date getOperationStartDateRange() {
		return operationStartDateRange;
	}
	/**
	 * @param operationStartDateRange the operationStartDateRange to set
	 */
	public void setOperationStartDateRange(Date operationStartDateRange) {
		this.operationStartDateRange = operationStartDateRange;
	}
	/**
	 * @return the operationEndDateRange
	 */
	public Date getOperationEndDateRange() {
		return operationEndDateRange;
	}
	/**
	 * @param operationEndDateRange the operationEndDateRange to set
	 */
	public void setOperationEndDateRange(Date operationEndDateRange) {
		this.operationEndDateRange = operationEndDateRange;
	}
	/**
	 * @return the operationCategory
	 */
	public String getOperationCategory() {
		return operationCategory;
	}
	/**
	 * @param operationCategory the operationCategory to set
	 */
	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	/**
	 * @return the operationOffice
	 */
	public String getOperationOffice() {
		return operationOffice;
	}
	/**
	 * @param operationOffice the operationOffice to set
	 */
	public void setOperationOffice(String operationOffice) {
		this.operationOffice = operationOffice;
	}
	/**
	 * @return the trnNbr
	 */
	public String getTrnNbr() {
		return trnNbr;
	}
	/**
	 * @param trnNbr the trnNbr to set
	 */
	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}
	/**
	 * @return the offenderFirstName
	 */
	public String getOffenderFirstName() {
		return offenderFirstName;
	}
	/**
	 * @param offenderFirstName the offenderFirstName to set
	 */
	public void setOffenderFirstName(String offenderFirstName) {
		this.offenderFirstName = offenderFirstName;
	}
	/**
	 * @return the offenderLastName
	 */
	public String getOffenderLastName() {
		return offenderLastName;
	}
	/**
	 * @param offenderLastName the offenderLastName to set
	 */
	public void setOffenderLastName(String offenderLastName) {
		this.offenderLastName = offenderLastName;
	}
	/**
	 * @return the offenceId
	 */
	public Integer getOffenceId() {
		return offenceId;
	}
	/**
	 * @param offenceId the offenceId to set
	 */
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	/**
	 * @return the offenceDescription
	 */
	public String getOffenceDescription() {
		return offenceDescription;
	}
	/**
	 * @param offenceDescription the offenceDescription to set
	 */
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}
	/**
	 * @return the offenceStartDateRange
	 */
	public Date getOffenceStartDateRange() {
		return offenceStartDateRange;
	}
	/**
	 * @param offenceStartDateRange the offenceStartDateRange to set
	 */
	public void setOffenceStartDateRange(Date offenceStartDateRange) {
		this.offenceStartDateRange = offenceStartDateRange;
	}
	/**
	 * @return the offenceEndDateRange
	 */
	public Date getOffenceEndDateRange() {
		return offenceEndDateRange;
	}
	/**
	 * @param offenceEndDateRange the offenceEndDateRange to set
	 */
	public void setOffenceEndDateRange(Date offenceEndDateRange) {
		this.offenceEndDateRange = offenceEndDateRange;
	}
	/**
	 * @return the staffId
	 */
	public String getStaffId() {
		return staffId;
	}
	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	/**
	 * @return the staffFirstName
	 */
	public String getStaffFirstName() {
		return staffFirstName;
	}
	/**
	 * @param staffFirstName the staffFirstName to set
	 */
	public void setStaffFirstName(String staffFirstName) {
		this.staffFirstName = staffFirstName;
	}
	/**
	 * @return the staffLastName
	 */
	public String getStaffLastName() {
		return staffLastName;
	}
	/**
	 * @param staffLastName the staffLastName to set
	 */
	public void setStaffLastName(String staffLastName) {
		this.staffLastName = staffLastName;
	}
	/**
	 * @return the staffTypeCode
	 */
	public String getStaffTypeCode() {
		return staffTypeCode;
	}
	/**
	 * @param staffTypeCode the staffTypeCode to set
	 */
	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}

	

}
