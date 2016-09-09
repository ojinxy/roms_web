package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.TeamLocationDO;

public class OperationLocationBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2004298808791178160L;

	private Integer teamId;
	private Integer roadOperationId;
	private Integer locationId;
	
	public OperationLocationBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationLocationBO(TeamLocationDO operationLocationDO) {
		if(operationLocationDO!=null){
			if(operationLocationDO.getOperationLocationKey()!=null){
				if(operationLocationDO.getOperationLocationKey().getTeam()!=null){
					this.roadOperationId = operationLocationDO.getOperationLocationKey().getTeam().getRoadOperation().getRoadOperationId();
					this.teamId = operationLocationDO.getOperationLocationKey().getTeam().getTeamId();
				}
				if(operationLocationDO.getOperationLocationKey().getLocation()!=null){
					this.locationId = operationLocationDO.getOperationLocationKey().getLocation().getLocationId();
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	
	
}
