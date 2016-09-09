package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_cd_status")
public class StatusDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4297459721873329984L;

	public StatusDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="status_id")
	String statusId;
	
	@Column(name="description")
	String description;
	
	@Column(name="type")
	String type;
	
	@Embedded
	AuditEntry auditEntry;
		
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	
}
