package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.CourtBO;

@Entity
@Table(name="ROMS_court")
public class CourtDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832000667269948805L;

	public CourtDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourtDO(CourtBO courtBO) {
		//this.courtId = courtBO.getCourtId();
		this.description = courtBO.getDescription();
		
		
		this.shortDesc = courtBO.getShortDescription();
				
		this.status = new StatusDO();
		this.status.setStatusId(courtBO.getStatusId());
	}

	@Id
	@Column(name="court_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer courtId;
	

	
	@Column(name="description")
	String description;
	
	@Column(name="short_desc")
	String shortDesc;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AddressDO address;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	
	
	/**
	 * @return the address
	 */
	public AddressDO getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDO address) {
		this.address = address;
	}

	public void updateCourtDOFields(CourtBO courtBO) {
		//this.courtId = courtBO.getCourtId();
		this.description = courtBO.getDescription();
		
		
		this.shortDesc = courtBO.getShortDescription();
				
		this.status = new StatusDO();
		this.status.setStatusId(courtBO.getStatusId());
		
	}

	
}
