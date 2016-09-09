package fsl.ta.toms.roms.bo;

import java.io.Serializable;

public class RoadCheckOffenceBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7587542018499935698L;
	
	Integer roadCheckOffenceId;
	RoadCheckBO roadCheck;
	OffenceBO offence;
	
	
	public Integer getRoadCheckOffenceId() {
		return roadCheckOffenceId;
	}
	public void setRoadCheckOffenceId(Integer roadCheckOffenceId) {
		this.roadCheckOffenceId = roadCheckOffenceId;
	}
	public RoadCheckBO getRoadCheck() {
		return roadCheck;
	}
	public void setRoadCheck(RoadCheckBO roadCheck) {
		this.roadCheck = roadCheck;
	}
	public OffenceBO getOffence() {
		return offence;
	}
	public void setOffence(OffenceBO offence) {
		this.offence = offence;
	}


}
