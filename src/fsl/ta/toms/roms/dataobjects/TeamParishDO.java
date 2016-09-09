package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_team_parish")
public class TeamParishDO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4008367148973195505L;

	public TeamParishDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@EmbeddedId
	TeamParishKey teamParishKey;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	

	public TeamParishKey getTeamParishKey() {
		return teamParishKey;
	}

	public void setTeamParishKey(TeamParishKey teamParishKey) {
		this.teamParishKey = teamParishKey;
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
