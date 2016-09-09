package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_team_artery")
public class TeamArteryDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 631765940171698294L;

	public TeamArteryDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	@EmbeddedId
	TeamArteryKey operationArteryKey;
		
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	
	
	public TeamArteryKey getOperationArteryKey() {
		return operationArteryKey;
	}

	public void setOperationArteryKey(TeamArteryKey operationArteryKey) {
		this.operationArteryKey = operationArteryKey;
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
