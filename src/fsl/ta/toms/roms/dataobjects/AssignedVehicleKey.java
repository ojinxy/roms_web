package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AssignedVehicleKey implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -624611899080196395L;

	
	
	public AssignedVehicleKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	@JoinColumn(name="ta_vehicle_id", nullable=false)
	TAVehicleDO taVehicle;
	
	@ManyToOne
	@JoinColumn(name="team_id", nullable=false)
	TeamDO team;

	public TAVehicleDO getTaVehicle() {
		return taVehicle;
	}

	public void setTaVehicle(TAVehicleDO taVehicle) {
		this.taVehicle = taVehicle;
	}

	

	public TeamDO getTeam() {
		return team;
	}

	public void setTeam(TeamDO team) {
		this.team = team;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
