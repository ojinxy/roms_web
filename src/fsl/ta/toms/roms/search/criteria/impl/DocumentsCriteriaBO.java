package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * 
 * @author jreid
 * Created Date: May 1, 2013
 */
public class DocumentsCriteriaBO implements SearchCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4375557309996769176L;
	
	String documentType;
	String documentStatus;
	//String documentOrigin;
	//boolean isManualDocument;
	
	//String serialNumberEntered;
	String manualSerialNo;
	Integer automaticSerialNo;	
	String referenceNo;
	
	String servedByStaffId;//autocomplete
	String servedByFirstName;
	String servedByLastName;	 
	
	Date servedStartDateRange;
	Date servedEndDateRange;	
	
	String staffId; //auto complete
	String staffFirstName;
	String staffLastName;
	//String staffTypeCode;
	
	String operationName;//auto complete
	Date operationStartDateRange;
	Date operationEndDateRange;	
		
	String trnNbr;
	String offenderFirstName;
	String offenderLastName;
	String offenderMiddleName;
	
	//offence
	Date offenceStartDateRange;
	Date offenceEndDateRange;
	
	Integer complianceId;
	
	/**
	 * 
	 * @return
	 */
	/*public Boolean isCriteriaEmpty(){
		boolean isEmpty = true;
		
		if(!StringUtils.isEmpty(documentType))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(documentStatus))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(serialNumberEntered))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(servedByStaffId))
			isEmpty = false;
		
		if(servedStartDateRange != null)
			isEmpty = false;
		
		if(servedEndDateRange != null)
			isEmpty = false;
		
		if(!StringUtils.isEmpty(staffId))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(operationName))
			isEmpty = false;
		
		if(operationStartDateRange != null)
			isEmpty = false;
		
		if(operationEndDateRange != null)
			isEmpty = false;
		
		if(!StringUtils.isEmpty(trnNbr))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(offenderFirstName))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(offenderLastName))
			isEmpty = false;
		
		if(!StringUtils.isEmpty(offenderMiddleName))
			isEmpty = false; 
		
		if(offenceStartDateRange != null)
			isEmpty = false;
		
		if(offenceEndDateRange != null)
			isEmpty = false;
		
		if(this.complianceId != null && this.complianceId > 0)
			isEmpty = false;
			
		return isEmpty;
	}
	*/
	
	
	
	
	/**
	 * @param serialNumberEntered the serialNumberEntered to set
	 * When this value is set it is checked and used to determine whether a 
	 * search should be done for an automatic serial number or manual
	 */
	/*public void setSerialNumberEntered(String serialNumberEntered) {
		if(StringUtils.isNotBlank(serialNumberEntered)){
			if(StringUtils.isNumeric(serialNumberEntered))
				this.automaticSerialNo = new Integer(serialNumberEntered);
			
			else
				this.manualSerialNo = serialNumberEntered;
		}
		
		this.serialNumberEntered = serialNumberEntered;
			
	}*/
	
	
	

	/**
	 * @return the referenceNo
	 */
	public String getReferenceNo() {
		return referenceNo;
	}




	/**
	 * @param referenceNo the referenceNo to set
	 */
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}




	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the documentStatus
	 */
	public String getDocumentStatus() {
		return documentStatus;
	}

	/**
	 * @param documentStatus the documentStatus to set
	 */
	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	/**
	 * @return the manualSerialNo
	 */
	public String getManualSerialNo() {
		return manualSerialNo;
	}

	/**
	 * @param manualSerialNo the manualSerialNo to set
	 */
	public void setManualSerialNo(String manualSerialNo) {
		this.manualSerialNo = manualSerialNo;
	}

	/**
	 * @return the automaticSerialNo
	 */
	public Integer getAutomaticSerialNo() {
		return automaticSerialNo;
	}

	/**
	 * @param automaticSerialNo the automaticSerialNo to set
	 */
	public void setAutomaticSerialNo(Integer automaticSerialNo) {
		this.automaticSerialNo = automaticSerialNo;
	}

	/**
	 * @return the servedByStaffId
	 */
	public String getServedByStaffId() {
		return servedByStaffId;
	}

	/**
	 * @param servedByStaffId the servedByStaffId to set
	 */
	public void setServedByStaffId(String servedByStaffId) {
		this.servedByStaffId = servedByStaffId;
	}

	/**
	 * @return the servedByFirstName
	 */
	public String getServedByFirstName() {
		return servedByFirstName;
	}

	/**
	 * @param servedByFirstName the servedByFirstName to set
	 */
	public void setServedByFirstName(String servedByFirstName) {
		this.servedByFirstName = servedByFirstName;
	}

	/**
	 * @return the servedByLastName
	 */
	public String getServedByLastName() {
		return servedByLastName;
	}

	/**
	 * @param servedByLastName the servedByLastName to set
	 */
	public void setServedByLastName(String servedByLastName) {
		this.servedByLastName = servedByLastName;
	}

	/**
	 * @return the servedStartDateRange
	 */
	public Date getServedStartDateRange() {
		return servedStartDateRange;
	}

	/**
	 * @param servedStartDateRange the servedStartDateRange to set
	 */
	public void setServedStartDateRange(Date servedStartDateRange) {
		this.servedStartDateRange = servedStartDateRange;
	}

	/**
	 * @return the servedEndDateRange
	 */
	public Date getServedEndDateRange() {
		return servedEndDateRange;
	}

	/**
	 * @param servedEndDateRange the servedEndDateRange to set
	 */
	public void setServedEndDateRange(Date servedEndDateRange) {
		this.servedEndDateRange = servedEndDateRange;
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
	 * @return the offenderMiddleName
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * @param offenderMiddleName the offenderMiddleName to set
	 */
	public void setOffenderMiddleName(String offenderMiddleName) {
		this.offenderMiddleName = offenderMiddleName;
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
	 * @return the complianceId
	 */
	public Integer getComplianceId() {
		return complianceId;
	}

	/**
	 * @param complianceId the complianceId to set
	 */
	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}

/*
	*//**
	 * @return the serialNumberEntered
	 *//*
	public String getSerialNumberEntered() {
		return serialNumberEntered;
	}
*/
	
	
	
}
