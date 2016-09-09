/**
 * 
 */
package org.fsl.roms.view;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.UploadedFile;

/**
 * @author oanguin
 *This view will be used with an Data Table multiple select model
 *associated with the Road Compliancy Review Summary screen.
 */
public class AssociatedDocView implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String documentType;
	
	public String documentTypeCode;
	
	public List<String> associatedOffences;
	
	private List<Integer> offenceIDs;
	
	public Boolean printed;
	
	public String serialNum;
	
	public Integer index;
	
	private String comment;
	
	private String fileSize;
	
	private String fileType;
	
	private UploadedFile uploadedFile;
	
	private Boolean verified;
	

	public AssociatedDocView(String documentType,
			List<String> associatedOffences, Boolean printed, String serialNum,
			Integer index,List<Integer> associatedOffenceIDs,String documentTypeCode) {
		super();
		this.documentType = documentType;
		this.associatedOffences = associatedOffences;
		this.printed = printed;
		this.serialNum = serialNum;
		this.index = index;
		this.offenceIDs = associatedOffenceIDs;
		this.documentTypeCode = documentTypeCode;
	}

	public AssociatedDocView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public List<String> getAssociatedOffences() {
		return associatedOffences;
	}

	public void setAssociatedOffences(List<String> associatedOffences) {
		this.associatedOffences = associatedOffences;
	}

	public Boolean getPrinted() {
		return printed;
	}

	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public List<Integer> getOffenceIDs() {
		return offenceIDs;
	}

	public void setOffenceIDs(List<Integer> offenceIDs) {
		this.offenceIDs = offenceIDs;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	
	
	

}
