/**
 * Created By: oanguin
 * Date: Jul 3, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtRulingBO;
import fsl.ta.toms.roms.constants.Constants;

import java.util.Date;

/**
 * @author oanguin
 * Created Date: Jul 3, 2013
 */
@Entity
@Table(name="ROMS_court_appearance") 
public class CourtAppearanceDO implements Serializable
{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8472317337281563617L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="court_appearance_id")
	Integer courtAppearanceId;
	
	@ManyToOne
	@JoinColumn(name="court_case_id")
	CourtCaseDO courtCase;
	
	@ManyToOne
	@JoinColumn(name="court_id")
	CourtDO court;
	
	@ManyToOne
	@JoinColumn(name="court_ruling_id")
	CDCourtRulingDO courtRuling;
	
	@Column(name="court_date_time")
	Date courtDTime;
	
	@Column
	String comment;
	
	@ManyToOne
	@JoinColumn(name="plea_id")
	CDPleaDO plea;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	
	@Column(name="inspector_attended")
	String inspectorAttended;
	
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getCourtAppearanceId() {
		return courtAppearanceId;
	}

	public void setCourtAppearanceId(Integer courtAppearanceId) {
		this.courtAppearanceId = courtAppearanceId;
	}

	public CourtCaseDO getCourtCase() {
		return courtCase;
	}

	public void setCourtCase(CourtCaseDO courtCase) {
		this.courtCase = courtCase;
	}

	public CourtDO getCourt() {
		return court;
	}

	public void setCourt(CourtDO court) {
		this.court = court;
	}

	public CDCourtRulingDO getCourtRuling() {
		return courtRuling;
	}

	public void setCourtRuling(CDCourtRulingDO courtRuling) {
		this.courtRuling = courtRuling;
	}



	/**
	 * @return the courtDTime
	 */
	public Date getCourtDTime() {
		return courtDTime;
	}

	/**
	 * @param courtDTime the courtDTime to set
	 */
	public void setCourtDTime(Date courtDTime) {
		this.courtDTime = courtDTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public CDPleaDO getPlea() {
		return plea;
	}

	public void setPlea(CDPleaDO plea) {
		this.plea = plea;
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
	
	public String getInspectorAttended() {
		return inspectorAttended;
	}

	public void setInspectorAttended(String inspector_attended) {
		this.inspectorAttended = inspector_attended;
	}

	public CourtAppearanceDO(Integer courtAppearanceId, CourtCaseDO courtCase,
			CourtDO court, CDCourtRulingDO courtRuling, Date courtDateTime,
			String comment, CDPleaDO plea, StatusDO status,
			AuditEntry auditEntry, Integer versionNbr, String inspectorAttended) {
		super();
		this.courtAppearanceId = courtAppearanceId;
		this.courtCase = courtCase;
		this.court = court;
		this.courtRuling = courtRuling;
		this.courtDTime = courtDateTime;
		this.comment = comment;
		this.plea = plea;
		this.status = status;
		this.auditEntry = auditEntry;
		this.versionNbr = versionNbr;
		this.inspectorAttended = inspectorAttended;
	}

	public CourtAppearanceDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourtAppearanceDO(CourtAppearanceBO courtAppearance) {
		this.status = new StatusDO();
		this.status.statusId = courtAppearance.getStatusCode();
					
		this.courtDTime = courtAppearance.getCourtDate();
		
		this.court = new CourtDO();
		this.court.courtId = courtAppearance.getCourtId();	
		
		this.comment = courtAppearance.getComment();
		
		this.courtCase = new CourtCaseDO();
		this.courtCase.courtCaseId = courtAppearance.getCourtCaseId();
		this.inspectorAttended = courtAppearance.getInspectorAttended();
		if(courtAppearance.getCourtRulingId() != null){
			this.courtRuling = new CDCourtRulingDO();
			this.courtRuling.rulingId = courtAppearance.getCourtRulingId();
		}
		
		if(courtAppearance.getPleaId() != null){
			this.plea = new CDPleaDO();
			this.plea.pleaId = courtAppearance.getPleaId();
		}
		
	}


	
	
	
}
