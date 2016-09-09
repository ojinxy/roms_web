package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_witness_warning_notice")
public class WitnessWarningNoticeDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6335411032066301030L;

	public WitnessWarningNoticeDO() {
		super();
		this.pk = new WitnessWarningNoticeKey();
	}
	
	
	
	
	
	public WitnessWarningNoticeDO(WitnessWarningNoticeKey pk,
			AuditEntry auditEntry) {
		super();
		this.pk = pk;
		this.auditEntry = auditEntry;
	}

	


	/*@Id
	@ManyToOne
	@JoinColumn(name="witness_id")
	PersonDO witness;
	
	@ManyToOne
	@JoinColumn(name="warning_notice_id")
	WarningNoticeDO warningNotice;*/
	/* Get many to many to function between warning notice and person*/
	@EmbeddedId
	WitnessWarningNoticeKey pk;
	/*__________________________________*/
	
	
	@Embedded
	AuditEntry auditEntry;
	
	public WitnessWarningNoticeKey getPk() {
		return pk;
	}

	public void setPk(WitnessWarningNoticeKey pk) {
		this.pk = pk;
	}

	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	/*public PersonDO getWitness() {
		return witness;
	}

	public void setWitness(PersonDO witness) {
		this.witness = witness;
	}

	public WarningNoticeDO getWarningNotice() {
		return warningNotice;
	}

	public void setWarningNotice(WarningNoticeDO warningNotice) {
		this.warningNotice = warningNotice;
	}*/

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
