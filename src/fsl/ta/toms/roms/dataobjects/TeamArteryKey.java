package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class TeamArteryKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577719317589847084L;

	public TeamArteryKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	@JoinColumn(name="team_id")
	TeamDO team;
	
	@ManyToOne
	@JoinColumn(name="artery_id")
	ArteryDO artery;

	

	public TeamDO getTeam() {
		return team;
	}

	public void setTeam(TeamDO team) {
		this.team = team;
	}

	public ArteryDO getArtery() {
		return artery;
	}

	public void setArtery(ArteryDO artery) {
		this.artery = artery;
	}
	
	
}
