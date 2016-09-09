package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.dataobjects.AuditEntry;

public class AuditEntryBO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4362991671325755438L;
	
	private String createUsername;
	private Date createDTime;
	private String updateUsername;
	private Date updateDTime;
	
	public AuditEntryBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuditEntryBO(AuditEntry auditEntry) {
		super();
		this.createUsername = auditEntry.getCreateUsername();
		this.createDTime = auditEntry.getCreateDTime();
		this.updateUsername = auditEntry.getUpdateUsername();
		this.updateDTime = auditEntry.getUpdateDTime();
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public Date getCreateDTime() {
		return createDTime;
	}

	public void setCreateDTime(Date createDTime) {
		this.createDTime = createDTime;
	}

	public String getUpdateUsername() {
		return updateUsername;
	}

	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}

	public Date getUpdateDTime() {
		return updateDTime;
	}

	public void setUpdateDTime(Date updateDTime) {
		this.updateDTime = updateDTime;
	}
	
	

}
