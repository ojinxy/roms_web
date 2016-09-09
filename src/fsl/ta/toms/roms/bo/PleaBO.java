package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.StatusDO;

public class PleaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5748801416868615809L;

	Integer pleaId;
	String description;
	StatusDO status;
	AuditEntry auditEntry;
	public Integer getPleaId() {
		return pleaId;
	}
	public void setPleaId(Integer pleaId) {
		this.pleaId = pleaId;
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
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}
	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	
	
}
