package fsl.ta.toms.roms.bo;

import java.io.Serializable;

public class OffenceCheckResultBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2864769308675712839L;

	Integer offenceCheckId;
	Integer citationCheckID;
	Integer roadCheckOffenceID;
	Integer roadLicNumber;
	String roadLicOwner;
	String roadLicStatus;
	
	public Integer getOffenceCheckId() {
		return offenceCheckId;
	}
	public void setOffenceCheckId(Integer offenceCheckId) {
		this.offenceCheckId = offenceCheckId;
	}
	public Integer getCitationCheckID() {
		return citationCheckID;
	}
	public void setCitationCheckID(Integer citationCheckID) {
		this.citationCheckID = citationCheckID;
	}
	public Integer getRoadCheckOffenceID() {
		return roadCheckOffenceID;
	}
	public void setRoadCheckOffenceID(Integer roadCheckOffenceID) {
		this.roadCheckOffenceID = roadCheckOffenceID;
	}
	public Integer getRoadLicNumber() {
		return roadLicNumber;
	}
	public void setRoadLicNumber(Integer roadLicNumber) {
		this.roadLicNumber = roadLicNumber;
	}
	public String getRoadLicOwner() {
		return roadLicOwner;
	}
	public void setRoadLicOwner(String roadLicOwner) {
		this.roadLicOwner = roadLicOwner;
	}
	public String getRoadLicStatus() {
		return roadLicStatus;
	}
	public void setRoadLicStatus(String roadLicStatus) {
		this.roadLicStatus = roadLicStatus;
	}
	
	
}
