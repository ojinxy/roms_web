package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class AssignedPersonKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3125330023221125563L;

	public AssignedPersonKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@ManyToOne
	@JoinColumn(name="person_id")
	PersonDO person;
	
	
	@ManyToOne
	@JoinColumn(name="person_type_id")
	CDPersonTypeDO personType;
	

	@ManyToOne
	@JoinColumn(name="team_id")
	TeamDO team;

	public PersonDO getPerson() {
		return person;
	}

	public void setPerson(PersonDO person) {
		this.person = person;
	}



	public TeamDO getTeam() {
		return team;
	}

	public void setTeam(TeamDO team) {
		this.team = team;
	}

	public CDPersonTypeDO getPersonType() {
		return personType;
	}

	public void setPersonType(CDPersonTypeDO personType) {
		this.personType = personType;
	}

	/*public AssignedPersonKey(PersonDO person, CDPersonTypeDO personType,
			RoadOperationDO roadOperation) {
		super();
		this.person = person;
		this.personType = personType;
		this.roadOperation = roadOperation;
	}*/

	
	
	
	
}
