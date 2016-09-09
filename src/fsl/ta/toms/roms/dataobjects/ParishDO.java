package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.ParishBO;

@Entity
@Table(name="ROMS_parish")
public class ParishDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8106577323109687964L;

	public ParishDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
/**
 * 
 * @param parishBO
 */
	public ParishDO(ParishBO parishBO) {
		this.parishCode = parishBO.getParishCode();
		this.description = parishBO.getDescription();
		this.officeLocationCode = parishBO.getOfficeLocationCode();
		
		this.status = new StatusDO();
		this.status.setStatusId(parishBO.getStatusId());
		
	}


	@Id
	@Column(name="parish_code")
	String parishCode;
	
	@Column(name="description")
	String description;
	
	//@ManyToOne
	//@JoinColumn(name="region_id")
	//RegionDO region;
	@Column(name="office_loc_code")
	String officeLocationCode;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;



	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public AuditEntry getAuditInfo() {
		return auditEntry;
	}

	public void setAuditInfo(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}
	
	
	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public void updateParishDOFields(ParishBO parishBO) {
		//this.parishCode = parishBO.getParishCode();
	//	this.description = parishBO.getDescription();
		this.officeLocationCode = parishBO.getOfficeLocationCode();
		
		//this.status = new StatusDO();
		//this.status.setStatusId(parishBO.getStatusId());	
	}

		
}
