package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.RoadCheckDO;

public class RoadCheckBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6377183345789763681L;
	Integer roadCheckId;
	String roadCheckTypeID;
	Integer complianceID;
	Character compliant;
	String comment;
	public Integer getRoadCheckId() {
		return roadCheckId;
	}
	
	

	
	public RoadCheckBO() {
		super();
		// TODO Auto-generated constructor stub
	}




	public RoadCheckBO(RoadCheckDO roadChkDO) {
		super();
		this.roadCheckId = roadChkDO.getRoadCheckId();
		this.roadCheckTypeID = roadChkDO.getRoadCheckType().getRoadCheckTypeId();
		this.complianceID = roadChkDO.getCompliance().getComplianceId();
		this.compliant = roadChkDO.getCompliant();
		this.comment = roadChkDO.getComment();
	}






	public void setRoadCheckId(Integer roadCheckId) {
		this.roadCheckId = roadCheckId;
	}
	public String getRoadCheckTypeID() {
		return roadCheckTypeID;
	}
	public void setRoadCheckTypeID(String roadCheckTypeID) {
		this.roadCheckTypeID = roadCheckTypeID;
	}
	public Integer getComplianceID() {
		return complianceID;
	}
	public void setComplianceID(Integer complianceID) {
		this.complianceID = complianceID;
	}
	public Character getCompliant() {
		return compliant;
	}
	public void setCompliant(Character compliant) {
		this.compliant = compliant;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
