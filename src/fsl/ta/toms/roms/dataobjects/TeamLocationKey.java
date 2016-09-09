package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class TeamLocationKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -662442132546140194L;

	public TeamLocationKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne
	@JoinColumn(name="team_id")
	TeamDO team;
	
	@ManyToOne
	@JoinColumn(name="location_id")
	LocationDO location;


	
	public TeamDO getTeam() {
		return team;
	}

	public void setTeam(TeamDO team) {
		this.team = team;
	}

	public LocationDO getLocation() {
		return location;
	}

	public void setLocation(LocationDO location) {
		this.location = location;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
