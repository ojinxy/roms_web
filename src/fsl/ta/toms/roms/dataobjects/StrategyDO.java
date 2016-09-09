package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_strategy")
public class StrategyDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3165556255822537L;

	public StrategyDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="strategy_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer strategyId;
	
	@Column(name="description")
	String description;
	
	@Column(name="vehicle_required")
	String vehicleRequired; 
	
	@Column(name="artery_required")
	String arteryRequired; 
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVehicleRequired() {
		return vehicleRequired;
	}

	public void setVehicleRequired(String vehicleRequired) {
		this.vehicleRequired = vehicleRequired;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
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

	public String getArteryRequired() {
		return arteryRequired;
	}

	public void setArteryRequired(String arteryRequired) {
		this.arteryRequired = arteryRequired;
	}
	
	
}
