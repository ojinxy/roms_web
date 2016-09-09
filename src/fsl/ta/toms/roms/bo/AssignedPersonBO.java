package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.util.NameUtil;

public class AssignedPersonBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856031551994272847L;
	
	private Integer teamId;
	private Integer roadOperationId;
	private Integer personId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String personTypeId;
	private String otherInfo;
	private boolean attended;
	private String comment;
	
	
	
	public AssignedPersonBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public AssignedPersonBO(AssignedPersonDO assignedPersonDO) {
		if(assignedPersonDO != null){
			
			if(assignedPersonDO.getAssignedPersonKey()!=null){
				if(assignedPersonDO.getAssignedPersonKey().getTeam()!=null){
					this.roadOperationId = assignedPersonDO.getAssignedPersonKey().getTeam().getRoadOperation().getRoadOperationId();
					this.teamId = assignedPersonDO.getAssignedPersonKey().getTeam().getTeamId();
				}
				if(assignedPersonDO.getAssignedPersonKey().getPersonType()!=null){
					this.personTypeId = assignedPersonDO.getAssignedPersonKey().getPersonType().getPersonTypeId();
				}
				if(assignedPersonDO.getAssignedPersonKey().getPerson()!=null){
					this.personId = assignedPersonDO.getAssignedPersonKey().getPerson().getPersonId();
					this.firstName = assignedPersonDO.getAssignedPersonKey().getPerson().getFirstName();
					this.middleName = assignedPersonDO.getAssignedPersonKey().getPerson().getMiddleName();
					this.lastName = assignedPersonDO.getAssignedPersonKey().getPerson().getLastName();
	
					NameUtil util = new NameUtil();
					this.fullName= util.getFullName(firstName, lastName);
				}
			}
			
			if(assignedPersonDO.getAttended()==null){
				this.attended = false;
			}
			else{
				if(assignedPersonDO.getAttended().equalsIgnoreCase("Y")){
					this.attended = true;
				}
				else{
					this.attended = false;
				}
			}
		
			this.comment = assignedPersonDO.getComment();
		}
	}


	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public Integer getRoadOperationId() {
		return roadOperationId;
	}
	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}
	public String getPersonTypeId() {
		return personTypeId;
	}
	public void setPersonTypeId(String personTypeId) {
		this.personTypeId = personTypeId;
	}

	public boolean isAttended() {
		return attended;
	}

	public void setAttended(boolean attended) {
		this.attended = attended;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}


	public Integer getTeamId() {
		return teamId;
	}


	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}


	
}
