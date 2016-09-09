package fsl.ta.toms.roms.bo;

import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;

public class WarningNoProsecutionBO extends DocumentViewBO {

	private static final long serialVersionUID = -6855615164654140063L;

	public WarningNoProsecutionBO() {
		super();

	}

	Boolean isManualWarningNoProsecution;// will help with validation
	
	String allegation;

	
	public WarningNoProsecutionBO(Integer warningNoProsecutionId,
			Integer roadOperationId, Integer roadCheckOffenceOutcomeCode,
			Integer offenderId, String taStaffId,
			Boolean isManualWarningNotice, String manualSerialNumber,
			String statusId, Integer reasonCode, String comment,
			Character allowUpload, String printUsername, Date printDtime,
			String reprintUsername, Date reprintDtime,
			Character authorizedFlag, String authorizeUsername,
			Date authorizeDtime, String issueUsername, Date issueDate,
			List<ScannedDocBO> scannedDocList, String currentUsername,
			Date offenceDtime, String allegation) {
		super();
		this.automaticSerialNo = warningNoProsecutionId;
		this.roadOperationId = roadOperationId;
		this.roadCheckOffenceOutcomeCode = roadCheckOffenceOutcomeCode;
		this.offenderId = offenderId;
		this.taStaffId = taStaffId;
		//this.isManualWarningNotice = isManualWarningNotice;
		this.manualSerialNo = manualSerialNumber;
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
		this.issueUsername = issueUsername;
		this.issueDate = issueDate;
		this.scannedDocList = scannedDocList;
		this.currentUsername = currentUsername;
		this.offenceDtime = offenceDtime;
		this.documentTypeCode = Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION;
		this.documentTypeName = Constants.DocumentTypeLong.WARNING_NOTICE_NO_PROSECUTION;
		this.allegation = allegation;
	}

	
	/**
	 * 
	 * @param warningNotice
	 */
	public WarningNoProsecutionBO(WarningNoProsecutionDO warningNoProsecution) {
		super(warningNoProsecution);
		
		this.allegation = warningNoProsecution.getAllegation();
		
		if(origin.equalsIgnoreCase(Constants.DocumentType.ORIGIN_MANUAL))
				setIsManualWarningNoProsecution(true);
		

	}




	public Boolean getIsManualWarningNoProsecution() {
		return isManualWarningNoProsecution;
	}


	public void setIsManualWarningNoProsecution(Boolean isManualWarningNoProsecution) {
		this.isManualWarningNoProsecution = isManualWarningNoProsecution;
	}


	public String getAllegation() {
		return allegation;
	}


	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}



}
