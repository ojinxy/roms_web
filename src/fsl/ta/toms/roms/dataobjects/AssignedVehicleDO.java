package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_assigned_vehicle")
public class AssignedVehicleDO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 710591027452159128L;

	public AssignedVehicleDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@EmbeddedId
	AssignedVehicleKey assignedVehicleKey;
	
	
	@Column(name="attended")
	String attended;
	
	@Column(name="comment")
	String comment;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	
	
	public String getAttended() {
		return attended;
	}

	public void setAttended(String attended) {
		this.attended = attended;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	

	public AssignedVehicleKey getAssignedVehicleKey() {
		return assignedVehicleKey;
	}

	public void setAssignedVehicleKey(AssignedVehicleKey assignedVehicleKey) {
		this.assignedVehicleKey = assignedVehicleKey;
	}

	
	}
