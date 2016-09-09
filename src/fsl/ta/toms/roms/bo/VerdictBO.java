package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.StatusDO;

public class VerdictBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -28979443602919497L;
	/**
	 * 
	 */
	Integer verdict_code;
	String verdict_desc;
	StatusDO status;
	String currentUserName;
	public Integer getVerdict_code() {
		return verdict_code;
	}
	public void setVerdict_code(Integer verdict_code) {
		this.verdict_code = verdict_code;
	}
	public String getVerdict_desc() {
		return verdict_desc;
	}
	public void setVerdict_desc(String verdict_desc) {
		this.verdict_desc = verdict_desc;
	}
	public StatusDO getStatus() {
		return status;
	}
	public void setStatus(StatusDO status) {
		this.status = status;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	
	
}
