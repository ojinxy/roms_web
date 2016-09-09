package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_cd_document_type")
public class ScannedDocumentTypeDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8595617456890159254L;

	public ScannedDocumentTypeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="document_type_id")
	String documentTypeCode;
	
	@Column(name="document_type_desc")
	String documentTypeDescription;
	
	@Column(name="create_user")
	String createUsername;
	
	@Column(name="create_dtime")
	Date createDTime;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

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

	/**
	 * @return the documentTypeDescription
	 */
	public String getDocumentTypeDescription() {
		return documentTypeDescription;
	}

	/**
	 * @param documentTypeDescription the documentTypeDescription to set
	 */
	public void setDocumentTypeDescription(String documentTypeDescription) {
		this.documentTypeDescription = documentTypeDescription;
	}

	/**
	 * @return the createUsername
	 */
	public String getCreateUsername() {
		return createUsername;
	}

	/**
	 * @param createUsername the createUsername to set
	 */
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	/**
	 * @return the createDTime
	 */
	public Date getCreateDTime() {
		return createDTime;
	}

	/**
	 * @param createDTime the createDTime to set
	 */
	public void setCreateDTime(Date createDTime) {
		this.createDTime = createDTime;
	}

	/**
	 * @return the versionNbr
	 */
	public Integer getVersionNbr() {
		return versionNbr;
	}

	/**
	 * @param versionNbr the versionNbr to set
	 */
	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}
	

	

}
