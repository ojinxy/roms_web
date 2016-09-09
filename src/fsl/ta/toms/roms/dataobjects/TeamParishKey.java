package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class TeamParishKey implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6260277405741134699L;



	/**
	 * 
	 */
	



	public TeamParishKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne
	@JoinColumn(name="team_id")
	TeamDO team;
	
	@ManyToOne
	@JoinColumn(name="parish_code")
	ParishDO parish;



	public TeamDO getTeam() {
		return team;
	}

	public void setTeam(TeamDO team) {
		this.team = team;
	}

	public ParishDO getParish() {
		return parish;
	}

	public void setParish(ParishDO parish) {
		this.parish = parish;
	}


	
	
	
	

}
