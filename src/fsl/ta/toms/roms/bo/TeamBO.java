package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.TeamDO;

public class TeamBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8426283995100320550L;
	private TAStaffBO teamLead;
	private Integer roadOperationId;
	private Integer teamId;
	private String teamName;
	private Integer NoMotorCycleAssigned;
	private Integer NoMotorCycleUsed;
	private boolean delete;
	
	
	public TeamBO(TeamDO teamDO){
		this.teamLead = new TAStaffBO(teamDO.getTeamLead());
		this.teamId = teamDO.getTeamId();
		this.teamName = teamDO.getTeamName();
		this.delete = false;
		this.roadOperationId=teamDO.getRoadOperation().getRoadOperationId();
		this.NoMotorCycleAssigned=teamDO.getNoMotorCycleAssigned();
		this.NoMotorCycleUsed=teamDO.getNoMotorCycleUsed();
	}
	

	
	public TeamBO(TAStaffBO teamLead, Integer roadOperationId, Integer teamId,
			String teamName, boolean delete) {
		super();
		this.teamLead = teamLead;
		this.roadOperationId = roadOperationId;
		this.teamId = teamId;
		this.teamName = teamName;
		this.delete = delete;
	}



	public TeamBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TAStaffBO getTeamLead() {
		return teamLead;
	}
	public void setTeamLead(TAStaffBO teamLead) {
		this.teamLead = teamLead;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}



	public Integer getRoadOperationId() {
		return roadOperationId;
	}



	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}



	public Integer getNoMotorCycleAssigned() {
		return NoMotorCycleAssigned;
	}



	public void setNoMotorCycleAssigned(Integer noMotorCycleAssigned) {
		NoMotorCycleAssigned = noMotorCycleAssigned;
	}



	public Integer getNoMotorCycleUsed() {
		return NoMotorCycleUsed;
	}



	public void setNoMotorCycleUsed(Integer noMotorCycleUsed) {
		NoMotorCycleUsed = noMotorCycleUsed;
	}
	
	
	
}
