package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class DocumentsToPrintBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer automaticSerialNumber;
	String documentTypeCode;
	/**
	 * @return the automaticSerialNumber
	 */
	public Integer getAutomaticSerialNumber() {
		return automaticSerialNumber;
	}
	/**
	 * @param automaticSerialNumber the automaticSerialNumber to set
	 */
	public void setAutomaticSerialNumber(Integer automaticSerialNumber) {
		this.automaticSerialNumber = automaticSerialNumber;
	}
	/**
	 * @return the documentTypeCode
	 */
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	/**
	 * @param documentTypeCode the documentTypeCode to set
	 */
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	
	

	
	
}
