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
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_event_audit")
public class EventAuditDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4468273076545659575L;
	
	public EventAuditDO() {
		super();
		this.auditEntry = new AuditEntry();
	}

	@Id
	@Column(name="event_audit_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer eventAuditId;

	@ManyToOne	
	@JoinColumn(name="event_code")
	CDEventDO event;
	
	@ManyToOne
	@JoinColumn(name="ref_type_1")
	CDEventRefTypeDO refType1;
	

	@Column(name="ref_value_1")
	String refValue1;
	
	@ManyToOne
	@JoinColumn(name="ref_type_2")
	CDEventRefTypeDO refType2;
	
	@Column(name="ref_value_2")
	String refValue2;
	
	@Column(name="comment")
	String comment;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	@Transient
	Integer eventCode;
	
	@Transient
	String refType1Code;
	
	@Transient
	String refType2Code;

	public Integer getEventAuditId() {
		return eventAuditId;
	}

	public void setEventAuditId(Integer eventAuditId) {
		this.eventAuditId = eventAuditId;
	}

	public CDEventDO getEvent() {
		return event;
	}

	public void setEvent(CDEventDO event) {
		this.event = event;
	}

	
	public CDEventRefTypeDO getRefType1() {
		return refType1;
	}

	public void setRefType1(CDEventRefTypeDO refType1) {
		this.refType1 = refType1;
	}

	

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}


	

	public String getRefValue1() {
		return refValue1;
	}

	public void setRefValue1(String refValue1) {
		this.refValue1 = refValue1;
	}

	public CDEventRefTypeDO getRefType2() {
		return refType2;
	}

	public void setRefType2(CDEventRefTypeDO refType2) {
		this.refType2 = refType2;
	}

	public String getRefValue2() {
		return refValue2;
	}

	public void setRefValue2(String refValue2) {
		this.refValue2 = refValue2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	public Integer getEventCode() {
		return eventCode;
	}

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	public String getRefType1Code() {
		return refType1Code;
	}

	public void setRefType1Code(String refType1Code) {
		this.refType1Code = refType1Code;
	}

	public String getRefType2Code() {
		return refType2Code;
	}

	public void setRefType2Code(String refType2Code) {
		this.refType2Code = refType2Code;
	}

	
	
	
}
