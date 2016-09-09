package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.dataobjects.ScannedDocDO;

public class ScannedDocHistoryBO implements Serializable {

	private static final long serialVersionUID = -9136605433864021579L;

	public ScannedDocHistoryBO() {
		super();
	}

	public ScannedDocHistoryBO(ScannedDocDO doc) {
		super();
		this.docType = doc.getDocType();
		
		if(doc.getRoadCheckOffenceOutcome() != null)
			this.roadCheckOffenceOutcomeId = doc.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId();
		
		if(doc.getRoadCheckOffenceOutcome() != null)
			this.roadCheckOffence =doc.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence().getDescription();
		
		this.mimeType = doc.getMimeType();
		
		this.relativePath = doc.getRelativePath();
		
		this.scannedDocId = doc.getScannedDocId();
		
		this.description = doc.getDescription();
		
		this.createdDate = doc.getAuditEntry().getCreateDTime();
		this.createdUser = doc.getAuditEntry().getCreateUsername();
	}

	
	
	
	Integer scannedDocId;

	Integer roadCheckOffenceOutcomeId;
	
	String roadCheckOffence;

	String description;

	String mimeType;

	String relativePath;

	String docType;
	
	String currentUsername;

	byte[] fileContents;
	
	Date createdDate;
	
	String createdUser;
	
	

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

	public Integer getRoadCheckOffenceOutcomeId() {
		return roadCheckOffenceOutcomeId;
	}

	public void setRoadCheckOffenceOutcomeId(Integer roadCheckOffenceOutcomeId) {
		this.roadCheckOffenceOutcomeId = roadCheckOffenceOutcomeId;
	}
	
	
}
