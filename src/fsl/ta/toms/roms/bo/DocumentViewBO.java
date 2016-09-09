package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.NameUtil;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * BASE CLASS FOR DOCUMENTS
 * 
 * @author jreid Created Date: Sep 30, 2013
 */
public class DocumentViewBO implements Serializable {

	private static final long serialVersionUID = -628951144396531086L;

	// operation
	String operationName;

	// document
	String documentTypeCode;
	String documentTypeName;
	Integer automaticSerialNo;
	String manualSerialNo;
	String referenceNo;
	String origin; // manual
					// or
					// automatic
	Date issueDate;
	String issueUsername;

	// road details
	Integer roadOperationId;
	Integer roadCheckOffenceOutcomeCode;

	// offender
	Integer offenderId;
	String offenderTRN;
	String offenderFirstName;
	String offenderLastName;
	String offenderMiddleName;
	String offenderFullName;
	String offenderFullNameNTRN;

	String offenderCellPhone;
	String offenderHomePhone;
	String offenderWorkPhone;
	String offenderRoleObserved;
	String offenderAddressWithNewLine;

	// ta staff
	String taStaffId;
	String taStaffFirstName;
	String taStaffLastName;
	String taStaffMiddleName;
	String taStaffFullName;

	// jp
	Integer jpIdNumber;
	String jpRegNumber;
	String jpFirstName;
	String jpLastName;
	String jpFullName;
	String jpParish;

	// offence
	Date offenceDtime;
	String offenceDtimeString;
	Integer offenceCode;
	String offenceLocation;
	String offenceDescription;
	String offenceParishCode;

	// court
	Integer courtCode;
	String courtName;
	String courtTime;
	Date courtDtime;
	Date initialCourtDtime;
	Integer initialCourtId;
	String initialCourtName;

	// document details
	String statusId;
	String statusDescription;

	// track changes
	Integer reasonCode;
	String comment;
	String newComment;

	// upload details
	String allowUpload;

	// print details
	String printUsername;
	Date printDtime;
	String reprintUsername;
	Date reprintDtime;
	String reprintComment;
	Integer reprintReasonCode;
	Integer printCount;

	// authorised details
	String authorizedFlag;
	String authorizeUsername;
	Date authorizeDtime;
	String verificationRequired;

	// served
	String servedByUserID;
	String servedByFirstName;
	String servedByLastName;
	Date servedOnDate;

	// vehicle details
	VehicleBO vehicle;

	// Road Licence
	RoadLicenceBO roadLicence;

	// vehicle owners
	List<VehicleOwnerBO> listOfVehicleOwners;

	// offences
	List<OffenceBO> listOfOffences;

	// List of documents uploaded that pertain to this item
	List<ScannedDocBO> scannedDocList;

	// List Of offences related to this document
	Integer complianceID;

	// user making update
	String currentUsername;

	// for update
	String newStatusId;

	// use to handle verification pop ups
	// the person who last updated should not be able to now update
	String lastUpdateUser;

	// authorisation
	boolean isSupervisorAuthorisationRequested;

	// selection
	boolean documentSelected;

	

	public DocumentViewBO() {
		super();
		scannedDocList = new ArrayList<ScannedDocBO>();
	}

	/**
	 * 
	 * @param summons
	 */
	public DocumentViewBO(SummonsDO summons) {
		super();
		this.documentTypeCode = Constants.DocumentType.SUMMONS;
		this.documentTypeName = Constants.DocumentType.SUMMONS_STRING;

		if (summons.getSummonsId() != null)
			this.automaticSerialNo = summons.getSummonsId();

		this.referenceNo = Constants.formatRefSeqNo(summons.getReferenceNo());

		this.origin = Constants.DocumentType.ORIGIN_AUTOMATIC;

		if (StringUtils.isNotBlank(summons.getManualSerialNumber())) {
			this.manualSerialNo = summons.getManualSerialNumber();
			this.origin = Constants.DocumentType.ORIGIN_MANUAL;
		}

		this.roadCheckOffenceOutcomeCode = summons.getRoadCheckOffenceOutcome()
				.getRoadCheckOffenceOutcomeId();

		if (summons.getOffender() != null) {
			this.offenderId = summons.getOffender().getPersonId();
			this.offenderLastName = summons.getOffender().getLastName();
			this.offenderFirstName = summons.getOffender().getFirstName();
			this.offenderMiddleName = summons.getOffender().getMiddleName();
			this.offenderTRN = summons.getOffender().getTrnNbr();

			this.offenderCellPhone = summons.getOffender().getTelephoneCell();
			this.offenderHomePhone = summons.getOffender().getTelephoneHome();
			this.offenderWorkPhone = summons.getOffender().getTelephoneWork();

			NameUtil util = new NameUtil();
			this.offenderFullName = util.getLastNameCapsFirstNameMiddleInitial(
					offenderFirstName, offenderLastName, offenderMiddleName);

		}

		if (summons.getOffender().getAddress() != null) {
			this.offenderAddressWithNewLine = summons.getOffender()
					.getAddress().getAddressLineWithNewLine();
			// System.err.println(" the offenders address "
			// +offenderAddressWithNewLine );
		}

		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getPersonRole() != null) {
			this.offenderRoleObserved = getRole(summons
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getPersonRole());
		}

		// set the vehicle details
		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getVehicle() != null) {
			this.vehicle = new VehicleBO(summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getVehicle());
			if(summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery() != null){
				this.offenceLocation = summons.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getCompliancyArtery().getShortDescription()
						+ ", "
						+ summons.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getShortDesc()
						+ ", "
						+ summons.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getParish().getDescription();
			}

		}

		if (summons.getTaStaff() != null) {
			this.taStaffId = summons.getTaStaff().getStaffId();
			this.taStaffLastName = summons.getTaStaff().getPerson()
					.getLastName();
			this.taStaffFirstName = summons.getTaStaff().getPerson()
					.getFirstName();
			this.taStaffMiddleName = summons.getTaStaff().getPerson()
					.getMiddleName();

			NameUtil util = new NameUtil();
			this.taStaffFullName = util.getLastNameCapsFirstNameMiddleInitial(
					taStaffFirstName, taStaffLastName, taStaffMiddleName);
		}

		if (summons.getJusticeOfPeace() != null) {
			this.setJpIdNumber(summons.getJusticeOfPeace().getIdNumber());
			//this.jpRegNumber = summons.getJusticeOfPeace().getRegNumber();
			if (summons.getJusticeOfPeace().getParish() != null) {
				this.jpParish = summons.getJusticeOfPeace().getParish()
						.getDescription();
			}
			this.jpFirstName = summons.getJusticeOfPeace().getPerson()
					.getFirstName();
			this.jpLastName = summons.getJusticeOfPeace().getPerson()
					.getLastName();
			NameUtil util = new NameUtil();
			this.jpFullName = util.getLastNameCapsFirstNameMiddleInitial(
					summons.getJusticeOfPeace().getPerson().getFirstName(),
					summons.getJusticeOfPeace().getPerson().getLastName(),
					summons.getJusticeOfPeace().getPerson().getMiddleName());
		}

		this.offenceDtime = summons.getOffenceDtime();
		if (summons.getOffenceDtime() != null) {
			this.offenceDtimeString = DateUtils.getFulldateformatter().format(
					summons.getOffenceDtime());
		}

		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getOffence() != null) {
			this.offenceCode = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getOffenceId();
			this.offenceDescription = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getDescription();
			
			if(summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery() != null){
				this.offenceParishCode = summons.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getCompliancyArtery().getLocation().getParish()
						.getParishCode();
			}
			
			if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence()
					.getOffenceId().equals(Constants.OffenceAidAndAbbet.NO_PPV_INSUR)
					|| summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence()
					.getOffenceId().equals(Constants.OffenceAidAndAbbet.NO_ROAD_LIC)) {
				this.offenderRoleObserved = "Owner";
			}
		}

		if (summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getRoadOperation() != null
				&& summons.getRoadCheckOffenceOutcome().getRoadCheckOffence()
						.getRoadCheck().getCompliance().getRoadOperation()
						.getOperationName() != null) {
			this.operationName = summons.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOperationName();
		}

		if (summons.getStatus() != null) {
			this.statusId = summons.getStatus().getStatusId();
			this.statusDescription = summons.getStatus().getDescription();
		}

		if (summons.getReason() != null)
			this.reasonCode = summons.getReason().getReasonId();

		if (StringUtils.isNotBlank(summons.getComment()))
			this.comment = StringUtils.replaceChars(summons.getComment(), ';',
					'\n');

		/*if (StringUtils.isNotBlank(summons.getReprintComment()))
			this.comment = comment
					+ '\n'
					+ "(Reprint Comments): "
					+ StringUtils.replaceChars(summons.getReprintComment(),
							';', '\n');*/

		if (summons.getServedByTAStaff() != null) {
			this.servedByUserID = summons.getServedByTAStaff()
					.getLoginUsername();
			this.servedByFirstName = (StringUtils.isNotBlank(summons
					.getServedByTAStaff().getPerson().getFirstName()) ? WordUtils
					.capitalizeFully(summons.getServedByTAStaff().getPerson()
							.getFirstName(), Constants.STRING_DELIM_ARRAY) : "")
					+ " "
					+ (StringUtils.isNotBlank(summons.getServedByTAStaff()
							.getPerson().getMiddleName()) ? summons
							.getServedByTAStaff().getPerson().getMiddleName()
							.trim().substring(0, 1) : "")
					+ (StringUtils.isNotBlank(summons.getServedByTAStaff()
							.getStaffId()) ? " ["
							+ summons.getServedByTAStaff().getStaffId() + "]"
							: "");
			this.servedByLastName = summons.getServedByTAStaff().getPerson()
					.getLastName().toUpperCase();
		}

		if (summons.getServedOnDate() != null)
			this.servedOnDate = new Date(summons.getServedOnDate().getTime());

		if (summons.getAuditEntry().getCreateDTime() != null)
			this.issueDate = new Date(summons.getAuditEntry().getCreateDTime()
					.getTime());

		if (summons.getAuditEntry().getCreateUsername() != null)
			this.issueUsername = summons.getAuditEntry().getCreateUsername();

		this.authorizedFlag = summons.getAuthorizedFlag();
		this.authorizeDtime = summons.getAuthorizeDtime();
		this.authorizeUsername = summons.getAuthorizeUsername();

		this.verificationRequired = summons.getVerificationReq();

		this.printUsername = summons.getPrintUsername();
		this.printDtime = summons.getPrintDtime();
		this.printCount = summons.getPrintCount();
		this.reprintUsername = summons.getReprintUsername();
		this.reprintDtime = summons.getReprintDtime();

		if (summons.getAllowUpload() != null)
			this.allowUpload = summons.getAllowUpload().toString();

		if (StringUtils.isBlank(summons.getAuditEntry().getUpdateUsername())) {
			this.lastUpdateUser = summons.getAuditEntry().getCreateUsername();
		} else {
			this.lastUpdateUser = summons.getAuditEntry().getUpdateUsername();
		}
		
		if(summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().
				getRoadCheck().getCompliance().getCourt() != null){
			this.initialCourtId = summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().
					getRoadCheck().getCompliance().getCourt().getCourtId();
			
			this.initialCourtName = summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().
					getRoadCheck().getCompliance().getCourt().getShortDesc();
		}
		
		if(summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().
				getRoadCheck().getCompliance().getCourtDateTime() != null){
			this.initialCourtDtime = summons.getRoadCheckOffenceOutcome().getRoadCheckOffence().
				getRoadCheck().getCompliance().getCourtDateTime();
		}
		
	}

	public DocumentViewBO(WarningNoticeDO warningNotice) {
		super();
		this.documentTypeName = Constants.DocumentType.WARNING_NOTICE_STRING;
		this.documentTypeCode = Constants.DocumentType.WARNING_NOTICE;

		if (warningNotice.getWarningNoticeId() != null)
			this.automaticSerialNo = warningNotice.getWarningNoticeId();

		this.referenceNo = Constants.formatRefSeqNo(warningNotice.getReferenceNo());

		this.origin = Constants.DocumentType.ORIGIN_AUTOMATIC;

		if (StringUtils.isNotBlank(warningNotice.getManualSerialNumber())) {
			this.manualSerialNo = warningNotice.getManualSerialNumber();
			this.origin = Constants.DocumentType.ORIGIN_MANUAL;
		}

		this.roadCheckOffenceOutcomeCode = warningNotice
				.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId();

		if (warningNotice.getOffender() != null) {
			this.offenderId = warningNotice.getOffender().getPersonId();
			this.offenderLastName = warningNotice.getOffender().getLastName();
			this.offenderFirstName = warningNotice.getOffender().getFirstName();
			this.offenderMiddleName = warningNotice.getOffender()
					.getMiddleName();
			this.offenderTRN = warningNotice.getOffender().getTrnNbr();

			this.offenderCellPhone = warningNotice.getOffender()
					.getTelephoneCell();
			this.offenderHomePhone = warningNotice.getOffender()
					.getTelephoneHome();
			this.offenderWorkPhone = warningNotice.getOffender()
					.getTelephoneWork();

			NameUtil util = new NameUtil();
			this.offenderFullName = util.getLastNameCapsFirstNameMiddleInitial(
					offenderFirstName, offenderLastName, offenderMiddleName);
		}

		if (warningNotice.getOffender().getAddress() != null) {
			this.offenderAddressWithNewLine = warningNotice.getOffender()
					.getAddress().getAddressLineWithNewLine();
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getPersonRole() != null) {
			this.offenderRoleObserved = getRole(warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getPersonRole());
		}

		if (warningNotice.getTaStaff() != null) {
			this.taStaffId = warningNotice.getTaStaff().getStaffId();
			this.taStaffLastName = warningNotice.getTaStaff().getPerson()
					.getLastName().toUpperCase();
			this.taStaffFirstName = warningNotice.getTaStaff().getPerson()
					.getFirstName();
			this.taStaffMiddleName = warningNotice.getTaStaff().getPerson()
					.getMiddleName();

			NameUtil util = new NameUtil();
			this.taStaffFullName = util.getLastNameCapsFirstNameMiddleInitial(
					taStaffFirstName, taStaffLastName, taStaffMiddleName);
		}

		// set the vehicle details
		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getVehicle() != null) {
			this.vehicle = new VehicleBO(warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getVehicle());
			if(warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery()!= null){
				this.offenceLocation = warningNotice.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getCompliancyArtery().getShortDescription()
						+ ", "
						+ warningNotice.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getShortDesc()
						+ ", "
						+ warningNotice.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getParish().getDescription();
				
				this.offenceParishCode = warningNotice
						.getRoadCheckOffenceOutcome().getRoadCheckOffence()
						.getRoadCheck().getCompliance().getCompliancyArtery()
						.getLocation().getParish().getParishCode();
			}
		}

		this.offenceDtime = warningNotice.getRoadCheckOffenceOutcome()
				.getRoadCheckOffence().getRoadCheck().getCompliance()
				.getOffenceDateTime();
		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getOffenceDateTime() != null) {
			this.offenceDtimeString = DateUtils.getFulldateformatter().format(
					warningNotice.getRoadCheckOffenceOutcome()
							.getRoadCheckOffence().getRoadCheck()
							.getCompliance().getOffenceDateTime());
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getOffence() != null) {
			this.offenceCode = warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getOffenceId();
			this.offenceDescription = warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getOffence().getDescription();
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getRoadOperation() != null
				&& warningNotice.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getRoadOperation().getOperationName() != null)
			this.operationName = warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOperationName();

		if (warningNotice.getStatus() != null)

		{
			this.statusId = warningNotice.getStatus().getStatusId();
			this.statusDescription = warningNotice.getStatus().getDescription();
		}

		if (warningNotice.getReason() != null)
			this.reasonCode = warningNotice.getReason().getReasonId();

		if (StringUtils.isNotBlank(warningNotice.getComment()))
			this.comment = StringUtils.replaceChars(warningNotice.getComment(),
					';', '\n');

		/*if (StringUtils.isNotBlank(warningNotice.getReprintComment()))
			this.comment = comment
					+ '\n'
					+ "(Reprint Comments): "
					+ StringUtils.replaceChars(
							warningNotice.getReprintComment(), ';', '\n');*/

		if (warningNotice.getAuditEntry().getCreateDTime() != null)
			this.issueDate = new Date(warningNotice.getAuditEntry()
					.getCreateDTime().getTime());

		if (warningNotice.getAuditEntry().getCreateUsername() != null)
			this.issueUsername = warningNotice.getAuditEntry()
					.getCreateUsername();

		this.authorizedFlag = warningNotice.getAuthorizedFlag();
		this.authorizeDtime = warningNotice.getAuthorizeDtime();
		this.authorizeUsername = warningNotice.getAuthorizeUsername();

		this.verificationRequired = warningNotice.getVerificationReq();

		this.printUsername = warningNotice.getPrintUsername();
		this.printDtime = warningNotice.getPrintDtime();
		this.reprintUsername = warningNotice.getReprintUsername();
		this.reprintDtime = warningNotice.getReprintDtime();
		this.printCount = warningNotice.getPrintCount();

		if (warningNotice.getAllowUpload() != null)
			this.allowUpload = warningNotice.getAllowUpload().toString();

		if (warningNotice.getServedByTAStaff() != null) {
			this.servedByUserID = warningNotice.getServedByTAStaff()
					.getLoginUsername();
			this.servedByFirstName = (StringUtils.isNotBlank(warningNotice
					.getServedByTAStaff().getPerson().getFirstName()) ? WordUtils
					.capitalizeFully(warningNotice.getServedByTAStaff()
							.getPerson().getFirstName(),
							Constants.STRING_DELIM_ARRAY) : "")
					+ " "
					+ (StringUtils.isNotBlank(warningNotice
							.getServedByTAStaff().getPerson().getMiddleName()) ? warningNotice
							.getServedByTAStaff().getPerson().getMiddleName()
							.trim().substring(0, 1)
							: "")
					+ (StringUtils.isNotBlank(warningNotice
							.getServedByTAStaff().getStaffId()) ? " ["
							+ warningNotice.getServedByTAStaff().getStaffId()
							+ "]" : "");
			this.servedByLastName = warningNotice.getServedByTAStaff()
					.getPerson().getLastName().toUpperCase();
		}

		this.servedOnDate = warningNotice.getServedOnDate();

		if (StringUtils.isBlank(warningNotice.getAuditEntry()
				.getUpdateUsername())) {
			this.lastUpdateUser = warningNotice.getAuditEntry()
					.getCreateUsername();
		} else {
			this.lastUpdateUser = warningNotice.getAuditEntry()
					.getUpdateUsername();
		}

	}

	public DocumentViewBO(WarningNoProsecutionDO warningNotice) {
		super();
		this.documentTypeName = Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION_STRING;
		this.documentTypeCode = Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION;

		if (warningNotice.getWarningNoProsecutionID() != null)
			this.automaticSerialNo = warningNotice.getWarningNoProsecutionID();

		this.referenceNo = Constants.formatRefSeqNo(warningNotice.getReferenceNo());

		this.origin = Constants.DocumentType.ORIGIN_AUTOMATIC;

		if (StringUtils.isNotBlank(warningNotice.getManualSerialNumber())) {
			this.manualSerialNo = warningNotice.getManualSerialNumber();
			this.origin = Constants.DocumentType.ORIGIN_MANUAL;
		}

		this.roadCheckOffenceOutcomeCode = warningNotice
				.getRoadCheckOffenceOutcome().getRoadCheckOffenceOutcomeId();

		if (warningNotice.getOffender() != null) {
			this.offenderId = warningNotice.getOffender().getPersonId();
			this.offenderLastName = warningNotice.getOffender().getLastName()
					.toUpperCase();
			this.offenderFirstName = warningNotice.getOffender().getFirstName();
			this.offenderMiddleName = warningNotice.getOffender()
					.getMiddleName();
			this.offenderTRN = warningNotice.getOffender().getTrnNbr();

			this.offenderCellPhone = warningNotice.getOffender()
					.getTelephoneCell();
			this.offenderHomePhone = warningNotice.getOffender()
					.getTelephoneHome();
			this.offenderWorkPhone = warningNotice.getOffender()
					.getTelephoneWork();

			NameUtil util = new NameUtil();
			this.offenderFullName = util.getLastNameCapsFirstNameMiddleInitial(
					offenderFirstName, offenderLastName, offenderMiddleName);
		}

		if (warningNotice.getOffender().getAddress() != null) {
			this.offenderAddressWithNewLine = warningNotice.getOffender()
					.getAddress().getAddressLineWithNewLine();
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getPersonRole() != null) {
			this.offenderRoleObserved = getRole(warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getPersonRole());
		}
		if (warningNotice.getTaStaff() != null) {
			this.taStaffId = warningNotice.getTaStaff().getStaffId();
			this.taStaffLastName = warningNotice.getTaStaff().getPerson()
					.getLastName().toUpperCase();
			this.taStaffFirstName = warningNotice.getTaStaff().getPerson()
					.getFirstName();
			this.taStaffMiddleName = warningNotice.getTaStaff().getPerson()
					.getMiddleName();

			NameUtil util = new NameUtil();
			this.taStaffFullName = util.getLastNameCapsFirstNameMiddleInitial(
					taStaffFirstName, taStaffLastName, taStaffMiddleName);
		}

		this.offenceDtime = warningNotice.getOffenceDtime();
		if (warningNotice.getOffenceDtime() != null) {
			this.offenceDtimeString = DateUtils.getFulldateformatter().format(
					warningNotice.getOffenceDtime());
		}

		// set the vehicle details
		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getVehicle() != null) {
			this.vehicle = new VehicleBO(warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getRoadCheck().getCompliance().getVehicle());
			
			
			if(warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getCompliancyArtery() != null){
				this.offenceLocation = warningNotice.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getCompliancyArtery().getShortDescription()
						+ ", "
						+ warningNotice.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getShortDesc()
						+ ", "
						+ warningNotice.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getCompliancyArtery()
								.getLocation().getParish().getDescription();
			
			
				this.offenceParishCode = warningNotice.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getCompliancyArtery().getLocation().getParish()
						.getParishCode();
			}
			
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getOffence() != null) {
			this.offenceCode = warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getOffence().getOffenceId();

			this.offenceDescription = warningNotice
					.getRoadCheckOffenceOutcome().getRoadCheckOffence()
					.getOffence().getDescription();
		}

		if (warningNotice.getRoadCheckOffenceOutcome().getRoadCheckOffence()
				.getRoadCheck().getCompliance().getRoadOperation() != null
				&& warningNotice.getRoadCheckOffenceOutcome()
						.getRoadCheckOffence().getRoadCheck().getCompliance()
						.getRoadOperation().getOperationName() != null)
			this.operationName = warningNotice.getRoadCheckOffenceOutcome()
					.getRoadCheckOffence().getRoadCheck().getCompliance()
					.getRoadOperation().getOperationName();

		if (warningNotice.getStatus() != null)

		{
			this.statusId = warningNotice.getStatus().getStatusId();
			this.statusDescription = warningNotice.getStatus().getDescription();
		}

		if (warningNotice.getReason() != null)
			this.reasonCode = warningNotice.getReason().getReasonId();

		if (StringUtils.isNotBlank(warningNotice.getComment()))
			this.comment = StringUtils.replaceChars(warningNotice.getComment(),
					';', '\n');

		/*if (StringUtils.isNotBlank(warningNotice.getReprintComment()))
			this.comment = comment
					+ '\n'
					+ "(Reprint Comments): "
					+ StringUtils.replaceChars(
							warningNotice.getReprintComment(), ';', '\n');*/

		if (warningNotice.getAuditEntry().getCreateDTime() != null)
			this.issueDate = new Date(warningNotice.getAuditEntry()
					.getCreateDTime().getTime());

		if (warningNotice.getAuditEntry().getCreateUsername() != null)
			this.issueUsername = warningNotice.getAuditEntry()
					.getCreateUsername();

		this.authorizedFlag = warningNotice.getAuthorizedFlag();
		this.authorizeDtime = warningNotice.getAuthorizeDtime();
		this.authorizeUsername = warningNotice.getAuthorizeUsername();

		this.verificationRequired = warningNotice.getVerificationReq();

		this.printUsername = warningNotice.getPrintUsername();
		this.printDtime = warningNotice.getPrintDtime();
		this.reprintUsername = warningNotice.getReprintUsername();
		this.reprintDtime = warningNotice.getReprintDtime();
		this.printCount = warningNotice.getPrintCount();

		if (warningNotice.getAllowUpload() != null)
			this.allowUpload = warningNotice.getAllowUpload().toString();

		if (warningNotice.getServedByTAStaff() != null) {
			this.servedByUserID = warningNotice.getServedByTAStaff()
					.getLoginUsername();
			this.servedByFirstName = (StringUtils.isNotBlank(warningNotice
					.getServedByTAStaff().getPerson().getFirstName()) ? WordUtils
					.capitalizeFully(warningNotice.getServedByTAStaff()
							.getPerson().getFirstName(),
							Constants.STRING_DELIM_ARRAY) : "")
					+ " "
					+ (StringUtils.isNotBlank(warningNotice
							.getServedByTAStaff().getPerson().getMiddleName()) ? warningNotice
							.getServedByTAStaff().getPerson().getMiddleName()
							.trim().substring(0, 1)
							: "")
					+ (StringUtils.isNotBlank(warningNotice
							.getServedByTAStaff().getStaffId()) ? " ["
							+ warningNotice.getServedByTAStaff().getStaffId()
							+ "]" : "");
			this.servedByLastName = warningNotice.getServedByTAStaff()
					.getPerson().getLastName().toUpperCase();
		}

		this.servedOnDate = warningNotice.getServedOnDate();

		if (StringUtils.isBlank(warningNotice.getAuditEntry()
				.getUpdateUsername())) {
			this.lastUpdateUser = warningNotice.getAuditEntry()
					.getCreateUsername();
		} else {
			this.lastUpdateUser = warningNotice.getAuditEntry()
					.getUpdateUsername();
		}

	}

	public DocumentViewBO(String documentTypeCode, String documentTypeName,
			Integer automaticSerialNo, String manualSerialNo, String origin,
			Integer complianceID, String statusId, String statusDescription,
			String printUsername, Date printDtime, String reprintUsername,
			Date reprintDtime, Integer printCount) {
		super();

		this.documentTypeCode = documentTypeCode;
		this.documentTypeName = documentTypeName;
		this.automaticSerialNo = automaticSerialNo;
		this.manualSerialNo = manualSerialNo;
		this.origin = origin;
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		this.printCount = printCount;

	}

	public DocumentViewBO(String documentTypeCode, String documentTypeName,
			Integer automaticSerialNo, String manualSerialNo, String origin,
			Integer complianceID, String statusId, String statusDescription,
			String printUsername, Date printDtime, String reprintUsername,
			Date reprintDtime, Integer printCount, String verificationReq) {
		super();

		this.documentTypeCode = documentTypeCode;
		this.documentTypeName = documentTypeName;
		this.automaticSerialNo = automaticSerialNo;
		this.manualSerialNo = manualSerialNo;
		this.origin = origin;
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		this.printCount = printCount;
		this.verificationRequired = verificationReq;

	}

	public DocumentViewBO(String documentTypeCode, String documentTypeName,
			Integer automaticSerialNo, String manualSerialNo, String origin,
			Integer complianceID, String statusId, String statusDescription,
			String printUsername, Date printDtime, String reprintUsername,
			Date reprintDtime, Integer printCount, Integer jpIdNumber,
			String jpFirstName, String jpLastName) {
		super();

		this.documentTypeCode = documentTypeCode;
		this.documentTypeName = documentTypeName;
		this.automaticSerialNo = automaticSerialNo;
		this.manualSerialNo = manualSerialNo;
		this.origin = origin;
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		this.printCount = printCount;
		//this.jpRegNumber = jpRegNumber;
		this.jpIdNumber = jpIdNumber;
		this.jpFirstName = jpFirstName;
		this.jpLastName = jpLastName;

	}

	public DocumentViewBO(String documentTypeCode, String documentTypeName,
			Integer automaticSerialNo, String manualSerialNo, String origin,
			Integer complianceID, String statusId, String statusDescription,
			String printUsername, Date printDtime, String reprintUsername,
			Date reprintDtime, Integer printCount, Integer jpIdNumber,
			String jpFirstName, String jpLastName, String verificationReq) {
		super();

		this.documentTypeCode = documentTypeCode;
		this.documentTypeName = documentTypeName;
		this.automaticSerialNo = automaticSerialNo;
		this.manualSerialNo = manualSerialNo;
		this.origin = origin;
		this.statusId = statusId;
		this.statusDescription = statusDescription;
		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		this.printCount = printCount;
		this.jpIdNumber = jpIdNumber;
		//this.jpRegNumber = jpRegNumber;
		this.jpFirstName = jpFirstName;
		this.jpLastName = jpLastName;
		this.verificationRequired = verificationReq;

	}

	/**
	 * 
	 * @param roleID
	 * @return
	 */
	private String getRole(String roleID) {
		if (StringUtils.isNotBlank(roleID)) {
			if (roleID.equalsIgnoreCase(Constants.PersonRole.CONDUCTOR)) {
				return Constants.PersonRole.CONDUCTOR_STRING;
			}

			if (roleID.equalsIgnoreCase(Constants.PersonRole.DRIVER)) {
				return Constants.PersonRole.DRIVER_STRING;

			}

			if (roleID.equalsIgnoreCase(Constants.PersonRole.OWNER)) {
				return Constants.PersonRole.OWNER_STRING;

			}
		}
		return "";
	}

	/**
	 * @return the roadOperationId
	 */
	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	/**
	 * @param roadOperationId
	 *            the roadOperationId to set
	 */
	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	/**
	 * @return the roadCheckOffenceOutcomeCode
	 */
	public Integer getRoadCheckOffenceOutcomeCode() {
		return roadCheckOffenceOutcomeCode;
	}

	/**
	 * @param roadCheckOffenceOutcomeCode
	 *            the roadCheckOffenceOutcomeCode to set
	 */
	public void setRoadCheckOffenceOutcomeCode(
			Integer roadCheckOffenceOutcomeCode) {
		this.roadCheckOffenceOutcomeCode = roadCheckOffenceOutcomeCode;
	}

	/**
	 * @return the offenderId
	 */
	public Integer getOffenderId() {
		return offenderId;
	}

	/**
	 * @param offenderId
	 *            the offenderId to set
	 */
	public void setOffenderId(Integer offenderId) {
		this.offenderId = offenderId;
	}

	/**
	 * @return the offenderTRN
	 */
	public String getOffenderTRN() {
		return offenderTRN;
	}

	/**
	 * @param offenderTRN
	 *            the offenderTRN to set
	 */
	public void setOffenderTRN(String offenderTRN) {
		this.offenderTRN = offenderTRN;
	}

	/**
	 * @return the jpParish
	 */
	public String getJpParish() {
		return jpParish;
	}

	/**
	 * @param jpParish
	 *            the jpParish to set
	 */
	public void setJpParish(String jpParish) {
		this.jpParish = jpParish;
	}

	/**
	 * @return the offenderFirstName
	 */
	public String getOffenderFirstName() {
		return offenderFirstName;
	}

	/**
	 * @param offenderFirstName
	 *            the offenderFirstName to set
	 */
	public void setOffenderFirstName(String offenderFirstName) {
		this.offenderFirstName = offenderFirstName;
	}

	/**
	 * @return the offenderLastName
	 */
	public String getOffenderLastName() {
		return offenderLastName;
	}

	/**
	 * @param offenderLastName
	 *            the offenderLastName to set
	 */
	public void setOffenderLastName(String offenderLastName) {
		this.offenderLastName = offenderLastName;
	}

	/**
	 * @return the taStaffId
	 */
	public String getTaStaffId() {
		return taStaffId;
	}

	/**
	 * @param taStaffId
	 *            the taStaffId to set
	 */
	public void setTaStaffId(String taStaffId) {
		this.taStaffId = taStaffId;
	}

	/**
	 * @return the taStaffFirstName
	 */
	public String getTaStaffFirstName() {
		return taStaffFirstName;
	}

	/**
	 * @param taStaffFirstName
	 *            the taStaffFirstName to set
	 */
	public void setTaStaffFirstName(String taStaffFirstName) {
		this.taStaffFirstName = taStaffFirstName;
	}

	/**
	 * @return the taStaffLastName
	 */
	public String getTaStaffLastName() {
		return taStaffLastName;
	}

	/**
	 * @param taStaffLastName
	 *            the taStaffLastName to set
	 */
	public void setTaStaffLastName(String taStaffLastName) {
		this.taStaffLastName = taStaffLastName;
	}

	/**
	 * @return the jpRegNumber
	 */
	/*public String getJpRegNumber() {
		return jpRegNumber;
	}*/

	/**
	 * @param jpRegNumber
	 *            the jpRegNumber to set
	 */
	/*public void setJpRegNumber(String jpRegNumber) {
		this.jpRegNumber = jpRegNumber;
	}*/

	/**
	 * @return the jpFirstName
	 */
	public String getJpFirstName() {
		return jpFirstName;
	}

	/**
	 * @param jpFirstName
	 *            the jpFirstName to set
	 */
	public void setJpFirstName(String jpFirstName) {
		this.jpFirstName = jpFirstName;
	}

	/**
	 * @return the jpLastName
	 */
	public String getJpLastName() {
		return jpLastName;
	}

	/**
	 * @param jpLastName
	 *            the jpLastName to set
	 */
	public void setJpLastName(String jpLastName) {
		this.jpLastName = jpLastName;
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
	 * @return the offenceCode
	 */
	public Integer getOffenceCode() {
		return offenceCode;
	}

	/**
	 * @param offenceCode
	 *            the offenceCode to set
	 */
	public void setOffenceCode(Integer offenceCode) {
		this.offenceCode = offenceCode;
	}

	/**
	 * @return the offenceLocation
	 */
	public String getOffenceLocation() {
		return offenceLocation;
	}

	/**
	 * @param offenceLocation
	 *            the offenceLocation to set
	 */
	public void setOffenceLocation(String offenceLocation) {
		this.offenceLocation = offenceLocation;
	}

	/**
	 * @return the courtCode
	 */
	public Integer getCourtCode() {
		return courtCode;
	}

	/**
	 * @param courtCode
	 *            the courtCode to set
	 */
	public void setCourtCode(Integer courtCode) {
		this.courtCode = courtCode;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName
	 *            the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the courtTime
	 */
	public String getCourtTime() {
		return courtTime;
	}

	/**
	 * @param courtTime
	 *            the courtTime to set
	 */
	public void setCourtTime(String courtTime) {
		this.courtTime = courtTime;
	}

	/**
	 * @return the courtDtime
	 */
	public Date getCourtDtime() {
		return courtDtime;
	}

	/**
	 * @param courtDtime
	 *            the courtDtime to set
	 */
	public void setCourtDtime(Date courtDtime) {
		this.courtDtime = courtDtime;
	}
	
	/**
	 * @return the initialCourtDtime
	 */
	public Date getInitialCourtDtime() {
		return initialCourtDtime;
	}

	/**
	 * @param initialCourtDtime the initialCourtDtime to set
	 */
	public void setInitialCourtDtime(Date initialCourtDtime) {
		this.initialCourtDtime = initialCourtDtime;
	}

	/**
	 * @return the statusId
	 */
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId
	 *            the statusId to set
	 */
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param statusDescription
	 *            the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * @return the reasonCode
	 */
	public Integer getReasonCode() {
		return reasonCode;
	}

	/**
	 * @param reasonCode
	 *            the reasonCode to set
	 */
	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
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
	public String getAllowUpload() {
		return allowUpload;
	}

	/**
	 * @param allowUpload
	 *            the allowUpload to set
	 */
	public void setAllowUpload(String allowUpload) {
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
	 * @return the verificationRequired
	 */
	public String getVerificationRequired() {
		return verificationRequired;
	}

	/**
	 * @param verificationRequired
	 *            the verificationRequired to set
	 */
	public void setVerificationRequired(String verificationRequired) {
		this.verificationRequired = verificationRequired;
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
	 * @return the scannedDocList
	 */
	public List<ScannedDocBO> getScannedDocList() {
		return scannedDocList;
	}

	/**
	 * @param scannedDocList
	 *            the scannedDocList to set
	 */
	public void setScannedDocList(List<ScannedDocBO> scannedDocList) {
		this.scannedDocList = scannedDocList;
	}

	/**
	 * @return the currentUsername
	 */
	public String getCurrentUsername() {
		return currentUsername;
	}

	/**
	 * @param currentUsername
	 *            the currentUsername to set
	 */
	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
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

	/**
	 * @return the documentTypeCode
	 */
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	/**
	 * @param documentTypeCode
	 *            the documentTypeCode to set
	 */
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	/**
	 * @return the documentTypeName
	 */
	public String getDocumentTypeName() {
		return documentTypeName;
	}

	/**
	 * @param documentTypeName
	 *            the documentTypeName to set
	 */
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	/**
	 * @return the automaticSerialNo
	 */
	public Integer getAutomaticSerialNo() {
		return automaticSerialNo;
	}

	/**
	 * @param automaticSerialNo
	 *            the automaticSerialNo to set
	 */
	public void setAutomaticSerialNo(Integer automaticSerialNo) {
		this.automaticSerialNo = automaticSerialNo;
	}

	/**
	 * @return the manualSerialNo
	 */
	public String getManualSerialNo() {
		return manualSerialNo;
	}

	/**
	 * @param manualSerialNo
	 *            the manualSerialNo to set
	 */
	public void setManualSerialNo(String manualSerialNo) {
		this.manualSerialNo = manualSerialNo;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the servedByUserID
	 */
	public String getServedByUserID() {
		return servedByUserID;
	}

	/**
	 * @param servedByUserID
	 *            the servedByUserID to set
	 */
	public void setServedByUserID(String servedByUserID) {
		this.servedByUserID = servedByUserID;
	}

	/**
	 * @return the servedByFirstName
	 */
	public String getServedByFirstName() {
		return servedByFirstName;
	}

	/**
	 * @param servedByFirstName
	 *            the servedByFirstName to set
	 */
	public void setServedByFirstName(String servedByFirstName) {
		this.servedByFirstName = servedByFirstName;
	}

	/**
	 * @return the servedByLastName
	 */
	public String getServedByLastName() {
		return servedByLastName;
	}

	/**
	 * @param servedByLastName
	 *            the servedByLastName to set
	 */
	public void setServedByLastName(String servedByLastName) {
		this.servedByLastName = servedByLastName;
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
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * @param operationName
	 *            the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * @return the offenderFullName
	 */
	public String getOffenderFullName() {
		return offenderFullName;
	}

	/**
	 * @param offenderFullName
	 *            the offenderFullName to set
	 */
	public void setOffenderFullName(String offenderFullName) {
		this.offenderFullName = offenderFullName;
	}

	/**
	 * @return the offenderFullNameNTRN
	 */
	public String getOffenderFullNameNTRN() {
		return offenderFullNameNTRN;
	}

	/**
	 * @param offenderFullNameNTRN
	 *            the offenderFullNameNTRN to set
	 */
	public void setOffenderFullNameNTRN(String offenderFullNameNTRN) {
		this.offenderFullNameNTRN = offenderFullNameNTRN;
	}

	/**
	 * @return the taStaffFullName
	 */
	public String getTaStaffFullName() {
		return taStaffFullName;
	}

	/**
	 * @param taStaffFullName
	 *            the taStaffFullName to set
	 */
	public void setTaStaffFullName(String taStaffFullName) {
		this.taStaffFullName = taStaffFullName;
	}

	/**
	 * @return the jpFullName
	 */
	public String getJpFullName() {
		return jpFullName;
	}

	/**
	 * @param jpFullName
	 *            the jpFullName to set
	 */
	public void setJpFullName(String jpFullName) {
		this.jpFullName = jpFullName;
	}

	public Integer getComplianceID() {
		return complianceID;
	}

	public void setComplianceID(Integer complianceID) {
		this.complianceID = complianceID;
	}

	/**
	 * @return the offenderCellPhone
	 */
	public String getOffenderCellPhone() {
		return offenderCellPhone;
	}

	/**
	 * @param offenderCellPhone
	 *            the offenderCellPhone to set
	 */
	public void setOffenderCellPhone(String offenderCellPhone) {
		this.offenderCellPhone = offenderCellPhone;
	}

	/**
	 * @return the offenderHomePhone
	 */
	public String getOffenderHomePhone() {
		return offenderHomePhone;
	}

	/**
	 * @param offenderHomePhone
	 *            the offenderHomePhone to set
	 */
	public void setOffenderHomePhone(String offenderHomePhone) {
		this.offenderHomePhone = offenderHomePhone;
	}

	/**
	 * @return the offenderWorkPhone
	 */
	public String getOffenderWorkPhone() {
		return offenderWorkPhone;
	}

	/**
	 * @param offenderWorkPhone
	 *            the offenderWorkPhone to set
	 */
	public void setOffenderWorkPhone(String offenderWorkPhone) {
		this.offenderWorkPhone = offenderWorkPhone;
	}

	/**
	 * @return the offenderRoleObserved
	 */
	public String getOffenderRoleObserved() {
		return offenderRoleObserved;
	}

	/**
	 * @param offenderRoleObserved
	 *            the offenderRoleObserved to set
	 */
	public void setOffenderRoleObserved(String offenderRoleObserved) {
		this.offenderRoleObserved = offenderRoleObserved;
	}

	/**
	 * @return the offenderAddressWithNewLine
	 */
	public String getOffenderAddressWithNewLine() {
		return offenderAddressWithNewLine;
	}

	/**
	 * @param offenderAddressWithNewLine
	 *            the offenderAddressWithNewLine to set
	 */
	public void setOffenderAddressWithNewLine(String offenderAddressWithNewLine) {
		this.offenderAddressWithNewLine = offenderAddressWithNewLine;
	}

	/**
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate
	 *            the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the offenceDescription
	 */
	public String getOffenceDescription() {
		return offenceDescription;
	}

	/**
	 * @param offenceDescription
	 *            the offenceDescription to set
	 */
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

	/**
	 * @return the newStatusId
	 */
	public String getNewStatusId() {
		return newStatusId;
	}

	/**
	 * @param newStatusId
	 *            the newStatusId to set
	 */
	public void setNewStatusId(String newStatusId) {
		this.newStatusId = newStatusId;
	}

	public boolean isSupervisorAuthorisationRequested() {
		return isSupervisorAuthorisationRequested;
	}

	public void setSupervisorAuthorisationRequested(
			boolean isSupervisorAuthorisationRequested) {
		this.isSupervisorAuthorisationRequested = isSupervisorAuthorisationRequested;
	}

	/**
	 * @return the roadLicence
	 */
	public RoadLicenceBO getRoadLicence() {
		return roadLicence;
	}

	/**
	 * @param roadLicence
	 *            the roadLicence to set
	 */
	public void setRoadLicence(RoadLicenceBO roadLicence) {
		this.roadLicence = roadLicence;
	}

	/**
	 * @return the vehicle
	 */
	public VehicleBO getVehicle() {
		return vehicle;
	}

	/**
	 * @param vehicle
	 *            the vehicle to set
	 */
	public void setVehicle(VehicleBO vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * @return the issueUsername
	 */
	public String getIssueUsername() {
		return issueUsername;
	}

	/**
	 * @param issueUsername
	 *            the issueUsername to set
	 */
	public void setIssueUsername(String issueUsername) {
		this.issueUsername = issueUsername;
	}

	/**
	 * @return the newComment
	 */
	public String getNewComment() {
		return newComment;
	}

	/**
	 * @param newComment
	 *            the newComment to set
	 */
	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	/**
	 * @return the listOfOffences
	 */
	public List<OffenceBO> getListOfOffences() {
		return listOfOffences;
	}

	/**
	 * @param listOfOffences
	 *            the listOfOffences to set
	 */
	public void setListOfOffences(List<OffenceBO> listOfOffences) {
		this.listOfOffences = listOfOffences;
	}

	/**
	 * @return the lastUpdateUser
	 */
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * @param lastUpdateUser
	 *            the lastUpdateUser to set
	 */
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	/**
	 * @return the offenceDtimeString
	 */
	public String getOffenceDtimeString() {
		return offenceDtimeString;
	}

	/**
	 * @param offenceDtimeString
	 *            the offenceDtimeString to set
	 */
	public void setOffenceDtimeString(String offenceDtimeString) {
		this.offenceDtimeString = offenceDtimeString;
	}

	/**
	 * @return the reprintReasonCode
	 */
	public Integer getReprintReasonCode() {
		return reprintReasonCode;
	}

	/**
	 * @param reprintReasonCode
	 *            the reprintReasonCode to set
	 */
	public void setReprintReasonCode(Integer reprintReasonCode) {
		this.reprintReasonCode = reprintReasonCode;
	}

	/**
	 * @return the listOfVehicleOwners
	 */
	public List<VehicleOwnerBO> getListOfVehicleOwners() {
		return listOfVehicleOwners;
	}

	/**
	 * @param listOfVehicleOwners
	 *            the listOfVehicleOwners to set
	 */
	public void setListOfVehicleOwners(List<VehicleOwnerBO> listOfVehicleOwners) {
		this.listOfVehicleOwners = listOfVehicleOwners;
	}

	/**
	 * @return the referenceNo
	 */
	public String getReferenceNo() {
		return referenceNo;
	}

	/**
	 * @param referenceNo
	 *            the referenceNo to set
	 */
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
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

	/**
	 * @return the documentSelected
	 */
	public boolean isDocumentSelected() {
		return documentSelected;
	}

	/**
	 * @param documentSelected
	 *            the documentSelected to set
	 */
	public void setDocumentSelected(boolean documentSelected) {
		this.documentSelected = documentSelected;
	}

	/**
	 * @return the offenderMiddleName
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * @param offenderMiddleName
	 *            the offenderMiddleName to set
	 */
	public void setOffenderMiddleName(String offenderMiddleName) {
		this.offenderMiddleName = offenderMiddleName;
	}

	/**
	 * @return the taStaffMiddleName
	 */
	public String getTaStaffMiddleName() {
		return taStaffMiddleName;
	}

	/**
	 * @param taStaffMiddleName
	 *            the taStaffMiddleName to set
	 */
	public void setTaStaffMiddleName(String taStaffMiddleName) {
		this.taStaffMiddleName = taStaffMiddleName;
	}

	/**
	 * 
	 * @param offences2
	 * @return
	 */
	public static String formatOffences(List<OffenceBO> offences2) {
		String offenceS = null;
		int num = 0;
		int max = 10;
		for (OffenceBO offence : offences2) {
			num++;
			if (offenceS != null) {
				offenceS = offenceS + "(" + num + ") "
						+ offence.getOffenceDescription() + '\n';
			} else {
				offenceS = "(" + num + ") " + offence.getOffenceDescription()
						+ '\n';
			}

			if (num == max)
				break;
		}

		return offenceS;
	}

	public static String formatWitnessesNames(List<PersonBO> persons) {
		String personS = null;
		int num = 0;
		int max = 1;
		for (PersonBO person : persons) {
			num++;
			if (personS != null) {
				personS = personS
						+ WordUtils.capitalize(person.getFullName(),
								Constants.STRING_DELIM_ARRAY) + '\n';
			} else {
				personS = WordUtils.capitalize(person.getFullName(),
						Constants.STRING_DELIM_ARRAY) + '\n';
			}

			if (num == max)
				break;
		}

		return personS;
	}

	public static String formatWitnessesAddresses(List<PersonBO> persons) {
		String personS = null;
		int num = 0;
		int max = 1;
		for (PersonBO person : persons) {
			num++;
			if (personS != null) {
				personS = personS
						+ WordUtils.capitalize(
								(StringUtils.isNotBlank(person
										.getAddressLineWithNewLine()) ? person
										.getAddressLineWithNewLine() : ""),
								Constants.STRING_DELIM_ARRAY) + '\n';
			} else {
				personS = WordUtils.capitalize(
						(StringUtils.isNotBlank(person
								.getAddressLineWithNewLine()) ? person
								.getAddressLineWithNewLine() : ""),
						Constants.STRING_DELIM_ARRAY) + '\n';
			}
			if (num == max)
				break;
		}

		return personS;
	}

	public static String formatOwnersNames(List<VehicleOwnerBO> owners) {
		String ownerS = null;
		int num = 0;
		int max = 1;
		for (VehicleOwnerBO owner : owners) {
			num++;
			if (ownerS != null) {
				ownerS = ownerS
						+ WordUtils.capitalize(
								(owner.getFirstName() != null ? owner
										.getFirstName().trim() : "")
										+ " "
										+ owner.getLastName().trim(),
								Constants.STRING_DELIM_ARRAY) + '\n';
			} else {
				ownerS = WordUtils.capitalize(
						(owner.getFirstName() != null ? owner.getFirstName()
								.trim() : "")
								+ " "
								+ owner.getLastName().trim(),
						Constants.STRING_DELIM_ARRAY) + '\n';
			}
			if (num == max)
				break;
		}

		return ownerS;
	}

	public static String formatOwnerAddresses(List<VehicleOwnerBO> owners) {
		String ownerS = null;
		int max = 1;
		int num = 0;
		for (VehicleOwnerBO owner : owners) {
			num++;
			if (owner.getAddress() != null) {
				if (StringUtils.isNotBlank(ownerS)) {
					ownerS = ownerS
							+ WordUtils
									.capitalize(
											(StringUtils
													.isNotBlank(owner
															.getAddress()
															.getAddressLineWithNewLine()) ? owner
													.getAddress()
													.getAddressLineWithNewLine()
													: ""),
											Constants.STRING_DELIM_ARRAY)
							+ '\n';
				} else {
					ownerS = WordUtils.capitalize((StringUtils.isNotBlank(owner
							.getAddress().getAddressLineWithNewLine()) ? owner
							.getAddress().getAddressLineWithNewLine() : ""),
							Constants.STRING_DELIM_ARRAY) + '\n';
				}
			}

			if (num == max)
				break;
		}
		/*
		 * if (StringUtils.isNotBlank(ownerS)) return
		 * StringUtils.capitalize(ownerS);
		 */
		return ownerS;
	}

	public Integer getJpIdNumber() {
		return jpIdNumber;
	}

	public void setJpIdNumber(Integer jpIdNumber) {
		this.jpIdNumber = jpIdNumber;
	}

	public String getJpRegNumber() {
		return jpRegNumber;
	}

	public void setJpRegNumber(String jpRegNumber) {
		this.jpRegNumber = jpRegNumber;
	}

	/**
	 * @return the offenceParishCode
	 */
	public String getOffenceParishCode() {
		return offenceParishCode;
	}

	/**
	 * @param offenceParishCode the offenceParishCode to set
	 */
	public void setOffenceParishCode(String offenceParishCode) {
		this.offenceParishCode = offenceParishCode;
	}

	/**
	 * @return the initialCourtId
	 */
	public Integer getInitialCourtId() {
		return initialCourtId;
	}

	/**
	 * @param initialCourtId the initialCourtId to set
	 */
	public void setInitialCourtId(Integer initialCourtId) {
		this.initialCourtId = initialCourtId;
	}

	/**
	 * @return the initialCourtName
	 */
	public String getInitialCourtName() {
		return initialCourtName;
	}

	/**
	 * @param initialCourtName the initialCourtName to set
	 */
	public void setInitialCourtName(String initialCourtName) {
		this.initialCourtName = initialCourtName;
	}

}
