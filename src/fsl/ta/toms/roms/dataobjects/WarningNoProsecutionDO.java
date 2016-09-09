/**
 * Created By: oanguin
 * Date: Aug 7, 2013
 *
 */
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

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Aug 7, 2013
 */
@Entity
@Table(name="roms_warning_no_prosecution")
public class WarningNoProsecutionDO implements Serializable 
{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -487438614148830787L;

	@Id
	@Column(name="warning_no_prosecution_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer warningNoProsecutionID;
	
	@Generated(GenerationTime.INSERT)
	@Column(name="ref_seq_no")	
	Integer referenceNo;
	
	/*@ManyToOne
	@JoinColumn(name="road_operation_id")
	RoadOperationDO roadOperation;
*/	
	@ManyToOne
	@JoinColumn(name="road_check_offence_outcome_id")
	RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome;
	
	@ManyToOne
	@JoinColumn(name="offender_id")
	PersonDO offender;
	
	@ManyToOne
	@JoinColumn(name="ta_staff_id")
	TAStaffDO taStaff;
	
	@Column(name="manual_serial_no")
	String manualSerialNumber;
	
	@Column(name="allegation")
	String allegation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@ManyToOne
	@JoinColumn(name="reason_id")
	ReasonDO reason;
	
	@Column(name="comment")
	String comment;
	
	@Column(name="allow_upload")
	Character allowUpload;
	
	@Column(name="print_count")
	Integer printCount;
	
	@Column(name="print_username")
	String printUsername;
	
	@Column(name="print_dtime")
	Date printDtime;
	
	@Column(name="reprint_username")
	String reprintUsername;
	
	@Column(name="reprint_dtime")
	Date reprintDtime;
	
	@ManyToOne
	@JoinColumn(name = "reprint_reason_id")
	ReasonDO						reprintReason;
	
	@Column(name = "reprint_comment")
	String						reprintComment;
	
	@Column(name="authorized")
	String authorizedFlag;
	
	@Column(name="authorize_username")
	String authorizeUsername;
	
	@Column(name="authorize_dtime")
	Date authorizeDtime;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "served_by_username")	
	TAStaffDO servedByTAStaff;
	
	@Column(name="served_on_date")
	Date servedOnDate;
	
	@Column(name="offence_date_time")
	Date offenceDtime;
	
	@Column(name="verification_req")
	String verificationReq;
	
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public WarningNoProsecutionDO() 
	{
		super();
		
	}

	public Integer getWarningNoProsecutionID() {
		return warningNoProsecutionID;
	}

	public void setWarningNoProsecutionID(Integer warningNoProsecutionID) {
		this.warningNoProsecutionID = warningNoProsecutionID;
	}

	
	public RoadCheckOffenceOutcomeDO getRoadCheckOffenceOutcome() {
		return roadCheckOffenceOutcome;
	}

	public void setRoadCheckOffenceOutcome(
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome) {
		this.roadCheckOffenceOutcome = roadCheckOffenceOutcome;
	}


	public PersonDO getOffender() {
		return offender;
	}

	public void setOffender(PersonDO offender) {
		this.offender = offender;
	}

	public TAStaffDO getTaStaff() {
		return taStaff;
	}

	public void setTaStaff(TAStaffDO taStaff) {
		this.taStaff = taStaff;
	}


	public String getManualSerialNumber() {
		return manualSerialNumber;
	}

	public void setManualSerialNumber(String manualSerialNumber) {
		this.manualSerialNumber = StringUtil.isSet(manualSerialNumber) ? manualSerialNumber.toUpperCase() : manualSerialNumber ;
	}

	public String getAllegation() {
		return allegation;
	}

	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}



	public ReasonDO getReason() {
		return reason;
	}

	public void setReason(ReasonDO reason) {
		this.reason = reason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Character getAllowUpload() {
		return allowUpload;
	}

	public void setAllowUpload(Character allowUpload) {
		this.allowUpload = allowUpload;
	}

	public String getPrintUsername() {
		return printUsername;
	}

	public void setPrintUsername(String printUsername) {
		this.printUsername = printUsername;
	}

	public Date getPrintDtime() {
		return printDtime;
	}

	public void setPrintDtime(Date printDtime) {
		this.printDtime = printDtime;
	}

	public String getReprintUsername() {
		return reprintUsername;
	}

	public void setReprintUsername(String reprintUsername) {
		this.reprintUsername = reprintUsername;
	}

	public Date getReprintDtime() {
		return reprintDtime;
	}

	public void setReprintDtime(Date reprintDtime) {
		this.reprintDtime = reprintDtime;
	}



	public String getAuthorizeUsername() {
		return authorizeUsername;
	}

	public void setAuthorizeUsername(String authorizeUsername) {
		this.authorizeUsername = authorizeUsername;
	}

	public Date getAuthorizeDtime() {
		return authorizeDtime;
	}

	public void setAuthorizeDtime(Date authorizeDtime) {
		this.authorizeDtime = authorizeDtime;
	}

	
	public Date getOffenceDtime() {
		return offenceDtime;
	}

	public void setOffenceDtime(Date offenceDtime) {
		this.offenceDtime = offenceDtime;
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
	
	
	
	/**
	 * @return the status
	 */
	public StatusDO getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	
	

	/**
	 * @return the verificationReq
	 */
	public String getVerificationReq() {
		return verificationReq;
	}

	/**
	 * @param verificationReq the verificationReq to set
	 */
	public void setVerificationReq(String verificationReq) {
		this.verificationReq = verificationReq;
	}

	
	
	/**
	 * @return the authorizedFlag
	 */
	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	/**
	 * @param authorizedFlag the authorizedFlag to set
	 */
	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	

	/**
	 * @return the servedByTAStaff
	 */
	public TAStaffDO getServedByTAStaff() {
		return servedByTAStaff;
	}

	/**
	 * @param servedByTAStaff the servedByTAStaff to set
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

	public WarningNoProsecutionDO(WarningNoProsecutionBO warningNoProsecution) 
	{
	
		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.setRoadCheckOffenceOutcomeId( warningNoProsecution.getRoadCheckOffenceOutcomeCode());
		
		this.offender = new PersonDO();
		this.offender.setPersonId( warningNoProsecution.getOffenderId());
		
		this.taStaff = new TAStaffDO();
		this.taStaff.setStaffId( warningNoProsecution.getTaStaffId());
		
		this.manualSerialNumber =  StringUtil.isSet(warningNoProsecution.getManualSerialNo()) ? warningNoProsecution.getManualSerialNo().toUpperCase() 
				: warningNoProsecution.getManualSerialNo();
					
		this.status = new StatusDO();
		this.status.setStatusId( warningNoProsecution.getStatusId());		
		
		if(warningNoProsecution.getReasonCode() != null && warningNoProsecution.getReasonCode() > 0 )
		{
			this.reason =  new ReasonDO();
			this.reason.setReasonId( warningNoProsecution.getReasonCode());
		}
		
		this.comment =  warningNoProsecution.getComment();
		
		this.allowUpload = 'Y';
		
		this.printUsername = warningNoProsecution.getPrintUsername();
		
		this.printDtime =  warningNoProsecution.getPrintDtime();
		
		this.reprintUsername =  warningNoProsecution.getReprintUsername();
		
		this.reprintDtime =  warningNoProsecution.getReprintDtime();
		
		this.authorizedFlag =  warningNoProsecution.getAuthorizedFlag();
		
		this.authorizeUsername =  warningNoProsecution.getAuthorizeUsername();
		
	
		this.offenceDtime = warningNoProsecution.getOffenceDtime();
		
		this.allegation = warningNoProsecution.getAllegation();
		
		if(warningNoProsecution.getReferenceNo() == null)
			this.referenceNo = 0;
		
	}

	/**
	 * @return the referenceNo
	 */
	public Integer getReferenceNo() {
		return referenceNo;
	}

	/**
	 * @param referenceNo the referenceNo to set
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
	
	
}
