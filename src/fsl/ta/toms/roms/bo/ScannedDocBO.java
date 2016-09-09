package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.dataobjects.ScannedDocDO;

public class ScannedDocBO implements Serializable {

	private static final long serialVersionUID = -9136605433864021579L;

	public ScannedDocBO() {
		super();
	}

	public ScannedDocBO(ScannedDocDO doc) {
		super();
		this.docType = doc.getDocType();
		
		if(doc.getRoadCheckOffenceOutcome() != null)
			this.roadCheckOffenceOutcomeId = doc.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId();
		
		
		if(doc.getRoadCheckOffenceOutcome() != null)
			this.roadCheckOffence =doc.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence().getDescription();
		
		this.mimeType = doc.getMimeType();
		
		if( doc.getPrintCount() == null)
			this.printCount = 0;
		else
			this.printCount = doc.getPrintCount();
		
		this.printDTime = doc.getPrintDTime();
		this.printUsername = doc.getPrintUsername();
		this.relativePath = doc.getRelativePath();
		
		this.scannedDocId = doc.getScannedDocId();
		
		this.description = doc.getDescription();
		
		this.createdDate = doc.getAuditEntry().getCreateDTime();
		this.createdUser = doc.getAuditEntry().getCreateUsername();
		
		if(doc.getReprintReason() != null)
			this.reprintReasonCode = doc.getReprintReason().getReasonId();
		
		this.reprintComment = doc.getReprintComment();
	}

	
	Integer scannedDocId;

	Integer roadCheckOffenceOutcomeId;
	
	String roadCheckOffence;

	String description;

	String mimeType;

	String relativePath;

	String docType;

	//Print
	String printUsername;

	Date printDTime;

	Integer printCount;
	
	String currentUsername;

	byte[] fileContents;
	
	Date createdDate;
	
	String createdUser;
	
	String manualSerialNo;
	
	Integer reprintReasonCode;
	
	String reprintComment;
	
	String newReprintComment;
	

	public Integer getScannedDocId() {
		return scannedDocId;
	}

	public void setScannedDocId(Integer scannedDocId) {
		this.scannedDocId = scannedDocId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getPrintUsername() {
		return printUsername;
	}

	public void setPrintUsername(String printUsername) {
		this.printUsername = printUsername;
	}

	public Date getPrintDTime() {
		return printDTime;
	}

	public void setPrintDTime(Date printDTime) {
		this.printDTime = printDTime;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public Integer getRoadCheckOffenceOutcomeId() {
		return roadCheckOffenceOutcomeId;
	}

	public void setRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		this.roadCheckOffenceOutcomeId = roadCheckOffenceOutcomeId;
	}

	public String getRoadCheckOffence() {
		return roadCheckOffence;
	}

	public void setRoadCheckOffence(String roadCheckOffence) {
		this.roadCheckOffence = roadCheckOffence;
	}

	

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public byte[] getFileContents() {
		return fileContents;
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getManualSerialNo() {
		return manualSerialNo;
	}

	public void setManualSerialNo(String manualSerialNo) {
		this.manualSerialNo = manualSerialNo;
	}

	/**
	 * @return the reprintReasonCode
	 */
	public Integer getReprintReasonCode() {
		return reprintReasonCode;
	}

	/**
	 * @param reprintReason the reprintReason to set
	 */
	public void setReprintReasonCode(Integer reprintReasonCode) {
		this.reprintReasonCode = reprintReasonCode;
	}

	/**
	 * @return the reprintComment
	 */
	public String getReprintComment() {
		return reprintComment;
	}

	/**
	 * @param reprintComment the reprintComment to set
	 */
	public void setReprintComment(String reprintComment) {
		this.reprintComment = reprintComment;
	}

	/**
	 * @return the newReprintComment
	 */
	public String getNewReprintComment() {
		return newReprintComment;
	}

	/**
	 * @param newReprintComment the newReprintComment to set
	 */
	public void setNewReprintComment(String newReprintComment) {
		this.newReprintComment = newReprintComment;
	}
	
	
}
