package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.ROMS_CitationOffenceView;
import fsl.ta.toms.roms.util.NameUtil;

public class CitationOffenceBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3745454101415478689L;
	
	Date offenceDateTime;
	Integer roadCheckOffenceID;
	String offenceDescription,offenceShortDesc, offenceType;
	Date courtDate;
	String courtOutcome,trnNbr, dlNo,caseStatus;
	String plateRegNo;
	RoadLicenceBO roadLicDetails;
	String offenderFirstName,offenderLastName,offenderMidName, offenderType, offenderFullName;
	String otherRole;
	
	public CitationOffenceBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CitationOffenceBO(Date offenceDateTime, Integer roadCheckOffenceID,
			String offenceDescription, String offenceShortDesc,
			String offenceType, Date courtDate, String courtOutcome,
			String trnNbr, String dlNo, String caseStatus, String plateRegNo, String offenderType,
			String offenderFirstName,String offenderLastName, String offenderMidName,String otherRole, String otherRoleDescription) {
		super();
		this.offenceDateTime = offenceDateTime;
		this.roadCheckOffenceID = roadCheckOffenceID;
		this.offenceDescription = offenceDescription;
		this.offenceShortDesc = offenceShortDesc;
		this.offenceType = offenceType;
		this.courtDate = courtDate;
		this.courtOutcome = courtOutcome;
		this.trnNbr = trnNbr;
		this.dlNo = dlNo;
		this.caseStatus = caseStatus;
		this.plateRegNo = plateRegNo;
		
		if(offenderType != null){
			if(offenderType.equalsIgnoreCase(Constants.PersonRole.CONDUCTOR)){
				this.offenderType = Constants.PersonRole.CONDUCTOR_STRING;
			}
			else if(offenderType.equalsIgnoreCase(Constants.PersonRole.DRIVER)){
				this.offenderType = Constants.PersonRole.DRIVER_STRING;
			}
			else if(offenderType.equalsIgnoreCase(Constants.PersonRole.OWNER)){
				this.offenderType = Constants.PersonRole.OWNER_STRING;
			}else if(offenderType.equalsIgnoreCase(Constants.PersonRole.OTHER)){
				this.offenderType = "Other - " + otherRoleDescription;
			}
					
		}
		
		
		this.otherRole = otherRole;
		
		this.offenderFirstName = offenderFirstName;
		this.offenderLastName = offenderLastName;
		this.offenderMidName = offenderMidName;
		
		this.setOffenderFullName(NameUtil.getFullName(this.getOffenderFirstName(), this.getOffenderLastName()));

	}

	
	public CitationOffenceBO(ROMS_CitationOffenceView citationOffenceDO) {
	
		if(citationOffenceDO != null)
		{
			this.setCaseStatus(citationOffenceDO.getCaseStatus());
			this.setCourtDate(citationOffenceDO.getCourtDate());
			this.setCourtOutcome(citationOffenceDO.getCourtOutcome());
			this.setDlNo(citationOffenceDO.getDlNo());
			this.setOffenceDateTime(citationOffenceDO.getOffenceDateTime());
			this.setOffenceDescription(citationOffenceDO.getOffenceDescription());
			this.setOffenceShortDesc(citationOffenceDO.getOffenceShortDesc());
			this.setOffenceType(citationOffenceDO.getOffenceType());
			this.setRoadCheckOffenceID(citationOffenceDO.getRoadCheckOffenceID());
			this.setTrnNbr(citationOffenceDO.getTrnNbr());
			
			if(citationOffenceDO.getPersonRole() != null){
				if(citationOffenceDO.getPersonRole().equalsIgnoreCase(Constants.PersonRole.CONDUCTOR)){
					this.offenderType = Constants.PersonRole.CONDUCTOR_STRING;
				}
				else if(citationOffenceDO.getPersonRole().equalsIgnoreCase(Constants.PersonRole.DRIVER)){
					this.offenderType = Constants.PersonRole.DRIVER_STRING;
				}
				else if(citationOffenceDO.getPersonRole().equalsIgnoreCase(Constants.PersonRole.OWNER)){
					this.offenderType = Constants.PersonRole.OWNER_STRING;
				}
			
			}
			
			this.otherRole = citationOffenceDO.getOtherRoleId();
			this.setOffenderFirstName(citationOffenceDO.getOffenderFirstName());
			this.setOffenderLastName(citationOffenceDO.getOffenderLastName());
			this.setOffenderMidName(citationOffenceDO.getOffenderMidName()); 
		}
		
	}


	public Date getOffenceDateTime() {
		return offenceDateTime;
	}


	public void setOffenceDateTime(Date offenceDateTime) {
		this.offenceDateTime = offenceDateTime;
	}


	public Integer getRoadCheckOffenceID() {
		return roadCheckOffenceID;
	}


	public void setRoadCheckOffenceID(Integer roadCheckOffenceID) {
		this.roadCheckOffenceID = roadCheckOffenceID;
	}


	public String getOffenceDescription() {
		return offenceDescription;
	}


	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}


	public String getOffenceShortDesc() {
		return offenceShortDesc;
	}


	public void setOffenceShortDesc(String offenceShortDesc) {
		this.offenceShortDesc = offenceShortDesc;
	}


	public String getOffenceType() {
		return offenceType;
	}


	public void setOffenceType(String offenceType) {
		this.offenceType = offenceType;
	}


	public Date getCourtDate() {
		return courtDate;
	}


	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}


	public String getCourtOutcome() {
		return courtOutcome;
	}


	public void setCourtOutcome(String courtOutcome) {
		this.courtOutcome = courtOutcome;
	}


	public String getTrnNbr() {
		return trnNbr;
	}


	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}


	public String getDlNo() {
		return dlNo;
	}


	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}


	public String getCaseStatus() {
		return caseStatus;
	}


	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}


	public String getPlateRegNo() {
		return plateRegNo;
	}


	public void setPlateRegNo(String plateRegNo) {
		this.plateRegNo = plateRegNo;
	}


	public RoadLicenceBO getRoadLicDetails() {
		return roadLicDetails;
	}


	public void setRoadLicDetails(RoadLicenceBO roadLicDetails) {
		this.roadLicDetails = roadLicDetails;
	}




	public String getOffenderType()
	{
		return offenderType;
	}


	public void setOffenderType(String offenderType)
	{
		this.offenderType = offenderType;
	}


	public String getOffenderFirstName()
	{
		return offenderFirstName;
	}


	public void setOffenderFirstName(String offenderFirstName)
	{
		this.offenderFirstName = offenderFirstName;
	}


	public String getOffenderLastName()
	{
		return offenderLastName;
	}


	public void setOffenderLastName(String offenderLastName)
	{
		this.offenderLastName = offenderLastName;
	}


	public String getOffenderMidName()
	{
		return offenderMidName;
	}


	public void setOffenderMidName(String offenderMidName)
	{
		this.offenderMidName = offenderMidName;
	}


	public String getOffenderFullName()
	{
		return offenderFullName;
	}


	public void setOffenderFullName(String offenderFullName)
	{
		this.offenderFullName = offenderFullName;
	}


	public String getOtherRole() {
		return otherRole;
	}


	public void setOtherRole(String otherRole) {
		this.otherRole = otherRole;
	}
	
	
	
	
	
}
