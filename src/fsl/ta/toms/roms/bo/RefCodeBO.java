package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/*Created By: Oneal Anguin
 * Date: 17 April 2013
 * */
public class RefCodeBO implements Serializable
{
	/**
	 * 
	 */
	public RefCodeBO()
	{
		super();
	}
	
	private static final long serialVersionUID = 1L;
	
	private String code,altId,description,shortDescription,status;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the altId
	 */
	public String getAltId() {
		return altId;
	}

	/**
	 * @param altId the altId to set
	 */
	public void setAltId(String altId) {
		this.altId = altId;
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
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public RefCodeBO(String code,String altId, String description, String shortDescription,
			String status) {
		super();
		this.code = code;
		this.altId = altId;
		this.description = description;
		this.shortDescription = shortDescription;
		this.status = status;
	}
	
	
	
}
