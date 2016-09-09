package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_operation_strategy")
public class OperationStrategyDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2636050749855562338L;

	public OperationStrategyDO() {
		super();
		// TODO Auto-generated constructor stub
	}


	@EmbeddedId
	OperationStrategyKey operationStrategyKey;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public OperationStrategyKey getOperationStrategyKey() {
		return operationStrategyKey;
	}

	public void setOperationStrategyKey(OperationStrategyKey operationStrategyKey) {
		this.operationStrategyKey = operationStrategyKey;
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
	
	
	
}	