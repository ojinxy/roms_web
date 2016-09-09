package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.TeamArteryDO;

public class OperationArteryBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2972668727527922902L;
	
	private Integer teamId;
	private Integer roadOperationId;
	private Integer arteryId;
	
	public OperationArteryBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationArteryBO(TeamArteryDO operationArteryDO) {
		if(operationArteryDO!=null){
			if(operationArteryDO.getOperationArteryKey()!=null){
				if(operationArteryDO.getOperationArteryKey().getTeam()!=null){
					this.roadOperationId = operationArteryDO.getOperationArteryKey().getTeam().getRoadOperation().getRoadOperationId();
					this.teamId = operationArteryDO.getOperationArteryKey().getTeam().getTeamId();
				}
				if(operationArteryDO.getOperationArteryKey().getArtery()!=null){
					this.arteryId = operationArteryDO.getOperationArteryKey().getArtery().getArteryId();
				}
			}
			
		}

	}

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public Integer getArteryId() {
		return arteryId;
	}

	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	
	
}
