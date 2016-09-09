/**
 * Created By: oanguin
 * Date: Apr 24, 2013
 *
 */
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

/**
 * @author oanguin
 * Created Date: Apr 24, 2013
 */

@Entity
@Table(name="ROMS_CD_Verdict")
public class CDVerdictDO implements Serializable 
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6304305763128954704L;

	public CDVerdictDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CDVerdictDO(Integer verdict_code, String verdict_desc,
			StatusDO status) {
		super();
		this.verdict_code = verdict_code;
		this.verdict_desc = verdict_desc;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="verdict_code")
	Integer verdict_code;
	
	@Column(name="verdict_desc")
	String verdict_desc;
	
	@JoinColumn(name="status_id")
	@ManyToOne
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getVerdict_code() {
		return verdict_code;
	}

	public void setVerdict_code(Integer verdict_code) {
		this.verdict_code = verdict_code;
	}

	public String getVerdict_desc() {
		return verdict_desc;
	}

	public void setVerdict_desc(String verdict_desc) {
		this.verdict_desc = verdict_desc;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}
	
	

}
