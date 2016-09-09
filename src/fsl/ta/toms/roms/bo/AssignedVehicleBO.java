package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;

public class AssignedVehicleBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -507364216309708942L;

	private Integer teamId;
	private Integer roadOperationId;
	private TAVehicleBO taVehicle;
	private boolean attended;
	private String comment;
	
	public AssignedVehicleBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public AssignedVehicleBO(AssignedVehicleDO assignedVehicleDO) {
		if(assignedVehicleDO != null){
			
			if(assignedVehicleDO.getAssignedVehicleKey()!=null){
				if(assignedVehicleDO.getAssignedVehicleKey().getTeam()!=null){
					this.roadOperationId = assignedVehicleDO.getAssignedVehicleKey().getTeam().getRoadOperation().getRoadOperationId();
					this.teamId = assignedVehicleDO.getAssignedVehicleKey().getTeam().getTeamId();
				}
				
				if(assignedVehicleDO.getAssignedVehicleKey().getTaVehicle()!=null){
					this.taVehicle = new TAVehicleBO(assignedVehicleDO.getAssignedVehicleKey().getTaVehicle());
				}
			}
			if(assignedVehicleDO.getAttended()==null){
				this.attended = false;
			}
			else{
				if(assignedVehicleDO.getAttended().equalsIgnoreCase("Y")){
					this.attended = true;
				}
				else{
					this.attended = false;
				}
			}
			
			this.comment = assignedVehicleDO.getComment();
		}
	}


	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}



	public TAVehicleBO getTaVehicle() {
		return taVehicle;
	}


	public void setTaVehicle(TAVehicleBO taVehicle) {
		this.taVehicle = taVehicle;
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


	public Integer getTeamId() {
		return teamId;
	}


	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	
	
}
