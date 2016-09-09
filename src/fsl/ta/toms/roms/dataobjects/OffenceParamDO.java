/**
 * Created By: oanguin
 * Date: Sep 20, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.*;

/**
 * @author oanguin
 * Created Date: Sep 20, 2013
 */
@Entity
@Table(name="roms_offence_param")
public class OffenceParamDO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	OffenceParamKey offenceParamKey;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	

	
	
	public OffenceParamDO() {
		super();
		
	}

	public OffenceParamKey getOffenceParamKey() {
		return offenceParamKey;
	}

	public void setOffenceParamKey(OffenceParamKey offenceParamKey) {
		this.offenceParamKey = offenceParamKey;
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
