package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_team_location")
public class TeamLocationDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4914733459763249861L;

	public TeamLocationDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@EmbeddedId
	TeamLocationKey operationLocationKey;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public TeamLocationKey getOperationLocationKey() {
		return operationLocationKey;
	}

	public void setOperationLocationKey(TeamLocationKey operationLocationKey) {
		this.operationLocationKey = operationLocationKey;
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
