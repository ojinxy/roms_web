package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.util.NameUtil;

public class PoliceOfficerBO extends AttendanceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 868207083487840548L;

	private Integer polOfficerCompNo;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String rank;
	private String stationDescription;
	private String statusId;
	private Integer personID; 
	
	private String currentUserName;

	private boolean scheduled;
	
	public PoliceOfficerBO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public PoliceOfficerBO(Integer polOfficerCompNo, String firstName,
			String middleName, String lastName, String rank,
			String stationDescription, String statusId, String currentUserName) {
		super();
		
		NameUtil util = new NameUtil();
		this.polOfficerCompNo = polOfficerCompNo;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.fullName= util.getFullName(firstName, lastName);
		this.rank = rank;
		this.stationDescription = stationDescription;
		this.statusId = statusId;
		this.currentUserName = currentUserName;
	}


	public PoliceOfficerBO(PoliceOfficerDO policeOfficerDO) {
		
		if(policeOfficerDO!=null){
		
			this.polOfficerCompNo = policeOfficerDO.getPolOfficerCompNo();
		
			if(policeOfficerDO.getPerson()!=null){
				this.personID = policeOfficerDO.getPerson().getPersonId();
				
				this.firstName = policeOfficerDO.getPerson().getFirstName();
				this.middleName = policeOfficerDO.getPerson().getMiddleName();
				this.lastName = policeOfficerDO.getPerson().getLastName();
				
				
				NameUtil util = new NameUtil();
				this.fullName= util.getFullName(firstName, lastName);
				
			}
			
			this.rank = policeOfficerDO.getRank();
			this.stationDescription = policeOfficerDO.getStationDescription();
			
			if(policeOfficerDO.getStatus()!=null){
				this.statusId = policeOfficerDO.getStatus().getStatusId();
			}
			
			
		}
	}
	

	public PoliceOfficerBO(PoliceOfficerDO policeOfficerDO, AssignedPersonDO assignedPersonDO) {
		
		if(policeOfficerDO!=null){
		
			this.polOfficerCompNo = policeOfficerDO.getPolOfficerCompNo();
		
			if(policeOfficerDO.getPerson()!=null){
				this.personID = policeOfficerDO.getPerson().getPersonId();
				
				this.firstName = policeOfficerDO.getPerson().getFirstName();
				this.middleName = policeOfficerDO.getPerson().getMiddleName();
				this.lastName = policeOfficerDO.getPerson().getLastName();
				
				
				NameUtil util = new NameUtil();
				this.fullName= util.getFullName(firstName, lastName);
				
			}
			
			this.rank = policeOfficerDO.getRank();
			this.stationDescription = policeOfficerDO.getStationDescription();
			
			if(policeOfficerDO.getStatus()!=null){
				this.statusId = policeOfficerDO.getStatus().getStatusId();
			}
			
			
		}
		if(assignedPersonDO!=null){
			this.setComment(assignedPersonDO.getComment());
			
			this.setScheduled(true);
			
			if(assignedPersonDO.getAttended()==null){
				this.setAttended(null);
			}
			else{
				if(assignedPersonDO.getAttended().equalsIgnoreCase("Y")){
					this.setAttended(true);
				}
				if(assignedPersonDO.getAttended().equalsIgnoreCase("N")){
					this.setAttended(false);
				}
			}
		}
	}


	
	
	
	public Integer getPolOfficerCompNo() {
		return polOfficerCompNo;
	}


	public void setPolOfficerCompNo(Integer polOfficerCompNo) {
		this.polOfficerCompNo = polOfficerCompNo;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public String getStationDescription() {
		return stationDescription;
	}


	public void setStationDescription(String stationDescription) {
		this.stationDescription = stationDescription;
	}


	public String getStatusId() {
		return statusId;
	}


	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}


	public Integer getPersonID() {
		return personID;
	}


	public void setPersonID(Integer personID) {
		this.personID = personID;
	}



	public String getCurrentUserName() {
		return currentUserName;
	}



	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	
	public boolean isScheduled() {
		return this.scheduled;
	}

	
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
		
	}
	
	
}
