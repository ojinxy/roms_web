package fsl.ta.toms.roms.bo;

import java.io.Serializable;

public class WitnessWarningNoticeBO implements Serializable{

	Integer witnessPersonID;
	Integer warningNoticeID;
	public Integer getWitnessPersonID() {
		return witnessPersonID;
	}
	public void setWitnessPersonID(Integer witnessPersonID) {
		this.witnessPersonID = witnessPersonID;
	}
	public Integer getWarningNoticeID() {
		return warningNoticeID;
	}
	public void setWarningNoticeID(Integer warningNoticeID) {
		this.warningNoticeID = warningNoticeID;
	}
	
	
}
