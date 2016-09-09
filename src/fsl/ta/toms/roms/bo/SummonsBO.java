package fsl.ta.toms.roms.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.SummonsDO;

/**
 * 
 * @author jreid Created Date: Sep 11, 2013
 */
public class SummonsBO extends DocumentViewBO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -720152803031404569L;

	public SummonsBO() {
		super();
				
	}

	// offence
	String offenceExcerpt;

	// court
	String caseStatus;
	
	// court case
	CourtCaseBO courtCase;
	Integer overrideReason; 
	//String courtDateOverrideComment;

	// validation helpers
	boolean isManualSummons;
	boolean isSummonsVerified;	
	boolean isJPPinAuthorised;
	
	//overides
	boolean isOverrideRequested;
	
	//authorisation
	boolean isJPAuthorisationRequested;
	
	//print discharge form
	boolean isPrintDischargeFormRequested;
	
	//withdrawal details
	PersonBO dischargeWitness;
	String withdrawReason;
	
	//served	
	//String servedByUsername;	
	String servedAtParish;
	String servedAtAddress;
	AddressBO servedAtAddressBO;
	
	String excerpt;
	
	//stores parameters required for excerpt
	HashMap<String,String> excerptParams;
	
	/**
	 * Create a new Instance of Summons View object from query
	 * 
	 * @param roadOperationId
	 * @param roadCheckOffenceOutcomeCode
	 * @param offenderId
	 * @param taStaffId
	 * @param jpRegNumber
	 * @param manualSerialNumber
	 * @param isManualSummons
	 * @param offenceDtime
	 * @param offenceCode
	 * @param offenceLocation
	 * @param offenceExcerpt
	 * @param courtCode
	 * @param courtName
	 * @param courtDtime
	 * @param caseStatus
	 * @param statusId
	 * @param reasonCode
	 * @param comment
	 * @param allowUpload
	 * @param printUsername
	 * @param printDtime
	 * @param reprintUsername
	 * @param reprintDtime
	 * @param authorizedFlag
	 * @param authorizeUsername
	 * @param authorizeDtime
	 * @param issueUsername
	 * @param issueDate
	 * @param scannedDocList
	 * @param currentUsername
	 */
	public SummonsBO(Integer roadOperationId,
			Integer roadCheckOffenceOutcomeCode, Integer offenderId,
			String taStaffId, Integer jpIdNumber, String manualSerialNumber,
			Boolean isManualSummons, Date offenceDtime, Integer offenceCode,
			String offenceLocation, String offenceExcerpt, Integer courtCode,
			String courtName, Date courtDtime, String caseStatus,
			String statusId, Integer reasonCode, String comment,
			Character allowUpload, String printUsername, Date printDtime,
			String reprintUsername, Date reprintDtime,
			Character authorizedFlag, String authorizeUsername,
			Date authorizeDtime, String issueUsername, Date issueDate,
			List<ScannedDocBO> scannedDocList, String currentUsername,HashMap<String,String> excerptParams) {
		super();
		this.roadOperationId = roadOperationId;
		this.roadCheckOffenceOutcomeCode = roadCheckOffenceOutcomeCode;
		this.offenderId = offenderId;
		this.taStaffId = taStaffId;
		this.jpIdNumber = jpIdNumber;
		this.manualSerialNo = manualSerialNumber;
		this.isManualSummons = isManualSummons;
		this.offenceDtime = offenceDtime;
		this.offenceCode = offenceCode;
		this.offenceLocation = offenceLocation;
		this.offenceExcerpt = offenceExcerpt;
		this.courtCode = courtCode;
		this.courtName = courtName;
		this.courtDtime = courtDtime;
		this.caseStatus = caseStatus;
		this.statusId = statusId;
		this.reasonCode = reasonCode;
		this.comment = comment;
		if(allowUpload != null)
			this.allowUpload = allowUpload.toString();
		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		//this.authorizedFlag = authorizedFlag;
		this.authorizeUsername = authorizeUsername;
		this.authorizeDtime = authorizeDtime;
		//this.servedByUserID = issueUsername;
		this.issueDate = issueDate;
		//this.servedOnDate = new java.sql.Date(issueDate.getTime());
		this.scannedDocList = scannedDocList;
		this.currentUsername = currentUsername;
		this.documentTypeCode = Constants.DocumentType.SUMMONS;
		this.documentTypeName = Constants.DocumentTypeLong.SUMMONS;
		this.excerptParams = excerptParams;
		
	}

	/**
	 * Create a SUmmons View object from the Summons Database object
	 * 
	 * @param summons
	 */
	public SummonsBO(SummonsDO summons) {
		  
		super(summons);
		
		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getOffence() != null)
			this.offenceExcerpt = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getExcerpt();

		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getOffence() != null)
			this.offenceCode = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getOffenceId();
	
		this.servedAtAddress = summons.getServedAtAddress();
		this.servedAtParish = summons.getServedAtParish();
				
		if(summons.getDischargeWitness() != null)
			this.dischargeWitness = new PersonBO(summons.getDischargeWitness());

		this.excerpt = summons.getExcerpt();
		
		if(StringUtils.isNotBlank(summons.getVerificationReq())){
			if(summons.getVerificationReq().equalsIgnoreCase(Constants.YesNo.NO_LABEL_STR))
				this.isJPAuthorisationRequested = false;
			if(summons.getVerificationReq().equalsIgnoreCase(Constants.YesNo.YES_LABEL_STR))
				this.isJPAuthorisationRequested = true;
		}	
		
		if(origin.equalsIgnoreCase(Constants.DocumentType.ORIGIN_MANUAL))
			setManualSummons(true);

	}

	public String getOffenceExcerpt() {
		return offenceExcerpt;
	}

	public void setOffenceExcerpt(String offenceExcerpt) {
		this.offenceExcerpt = offenceExcerpt;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public CourtCaseBO getCourtCase() {
		return courtCase;
	}

	public void setCourtCase(CourtCaseBO courtCase) {
		this.courtCase = courtCase;
	}

	public boolean isManualSummons() {
		return isManualSummons;
	}

	public void setManualSummons(boolean isManualSummons) {
		this.isManualSummons = isManualSummons;
	}

	public boolean isSummonsVerified() {
		return isSummonsVerified;
	}

	public void setSummonsVerified(boolean isSummonsVerified) {
		this.isSummonsVerified = isSummonsVerified;
	}

	public boolean isJPPinAuthorised() {
		return isJPPinAuthorised;
	}

	public void setJPPinAuthorised(boolean isJPPinAuthorised) {
		this.isJPPinAuthorised = isJPPinAuthorised;
	}

	public boolean isOverrideRequested() {
		return isOverrideRequested;
	}

	public void setOverrideRequested(boolean isOverrideRequested) {
		this.isOverrideRequested = isOverrideRequested;
	}

	public boolean isJPAuthorisationRequested() {
		return isJPAuthorisationRequested;
	}

	public void setJPAuthorisationRequested(boolean isJPAuthorisationRequested) {
		this.isJPAuthorisationRequested = isJPAuthorisationRequested;
	}

	public boolean isPrintDischargeFormRequested() {
		return isPrintDischargeFormRequested;
	}

	public void setPrintDischargeFormRequested(boolean isPrintDischargeFormRequested) {
		this.isPrintDischargeFormRequested = isPrintDischargeFormRequested;
	}

	public PersonBO getDischargeWitness() {
		return dischargeWitness;
	}

	public void setDischargeWitness(PersonBO dischargeWitness) {
		this.dischargeWitness = dischargeWitness;
	}

	public String getWithdrawReason() {
		return withdrawReason;
	}

	public void setWithdrawReason(String withdrawReason) {
		this.withdrawReason = withdrawReason;
	}

	public String getServedAtParish() {
		return servedAtParish;
	}

	public void setServedAtParish(String servedAtParish) {
		this.servedAtParish = servedAtParish;
	}

	public String getServedAtAddress() {
		return servedAtAddress;
	}

	public void setServedAtAddress(String servedAtAddress) {
		this.servedAtAddress = servedAtAddress;
	}

	public AddressBO getServedAtAddressBO() {
		return servedAtAddressBO;
	}

	public void setServedAtAddressBO(AddressBO servedAtAddressBO) {
		this.servedAtAddressBO = servedAtAddressBO;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public HashMap<String, String> getExcerptParams() {
		return excerptParams;
	}

	public void setExcerptParams(HashMap<String, String> excerptParams) {
		this.excerptParams = excerptParams;
	}

	/**
	 * @return the overrideReason
	 */
	public Integer getOverrideReason() {
		return overrideReason;
	}

	/**
	 * @param overrideReason the overrideReason to set
	 */
	public void setOverrideReason(Integer overrideReason) {
		this.overrideReason = overrideReason;
	}

	

	

}
