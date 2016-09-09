package fsl.ta.toms.roms.bo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompliancyCheckBOList extends ArrayList<CompliancyCheckBO> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -976849706162175075L;
	List<RoadCheckOffenceOutcomeBO> listOfRoadChkOffOutcome;
	Integer roadCheckID;
	
	String outcomeTypeID;
	
	HashMap<String,String> excerptParams;
	
	String allegation;
	
	String currentUserName;
	
	Boolean inHouse;

	public List<RoadCheckOffenceOutcomeBO> getListOfRoadChkOffOutcome() {
		return listOfRoadChkOffOutcome;
	}

	public void setListOfRoadChkOffOutcome(
			List<RoadCheckOffenceOutcomeBO> listOfRoadChkOffOutcome) {
		this.listOfRoadChkOffOutcome = listOfRoadChkOffOutcome;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public Integer getRoadCheckID() {
		return roadCheckID;
	}

	public void setRoadCheckID(Integer roadCheckID) {
		this.roadCheckID = roadCheckID;
	}

	public String getOutcomeTypeID() {
		return outcomeTypeID;
	}

	public void setOutcomeTypeID(String outcomeTypeID) {
		this.outcomeTypeID = outcomeTypeID;
	}

	public HashMap<String, String> getExcerptParams() {
		return excerptParams;
	}

	public void setExcerptParams(HashMap<String, String> excerptParams) {
		this.excerptParams = excerptParams;
	}

	public String getAllegation() {
		return allegation;
	}

	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}

	public Boolean getInHouse() {
		return inHouse;
	}

	public void setInHouse(Boolean inHouse) {
		this.inHouse = inHouse;
	}
	
	
	
}
