/**
 * Created By: oanguin
 * Date: Sep 16, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

/**
 * @author oanguin
 * Created Date: Sep 16, 2013
 */
@Entity
@Table(name="roms_team")
public class TeamDO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="team_id")
	Integer teamId;
	
	@ManyToOne
	@JoinColumn(name="road_operation_id")
	RoadOperationDO roadOperation;
	
	@ManyToOne
	@JoinColumn(name="team_lead_id")
	TAStaffDO teamLead;
	
	@Column(name="team_name")
	String teamName;
	
	@Column(name="no_motor_cycle_assigned")
	Integer NoMotorCycleAssigned;
	
	@Column(name="no_motor_cycle_used")
	Integer NoMotorCycleUsed;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public TeamDO() 
	{
		
	}

	public TeamDO(Integer teamId, RoadOperationDO roadOperation,
			TAStaffDO teamLead, String teamName, AuditEntry auditEntry,
			Integer versionNbr) {
		super();
		this.teamId = teamId;
		this.roadOperation = roadOperation;
		this.teamLead = teamLead;
		this.teamName = teamName;
		this.auditEntry = auditEntry;
		this.versionNbr = versionNbr;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public RoadOperationDO getRoadOperation() {
		return roadOperation;
	}

	public void setRoadOperation(RoadOperationDO roadOperation) {
		this.roadOperation = roadOperation;
	}

	public TAStaffDO getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(TAStaffDO teamLead) {
		this.teamLead = teamLead;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
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
