package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

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

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import antlr.StringUtils;

import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.util.StringUtil;

@Entity
@Table(name = "ROMS_warning_notice")
public class WarningNoticeDO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9139727123659152615L;

	public WarningNoticeDO() {
		super();
	}

	@Id
	@Column(name = "warning_notice_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer	warningNoticeId;

	@Generated(GenerationTime.INSERT)
	@Column(name = "ref_seq_no")
	Integer referenceNo;

	@ManyToOne
	@JoinColumn(name = "road_check_offence_outcome_id")
	RoadCheckOffenceOutcomeDO	roadCheckOffenceOutcome;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "offender_id")
	PersonDO					offender;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "ta_staff_id")
	TAStaffDO					taStaff;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "pound_id")
	PoundDO						pound;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "wrecking_company_id")
	WreckingCompanyDO			wreckingCompany;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "vehicle_delivered_by_person_id")
	PersonDO vehicleDeliveredByPerson;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "vehicle_delivered_by_person_type")
	CDPersonTypeDO	vehicleDeliveredByPersonType;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "wrecker_vehicle_id")
	VehicleDO	wreckerVehicle;


	@Column(name = "manual_serial_no")
	String						manualSerialNumber;

	@Column(name = "seizure_date_time")
	Date						seizureDtime;

	@ManyToOne
	@JoinColumn(name = "status_id")
	StatusDO					status;

	@ManyToOne
	@JoinColumn(name = "reason_id")
	ReasonDO					reason;

	@Column(name = "comment")
	String						comment;

	@Column(name = "allow_upload")
	Character					allowUpload;

	@Column(name = "print_count")
	Integer						printCount;

	@Column(name = "print_username")
	String						printUsername;

	@Column(name = "print_dtime")
	Date						printDtime;

	@Column(name = "reprint_username")
	String						reprintUsername;

	@Column(name = "reprint_dtime")
	Date						reprintDtime;

	@ManyToOne
	@JoinColumn(name = "reprint_reason_id")
	ReasonDO					reprintReason;
	
	@Column(name = "reprint_comment")
	String						reprintComment;

	@Column(name = "authorized")
	String						authorizedFlag;

	@Column(name = "authorize_username")
	String						authorizeUsername;

	@Column(name = "authorize_dtime")
	Date						authorizeDtime;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "served_by_username")
	TAStaffDO					servedByTAStaff;

	@Column(name = "served_on_date")
	Date						servedOnDate;

	@Column(name = "allegation")
	String						allegation;

	@Column(name = "origin")
	String						origin;

	@Column(name = "verification_req")
	String						verificationReq;

	@Embedded
	AuditEntry					auditEntry;

	@Version
	@Column(name = "version_nbr")
	Integer						versionNbr;
	
	

	/**
	 * @return the warningNoticeId
	 */
	public Integer getWarningNoticeId() {
		return warningNoticeId;
	}

	/**
	 * @param warningNoticeId
	 *            the warningNoticeId to set
	 */
	public void setWarningNoticeId(Integer warningNoticeId) {
		this.warningNoticeId = warningNoticeId;
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
	public void setRoadCheckOffenceOutcome(RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome) {
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
	 * @return the pound
	 */
	public PoundDO getPound() {
		return pound;
	}

	/**
	 * @param pound
	 *            the pound to set
	 */
	public void setPound(PoundDO pound) {
		this.pound = pound;
	}

	/**
	 * @return the wreckingCompany
	 */
	public WreckingCompanyDO getWreckingCompany() {
		return wreckingCompany;
	}

	/**
	 * @param wreckingCompany
	 *            the wreckingCompany to set
	 */
	public void setWreckingCompany(WreckingCompanyDO wreckingCompany) {
		this.wreckingCompany = wreckingCompany;
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
	 * @return the seizureDtime
	 */
	public Date getSeizureDtime() {
		return seizureDtime;
	}

	/**
	 * @param seizureDtime
	 *            the seizureDtime to set
	 */
	public void setSeizureDtime(Date seizureDtime) {
		this.seizureDtime = seizureDtime;
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

	public Date getServedOnDate() {
		return servedOnDate;
	}

	public void setServedOnDate(Date servedOnDate) {
		this.servedOnDate = servedOnDate;
	}

	public WarningNoticeDO(WarningNoticeBO warningNotice) {

		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.setRoadCheckOffenceOutcomeId(warningNotice.getRoadCheckOffenceOutcomeCode());

		this.offender = new PersonDO();
		this.offender.setPersonId(warningNotice.getOffenderId());

		this.taStaff = new TAStaffDO();
		this.taStaff.setStaffId(warningNotice.getTaStaffId());

		this.status = new StatusDO();
		this.status.setStatusId(warningNotice.getStatusId());

		this.reason = new ReasonDO();
		this.reason.setReasonId(warningNotice.getReasonCode());

		this.comment = warningNotice.getComment();

		this.allowUpload = 'Y';

		this.printUsername = warningNotice.getPrintUsername();

		this.printDtime = warningNotice.getPrintDtime();

		this.reprintUsername = warningNotice.getReprintUsername();

		this.reprintDtime = warningNotice.getReprintDtime();

		this.authorizedFlag = warningNotice.getAuthorizedFlag();

		this.authorizeUsername = warningNotice.getAuthorizeUsername();

		this.allegation = warningNotice.getAllegation();

		if(warningNotice.getReferenceNo() == null)
			this.referenceNo = 0;
		/*
		 * this.issueUsername = warningNotice.getIssueUsername(); this.issueDate
		 * = warningNotice.getIssueDate();
		 */
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
	 * @return the allegation
	 */
	public String getAllegation() {
		return allegation;
	}

	/**
	 * @param allegation the allegation to set
	 */
	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
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
	 * @param reprintReason the reprintReason to set
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
	 * @param reprintComment the reprintComment to set
	 */
	public void setReprintComment(String reprintComment) {
		this.reprintComment = reprintComment;
	}

	public PersonDO getVehicleDeliveredByPerson()
	{
		return vehicleDeliveredByPerson;
	}

	public void setVehicleDeliveredByPerson(PersonDO vehicleDeliveredByPerson)
	{
		this.vehicleDeliveredByPerson = vehicleDeliveredByPerson;
	}

	public CDPersonTypeDO getVehicleDeliveredByPersonType()
	{
		return vehicleDeliveredByPersonType;
	}

	public void setVehicleDeliveredByPersonType(
			CDPersonTypeDO vehicleDeliveredByPersonType)
	{
		this.vehicleDeliveredByPersonType = vehicleDeliveredByPersonType;
	}

	public VehicleDO getWreckerVehicle()
	{
		return wreckerVehicle;
	}

	public void setWreckerVehicle(VehicleDO wreckerVehicle)
	{
		this.wreckerVehicle = wreckerVehicle;
	}


	
	

}
