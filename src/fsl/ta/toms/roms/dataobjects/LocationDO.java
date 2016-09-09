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

import fsl.ta.toms.roms.bo.LocationBO;

@Entity
@Table(name="ROMS_location")
public class LocationDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4665329858014530725L;

	public LocationDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocationDO(LocationBO locationBO) {
		this.description = locationBO.getLocationDescription();
		
		this.parish = new ParishDO();
		this.parish.setParishCode(locationBO.getParishCode());
		
		this.status = new StatusDO();
		this.status.setStatusId(locationBO.getStatusId());
		
		this.shortDesc = locationBO.getShortDesc();
	}

	@Id
	@Column(name="location_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer locationId;
	
	@Column(name="description")
	String description;
	
	@Column(name="short_desc")
	String shortDesc;
	
	@ManyToOne
	@JoinColumn(name="parish_code")
	ParishDO parish;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;	
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
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

	public ParishDO getParish() {
		return parish;
	}

	public void setParish(ParishDO parish) {
		this.parish = parish;
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

	public void updateLocationDOFields(LocationBO locationBO) {
this.description = locationBO.getLocationDescription();
		
		this.parish = new ParishDO();
		this.parish.setParishCode(locationBO.getParishCode());
		
		this.status = new StatusDO();
		this.status.setStatusId(locationBO.getStatusId());
		
		this.shortDesc = locationBO.getShortDesc();
		
	}

		
}
