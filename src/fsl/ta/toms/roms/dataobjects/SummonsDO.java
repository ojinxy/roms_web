package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.util.StringUtil;

@Entity
@Table(name = "ROMS_summons")
public class SummonsDO implements Serializable {

	private static final long serialVersionUID = -6855615164654140063L;

	public SummonsDO() {
		super();
		// a TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "summons_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer summonsId;

	@Column(name = "ref_seq_no")
	Integer referenceNo;

	@ManyToOne
	@JoinColumn(name = "road_check_offence_outcome_id")
	RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome;

	@ManyToOne
	@JoinColumn(name = "offender_id")
	PersonDO offender;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "ta_staff_id")
	TAStaffDO taStaff;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "jp_id_number")
	JPDO justiceOfPeace;

	@Column(name = "manual_serial_number")
	String manualSerialNumber;

	@Column(name = "offence_date_time")
	Date offenceDtime;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "status_id")
	StatusDO status;

	@ManyToOne
	@JoinColumn(name = "reason_id")
	ReasonDO reason;

	@Column(name = "comment")
	String comment;

	@Column(name = "allow_upload")
	Character allowUpload;

	@Column(name = "origin")
	String origin;

	@Column(name = "print_count")
	Integer printCount;

	@Column(name = "print_username")
	String printUsername;

	@Column(name = "print_dtime")
	Date printDtime;

	@Column(name = "reprint_username")
	String reprintUsername;

	@ManyToOne
	@JoinColumn(name = "reprint_reason_id")
	ReasonDO reprintReason;

	@Column(name = "reprint_comment")
	String reprintComment;

	@Column(name = "reprint_dtime")
	Date reprintDtime;

	@Column(name = "authorized")
	String authorizedFlag;

	@Column(name = "authorize_username")
	String authorizeUsername;

	@Column(name = "authorize_dtime")
	Date authorizeDtime;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "served_by_username")
	TAStaffDO servedByTAStaff;

	@Column(name = "served_on_date")
	Date servedOnDate;

	@Column(name = "served_by_parish")
	String servedAtParish;

	@Column(name = "served_by_address")
	String servedAtAddress;

	@Column(name = "excerpt")
	String excerpt;

	@Column(name = "verification_req")
	String verificationReq;

	// when summons is withdrawn/discharged a witness is needed
	@ManyToOne
	@JoinColumn(name = "discharge_witness_id")
	PersonDO dischargeWitness;

	@Embedded
	AuditEntry auditEntry;

	@Version
	@Column(name = "version_nbr")
	Integer versionNbr;

	/**
	 * 
	 * @param summons
	 */
	public SummonsDO(SummonsBO summons) {

		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.setRoadCheckOffenceOutcomeId(summons
				.getRoadCheckOffenceOutcomeCode());

		this.offender = new PersonDO();
		this.offender.setPersonId(summons.getOffenderId());

		this.taStaff = new TAStaffDO();
		this.taStaff.setStaffId(summons.getTaStaffId());

		this.justiceOfPeace = new JPDO();
		//this.justiceOfPeace.setRegNumber(summons.getJpRegNumber());
		this.justiceOfPeace.setIdNumber(summons.getJpIdNumber());

		this.manualSerialNumber = StringUtil.isSet(summons.getManualSerialNo()) ? summons.getManualSerialNo().toUpperCase() : summons.getManualSerialNo();

		this.offenceDtime = summons.getOffenceDtime();

		this.status = new StatusDO();
		this.status.setStatusId(summons.getStatusId());

		this.reason = new ReasonDO();
		this.reason.setReasonId(summons.getReasonCode());

		this.comment = summons.getComment();

		this.allowUpload = 'Y';

		if (summons.getReferenceNo() == null)
			this.referenceNo = 0;

		if (!StringUtils.isEmpty(summons.getVerificationRequired()))
			this.verificationReq = summons.getVerificationRequired();

		/*
		 * this.printUsername = summons.getPrintUsername(); this.printDtime =
		 * summons.getPrintDtime(); this.reprintUsername =
		 * summons.getReprintUsername(); this.reprintDtime =
		 * summons.getReprintDtime(); this.authorizedFlag =
		 * summons.getAuthorizedFlag(); this.authorizeUsername =
		 * summons.getAuthorizeUsername(); this.authorizeDtime =
		 * summons.getAuthorizeDtime();
		 */

		/*
		 * this.servedByTAStaffId = summons.getServedByUserID();
		 * this.servedOnDate = summons.getServedOnDate(); this.servedAtAddress =
		 * summons.getServedAtAddress(); this.servedAtParish =
		 * summons.getServedAtParish();
		 */

		/*
		 * if(summons.getDischargeWitness() != null) this.dischargeWitness = new
		 * PersonDO(summons.getDischargeWitness());
		 */
		this.excerpt = summons.getExcerpt();

	}

	/**
	 * @return the summonsId
	 */
	public Integer getSummonsId() {
		return summonsId;
	}

	/**
	 * @param summonsId
	 *            the summonsId to set
	 */
	public void setSummonsId(Integer summonsId) {
		this.summonsId = summonsId;
	}

	/**
	 * @return the roadCheckOffenceOutcome
	 */
	public RoadCheckOffenceOutcomeDO getRoadCheckOffenceOutcome() {
		return roadCheckOffenceOutcome;
	}

	/**
	 * @param roadCheckOffenceOutcome
	 *            the roadCheckOffenceOutcome to set
	 */
	public void setRoadCheckOffenceOutcome(
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome) {
		this.roadCheckOffenceOutcome = roadCheckOffenceOutcome;
	}

	/**
	 * @return the offender
	 */
	public PersonDO getOffender() {
		return offender;
	}

	/**
	 * @param offender
	 *            the offender to set
	 */
	public void setOffender(PersonDO offender) {
		this.offender = offender;
	}

	/**
	 * @return the taStaff
	 */
	public TAStaffDO getTaStaff() {
		return taStaff;
	}

	/**
	 * @param taStaff
	 *            the taStaff to set
	 */
	public void setTaStaff(TAStaffDO taStaff) {
		this.taStaff = taStaff;
	}

	/**
	 * @return the justiceOfPeace
	 */
	public JPDO getJusticeOfPeace() {
		return justiceOfPeace;
	}

	/**
	 * @param justiceOfPeace
	 *            the justiceOfPeace to set
	 */
	public void setJusticeOfPeace(JPDO justiceOfPeace) {
		this.justiceOfPeace = justiceOfPeace;
	}

	/**
	 * @return the manualSerialNumber
	 */
	public String getManualSerialNumber() {
		return manualSerialNumber;
	}

	/**
	 * @param manualSerialNumber
	 *            the manualSerialNumber to set
	 */
	public void setManualSerialNumber(String manualSerialNumber) {
		this.manualSerialNumber = StringUtil.isSet(manualSerialNumber) ? manualSerialNumber.toUpperCase() : manualSerialNumber;
	}

	/**
	 * @return the offenceDtime
	 */
	public Date getOffenceDtime() {
		return offenceDtime;
	}

	/**
	 * @param offenceDtime
	 *            the offenceDtime to set
	 */
	public void setOffenceDtime(Date offenceDtime) {
		this.offenceDtime = offenceDtime;
	}

	/**
	 * @return the status
	 */
	public StatusDO getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StatusDO status) {
		this.status = status;
	}

	/**
	 * @return the reason
	 */
	public ReasonDO getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(ReasonDO reason) {
		this.reason = reason;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the allowUpload
	 */
	public Character getAllowUpload() {
		return allowUpload;
	}

	/**
	 * @param allowUpload
	 *            the allowUpload to set
	 */
	public void setAllowUpload(Character allowUpload) {
		this.allowUpload = allowUpload;
	}

	/**
	 * @return the printUsername
	 */
	public String getPrintUsername() {
		return printUsername;
	}

	/**
	 * @param printUsername
	 *            the printUsername to set
	 */
	public void setPrintUsername(String printUsername) {
		this.printUsername = printUsername;
	}

	/**
	 * @return the printDtime
	 */
	public Date getPrintDtime() {
		return printDtime;
	}

	/**
	 * @param printDtime
	 *            the printDtime to set
	 */
	public void setPrintDtime(Date printDtime) {
		this.printDtime = printDtime;
	}

	/**
	 * @return the reprintUsername
	 */
	public String getReprintUsername() {
		return reprintUsername;
	}

	/**
	 * @param reprintUsername
	 *            the reprintUsername to set
	 */
	public void setReprintUsername(String reprintUsername) {
		this.reprintUsername = reprintUsername;
	}

	/**
	 * @return the reprintDtime
	 */
	public Date getReprintDtime() {
		return reprintDtime;
	}

	/**
	 * @param reprintDtime
	 *            the reprintDtime to set
	 */
	public void setReprintDtime(Date reprintDtime) {
		this.reprintDtime = reprintDtime;
	}

	/**
	 * @return the authorizedFlag
	 */
	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	/**
	 * @param authorizedFlag
	 *            the authorizedFlag to set
	 */
	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	/**
	 * @return the authorizeUsername
	 */
	public String getAuthorizeUsername() {
		return authorizeUsername;
	}

	/**
	 * @param authorizeUsername
	 *            the authorizeUsername to set
	 */
	public void setAuthorizeUsername(String authorizeUsername) {
		this.authorizeUsername = authorizeUsername;
	}

	/**
	 * @return the authorizeDtime
	 */
	public Date getAuthorizeDtime() {
		return authorizeDtime;
	}

	/**
	 * @param authorizeDtime
	 *            the authorizeDtime to set
	 */
	public void setAuthorizeDtime(Date authorizeDtime) {
		this.authorizeDtime = authorizeDtime;
	}

	/**
	 * @return the auditEntry
	 */
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	/**
	 * @param auditEntry
	 *            the auditEntry to set
	 */
	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	/**
	 * @return the versionNbr
	 */
	public Integer getVersionNbr() {
		return versionNbr;
	}

	/**
	 * @param versionNbr
	 *            the versionNbr to set
	 */
	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	/**
	 * @return the printCount
	 */
	public Integer getPrintCount() {
		return printCount;
	}

	/**
	 * @param printCount
	 *            the printCount to set
	 */
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public PersonDO getDischargeWitness() {
		return dischargeWitness;
	}

	public void setDischargeWitness(PersonDO dischargeWitness) {
		this.dischargeWitness = dischargeWitness;
	}

	/**
	 * @return the servedByTAStaff
	 */
	public TAStaffDO getServedByTAStaff() {
		return servedByTAStaff;
	}

	/**
	 * @param servedByTAStaff
	 *            the servedByTAStaff to set
	 */
	public void setServedByTAStaff(TAStaffDO servedByTAStaff) {
		this.servedByTAStaff = servedByTAStaff;
	}

	/**
	 * @return the servedOnDate
	 */
	public Date getServedOnDate() {
		return servedOnDate;
	}

	/**
	 * @param servedOnDate
	 *            the servedOnDate to set
	 */
	public void setServedOnDate(Date servedOnDate) {
		this.servedOnDate = servedOnDate;
	}

	/**
	 * @return the servedAtParish
	 */
	public String getServedAtParish() {
		return servedAtParish;
	}

	/**
	 * @param servedAtParish
	 *            the servedAtParish to set
	 */
	public void setServedAtParish(String servedAtParish) {
		this.servedAtParish = servedAtParish;
	}

	/**
	 * @return the servedAtAddress
	 */
	public String getServedAtAddress() {
		return servedAtAddress;
	}

	/**
	 * @param servedAtAddress
	 *            the servedAtAddress to set
	 */
	public void setServedAtAddress(String servedAtAddress) {
		this.servedAtAddress = servedAtAddress;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the verificationReq
	 */
	public String getVerificationReq() {
		return verificationReq;
	}

	/**
	 * @param verificationReq
	 *            the verificationReq to set
	 */
	public void setVerificationReq(String verificationReq) {
		this.verificationReq = verificationReq;
	}

	/**
	 * @return the referenceNo
	 */
	public Integer getReferenceNo() {
		return referenceNo;
	}

	/**
	 * @param referenceNo
	 *            the referenceNo to set
	 */
	public void setReferenceNo(Integer referenceNo) {
		this.referenceNo = referenceNo;
	}

	/**
	 * @return the reprintReason
	 */
	public ReasonDO getReprintReason() {
		return reprintReason;
	}

	/**
	 * @param reprintReason
	 *            the reprintReason to set
	 */
	public void setReprintReason(ReasonDO reprintReason) {
		this.reprintReason = reprintReason;
	}

	/**
	 * @return the reprintComment
	 */
	public String getReprintComment() {
		return reprintComment;
	}

	/**
	 * @param reprintComment
	 *            the reprintComment to set
	 */
	public void setReprintComment(String reprintComment) {
		this.reprintComment = reprintComment;
	}

}
