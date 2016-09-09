package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_offence_law")
public class OffenceLawDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -985160499579130693L;
	
	public OffenceLawDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@EmbeddedId
	OffenceLawKey offenceLawKey;
	
	@Embedded
	AuditEntry auditEntry;
		
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	

	public OffenceLawKey getOffenceLawKey() {
		return offenceLawKey;
	}

	public void setOffenceLawKey(OffenceLawKey offenceLawKey) {
		this.offenceLawKey = offenceLawKey;
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
