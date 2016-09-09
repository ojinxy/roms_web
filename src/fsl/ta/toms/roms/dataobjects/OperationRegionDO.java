package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_operation_region")
public class OperationRegionDO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4008367148973195505L;

	public OperationRegionDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@EmbeddedId
	OperationRegionKey operationRegionKey;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public OperationRegionKey getOperationRegionKey() {
		return operationRegionKey;
	}

	public void setOperationRegionKey(OperationRegionKey operationRegionKey) {
		this.operationRegionKey = operationRegionKey;
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
