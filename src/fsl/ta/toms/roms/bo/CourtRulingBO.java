package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.StatusDO;

public class CourtRulingBO implements Serializable{

	String rulingId,description;
	Character finalRuling;
	StatusDO status;
	
	public String getRulingId() {
		return rulingId;
	}
	public void setRulingId(String rulingId) {
		this.rulingId = rulingId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Character getFinalRuling() {
		return finalRuling;
	}
	public void setFinalRuling(Character finalRuling) {
		this.finalRuling = finalRuling;
	}
	public StatusDO getStatus() {
		return status;
	}
	public void setStatus(StatusDO status) {
		this.status = status;
	}
	
	
		
}
