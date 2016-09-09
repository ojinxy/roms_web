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

import fsl.ta.toms.roms.bo.GoverningLawBO;


@Entity
@Table(name="ROMS_governing_law")
public class GoverningLawDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 593628627843819751L;

	@Embedded
	AuditEntry auditEntry;
	
	@Column(name="description")
	String description;

	@Id
	@Column(name="law_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer lawId;
	
	@Column(name="short_desc")
	String shortDesc;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	

	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	public GoverningLawDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GoverningLawDO(GoverningLawBO governingLawBO) {
		this.description = governingLawBO.getDescription();
		//this.lawId = governingLawBO.getLawId();
		this.shortDesc = governingLawBO.getShortDesc();

		this.status = new StatusDO();
		this.status.setStatusId(governingLawBO.getStatusId());
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public String getDescription() {
		return description;
	}

	public Integer getLawId() {
		return lawId;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public StatusDO getStatus() {
		return status;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLawId(Integer lawId) {
		this.lawId = lawId;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	public void updateGoverningLawDOFields(GoverningLawBO governingLawBO) {
		this.description = governingLawBO.getDescription();
		//this.lawId = governingLawBO.getLawId();
		this.shortDesc = governingLawBO.getShortDesc();

		this.status = new StatusDO();
		this.status.setStatusId(governingLawBO.getStatusId());
		
	}

	
	
}
