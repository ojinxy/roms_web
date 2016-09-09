package org.fsl.roms.constants;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang.StringUtils;

@ManagedBean
@ApplicationScoped
public class Constants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// String delimiter array
	public static final char[] STRING_DELIM_ARRAY = { '.', ' ', '-', '\n', '/' };

	public static final String SYSTEM_USER = "SYSTEM";

	public static final String IS_MOBILE = "IS_MOBILE";

	// Maximum value for a Strategy Rule that was not specified
	public static final Integer maxStrategyRuleValue = 9999;

	
	public static final String ROAD_OPERATION_LIST = "roadOperationList";
	public static final String ALL_ARTERY_LIST = "ALL_ARTRIES";
	
	public interface ComplianceStatus {
		public String IS_COMPLETE = "COMPL_COMPLETE";
		public String NOT_COMPLETE = "COMPL_NOT_COMPLETE";
	}

	public interface Category {
		public String REGIONAL = "REG";
		public String SPECIAL = "SPC";
	}

	public interface CategoryDesc {
		public String REGIONAL = "Regional";
		public String SPECIAL = "Special";
	}

	public interface OperationType {
		public String REGIONAL = "R";
		public String SPECIAL = "S";
	}

	public interface PersonType {
		public String JP = "JP";
		public String TA_STAFF = "TA";
		public String POLICE_OFFCER = "PO";
		public String ITA_EXAMINER = "IE";
		public String WITNESS = "WT";
		public String DEFAULT = "DF";
		public String OTHER = "OT";
		public String WRECKERDRIVER = "WD";
	}

	public interface PersonRole {
		public String DRIVER = "D";
		public String CONDUCTOR = "C";
		public String OWNER = "O";
		public String OTHER = "T";
	}

	public interface VehicleType {
		public String TRANSPORT_AUTHORITY = "TA";
	}

	public interface OutcomeType {
		public String ISSUE_SUMMONS = "IS";
		public String ISSUE_WARNING_NOTICE = "IW";
		public String WARNED_FOR_PROSECUTION = "WP";
		public String WARNED_NO_PROSECUTION = "WN";
		public String VEHICLE_SEIZURE = "VS";
		public String REMOVE_PLATES = "RP";
		public String ALL_IN_ORDER = "AO";
	}

	public interface RoadCheckType {
		public String MOTOR_VEHICLE = "MV";
		public String DRIVERS_LICENCE = "DL";
		public String BADGE = "BA";
		public String ROAD_LICENCE = "RL";
		public String CITATION = "CI";
		public String OTHER = "OT";

	}

	public interface StatusType {
		public String ROAD_OPERATION = "Road Operation";
		public String SUMMONS = "Summons";
		public String WARNING_NOTICE = "Warning Notice";
		public String COURT_CASE = "Court Case";
		public String MAINTENANCE = "Maintenance";
		public String DOCUMENT_MANAGER = "Document Manager";
		public String ROAD_CHECK = "Road Check";
	}

	public interface Status {
		public String ROAD_OPERATION_SCHEDULING = "RO_SCH";
		public String ROAD_OPERATION_STARTED = "RO_STA";
		public String ROAD_OPERATION_CANCELLED = "RO_CAN";
		public String ROAD_OPERATION_TERMINATED = "RO_TER";
		public String ROAD_OPERATION_COMPLETED = "RO_COM";
		public String ROAD_OPERATION_CLOSED = "RO_CLO";
		public String ROAD_OPERATION_NO_ACTION = "RO_NOA";
		public String ROAD_OPERATION_SCHEDULING_DESC = "Scheduling";
		public String ROAD_OPERATION_STARTED_DESC = "Started";
		public String ROAD_OPERATION_CANCELLED_DESC = "Cancelled";
		public String ROAD_OPERATION_TERMINATED_DESC = "Terminated";
		public String ROAD_OPERATION_COMPLETED_DESC = "Completed";
		public String ROAD_OPERATION_CLOSED_DESC = "Closed";
		public String ROAD_OPERATION_NO_ACTION_DESC = "No Action";
		public String ROAD_CHECK_COMPLETE = "RC_COM";
		public String ROAD_CHECK_INCOMPLETE = "RC_INC";
		public String ROAD_CHECK_CANCELLED = "RC_CAN";

		/* public String SUMMONS_CREATED = "SU_CRE"; public String SUMMONS_AUTHORISED = "SU_AUT"; public String
		 * SUMMONS_CANCELLED = "SU_CAN"; public String SUMMONS_PRINTED= "SU_PRI"; public String SUMMONS_REPRINTED=
		 * "SU_RPR"; public String SUMMONS_SERVED = "SU_SER"; public String SUMMONS_WITHDRAWN = "SU_WIT"; public String
		 * SUMMONS_DISCHARGED_FORM_PRINTED = "SU_DFP"; public String WARNING_NOTICE_CREATED = "WN_CRE"; public String
		 * WARNING_NOTICE_CANCELLED = "WN_CAN"; public String WARNING_NOTICE_PRINTED = "WN_PRI"; public String
		 * WARNING_NOTICE_SERVED = "WN_SER"; public String WARNING_NO_PROSECUTION_CREATED = "WNP_CR"; public String
		 * WARNING_NO_PROSECUTION_CANCELLED = "WNP_CA"; public String WARNING_NO_PROSECUTION_PRINTED = "WNP_PR"; public
		 * String WARNING_NO_PROSECUTION_SERVED = "WNP_SE"; */

		public String COURT_CASE_OPEN = "CC_OPE";
		public String COURT_CASE_CANCELLED = "CC_CAN";
		public String COURT_CASE_CLOSED = "CC_CLO";
		public String COURT_CASE_WITHDRAWN = "CC_WTD";

		public String ACTIVE = "ACT";
		public String INACTIVE = "INA";

	}

	public interface DocumentStatus {
		public String DOCUMENT_MANAGER_PREFIX = "DOC_";
		public String CREATED = "DOC_CR";
		public String AUTHORISED = "DOC_AU";// used for validation
		public String VERIFIED = "DOC_VE";// used for validation
		public String DENY_AUTHORISATION = "DOC_DA";// used for validation
		public String CANCELLED = "DOC_CA";
		public String EDITED = "DOC_ED";// used for validation
		public String PRINTED = "DOC_PR";
		public String REPRINTED = "DOC_RP";// used for validation
		public String SERVED = "DOC_SE";
		public String CLOSED = "DOC_CL";
		public String WITHDRAWN = "DOC_WI";
		public String DISCHARGED_FORM_PRINTED = "DFP";

	}

	public interface BadgeStatusCode {
		public String APPROVED = "A";
		public String PRINTED = "P";
		public String DELIVERED = "D";
		public String EXPIRED = "E";
		public String REVOKED = "K";
		public String SUSPENDED = "S";
		public String REPLACED = "R";
	}

	public interface LMISUserStatusCode {
		public String ACTIVE = "A";
		public String DEACTIVATED = "D";
	}

	public interface StaffType {
		public String ADMINISTRATIVE = "A";
		public String INSPECTOR = "I";
	}



	public interface YesNo {
		public char YES_LABEL = 'Y';
		public char NO_LABEL = 'N';
		public String YES = "YES";
		public String NO = "NO";
		public String YES_LABEL_STR = "Y";
		public String NO_LABEL_STR = "N";
	}

	public interface Verified {

		public Integer YES = 1;
		public Integer NO = 0;

	}

	public interface TTMSCodeTypes {
		public String POLICE_OFFICER = "PoliceOfficer";
		public String VERDICT = "Verdict";
		public String PLEA = "Plea";
		public String COURT_RULING = "CourtRuling";
		public String POLICE_STATION = "PoliceStn";
		public String POLICE_RANK = "PoliceRank";
	}

	public interface WebServiceConfig {
		public String TTMS_WS_URL = "TTMS_WSURL";
		public String TTMS_WS_USER = "TTMS_USER";
		public String TTMS_WS_PASSWD = "TTMS_PASSWD";
		public String TICKET_WS_URL = "TICKET_WSURL";
		public String TRN_WS_URL = "TTMS_WSURL";
		public String AMVS_WS_URL = "AMVS_WSURL";
		public String DL_WS_URL = "TTMS_WSURL";

	}

	
	public interface Reason{
		
		public interface SystemReasonCode{
			public Integer ROAD_OPERATION_CANCEL_REASON = 999;
		}
		
		public interface SystemReasonDescription{
			public String ROAD_OPERATION_CANCEL_REASON_DESC = "Cancelled by System";
		}
	}
	public interface ReasonType {
		public String ROAD_OPERATION = "R";
		public String SUMMONS = "S";
		public String WARNING_NOTICE = "W";
	}

	public interface ReasonTypeCode {
		public String DOCUMENT_CANCELLED = "DC";
		public String DOCUMENT_WITHDRAWN = "DW";
		public String DOCUMENT_REPRINTED = "DR";
		public String ROAD_CHECK_CANCELLED = "RC";
		public String ROAD_CHECK_TERMINATED = "RT";
		public String ROAD_OPERATION_CANCELLED = "ROC";
		public String ROAD_OPERATION_TERMINATED = "ROT";
		public String COURT_CASE_OVERRIDE = "CCO";
		public String COURT_APPEARANCE_OVERRIDE = "CAO";
	}

	public interface FileManagerConfig {
		public String FILE_MANAGER_ROOT_DIRECTORY = "FM_ROOT";
		public String FILE_MANAGER_MAX_FOLDER_SIZE = "FM_MAX_FLDR";
		public String FILE_MANAGER_ROOT_FULL_PERCENT = "FM_FULL_PCNT";
		public String FILE_MANAGER_ROOT_NOTIFICATION_PERCENT = "FM_NOTI_PCNT";
		public String FILE_MANAGER_FOLDER_PREFIX = "FM_FLDR_PRE";
		public String FILE_MANAGER_MAX_FILE_SIZE = "FM_MAX_FILE";
		public String FILE_MANAGER_NOTIFICATION_EMAIL = "FM_EMAIL";
	}

	public interface ScannedDocumentTypeShort {
		public static String MANUAL_DOCUMENT = "MAN";
		public static String SIGNED_DOCUMENT = "SGN";
		public static String EVIDENCE_DOCUMENT = "EVI";
		public static String DISCHARGE_FORM = "DCG";
		public static String STATEMENT_DOCUMENT = "STM";
		public static String OTHER_DOCUMENT = "OTR";
	}

	public interface ScannedDocumentType {
		public static String MANUAL_DOCUMENT = "Manual Document";
		public static String SIGNED_DOCUMENT = "Signed Document";
		public static String EVIDENCE_DOCUMENT = "Evidence Document";
		public static String DISCHARGE_FORM = "Release & Discharge Form";
		public static String STATEMENT_DOCUMENT = "Officer's Statement";
		public static String OTHER_DOCUMENT = "Other";
	}

	public static Object ScannedDocConfig;

	public interface SummonsConfig {
		public int MIN_DAYS_TO_COURT_DATE = 3;

	}

	/*****************************************************************************
	 * DATE FORMATS
	 ****************************************************************************/
	public static class DateFormat {
		public static final String EEE_YYYY_MMM_DD = "EEE yyyy-MMM-dd";
		public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
		public static final String YYYY_MMM_DD_HH_MM = "yyyy-MMM-dd HH:mm";
		public static String YYYY_MM_DD_HH_MM_AM = "yyyy-MM-dd hh:mm a";
		public static String YYYY_MM_DD_HH_MM_SS_AM = "yyyy-MM-dd hh:mm:ss a";
		public static String YYYY_MM_DD = "yyyy-MM-dd";
		public static String YYYY_MM_DD_HH_MM_SS_SSSSS_AM = "yyyy-MM-dd hh:mm:ss.SSSSS a";
		public static String YYYY_MM_DD_HH_MM_SS_SSSSS = "yyyy-MM-dd HH:mm:ss.SSSSS";
	}

	/*****************************************************************************
	 * Document Types for Document Manager
	 ****************************************************************************/
	public static class DocumentType {
		public static String SUMMONS = "SUM";
		public static String WARNING_NOTICE = "WNO";
		public static String WARNING_NOTICE_NO_PROSECUTION = "WNP";
		public static String WITHDRAWAL_OF_SUMMONS = "WTH";
		public static String DISCHARGE_FORM = "DCF";

		public static String ORIGIN_MANUAL = "Manual";
		public static String ORIGIN_AUTOMATIC = "System Generated";

		public static String SUMMONS_STRING = "Summons";
		public static String WARNING_NOTICE_STRING = "Warning Notice";
		public static String WARNING_NOTICE_NO_PROSECUTION_STRING = "Warning Notice No Prosecution";
		public static String ALL_DOCUMENTS = "ALL";
	}

	public static class DocumentOrigin {
		public static String AUTOMATIC = "A";
		public static String MANUAL = "M";
	}

	/******************************************************************************
	 * Security
	 */

	public static class SecurityRoles {
		public static String ROLE_PREFIX = "ROLE_";
		public static String GENERAL_USER_ROLE = "ROLE_USER";
	}

	/****************** USER PERMISSIONS *********************/

	public static class UserPermissions implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1537536308347774905L;

		public UserPermissions() {

		}

		// document manager
		public static String ROMS_DOC_MAN_SEARCH = "ROMS_DOC_MAN_SEARCH";
		public static String ROMS_DOC_MAN_EDIT = "ROMS_DOC_MAN_EDIT";
		public static String ROMS_DOC_MAN_UPLOAD = "ROMS_DOC_MAN_UPLOAD";
		public static String ROMS_DOC_MAN_CANCEL = "ROMS_DOC_MAN_CANCEL";
		public static String ROMS_DOC_MAN_WITHDRAW = "ROMS_DOC_MAN_WITHDRAW";
		public static String ROMS_DOC_MAN_VERIFY = "ROMS_DOC_MAN_VERIFY";
		public static String ROMS_DOC_MAN_JP_VERIFY = "ROMS_DOC_MAN_JP_VERIFY";
		public static String ROMS_DOC_MAN_SERVE = "ROMS_DOC_MAN_SERVE";
		public static String ROMS_DOC_MAN_PRINT = "ROMS_DOC_MAN_PRINT";
		// public static String ROMS_DOC_MAN_PRINT_DF = "ROMS_DOC_MAN_PRINT_DF";

		// scanned doc
		public static String ROMS_DOC_MAN_SD_DELETE = "ROMS_DOC_MAN_SD_DELETE";
		public static String ROMS_DOC_MAN_SD_PRINT = "ROMS_DOC_MAN_SD_PRINT";

		// court
		public static String ROMS_DOC_MAN_CC_EDIT = "ROMS_DOC_MAN_CC_EDIT";
		public static String ROMS_DOC_MAN_CC_OVERRIDE = "ROMS_DOC_MAN_CC_OVER";
		public static String ROMS_DOC_MAN_CC_DATE_OVERRIDE = "ROMS_DOC_MAN_CC_DATE_OVER";
		public static String ROMS_DOC_MAN_CC_VIEW = "ROMS_DOC_MAN_CC_VIEW";

		// road operation
		public static String ROMS_ROAD_OP_ALL = "ROMS_ROAD_OP_ALL";
		public static String ROMS_ROAD_OP_SEARCH = "ROMS_ROAD_OP_SEARCH";
		public static String ROMS_ROAD_OP_SCHEDULE = "ROMS_ROAD_OP_SCHEDULE";
		public static String ROMS_ROAD_OP_CALENDAR = "ROMS_ROAD_OP_CALENDAR";
		public static String ROMS_ROAD_OP_EDIT = "ROMS_ROAD_OP_EDIT";
		public static String ROMS_ROAD_OP_START = "ROMS_ROAD_OP_START";
		public static String ROMS_ROAD_OP_COMPLETE = "ROMS_ROAD_OP_COMPLETE";
		public static String ROMS_ROAD_OP_TERMINATE = "ROMS_ROAD_OP_TERMINATE";
		public static String ROMS_ROAD_OP_CANCEL = "ROMS_ROAD_OP_CANCEL";
		public static String ROMS_ROAD_OP_CLOSE = "ROMS_ROAD_OP_CLOSE";
		public static String ROMS_ROAD_OP_AUTH = "ROMS_ROAD_OP_AUTH";

		// Road Check
		public static String ROMS_ROAD_CHK_PERFORM = "ROMS_ROAD_CHK_PERFORM";
		public static String ROMS_ROAD_CHK_ALL = "ROMS_ROAD_CHK_ALL";
		public static String ROMS_ROAD_CHK_EDIT = "ROMS_ROAD_CHK_EDIT";
		public static String ROMS_ROAD_CHK_PRINT = "ROMS_ROAD_CHK_PRINT";
		public static String ROMS_ROAD_CHK_SEARCH = "ROMS_ROAD_CHK_SEARCH";
		public static String ROMS_ROAD_CHK_UPLOAD = "ROMS_ROAD_CHK_UPLOAD";

		// User Setup
		public static String ROMS_USR_SET_EDIT = "ROMS_USR_SET_EDIT";
		public static String ROMS_USR_SET_SEARCH = "ROMS_USR_SET_SEARCH";

		// Reference Codes
		/**************** Artery ****************************/
		public final String ROLE_ROMS_REF_ART_MENU = "ROLE_ROMS_REF_ART_MENU";
		public final String ROLE_ROMS_REF_ART_SEARCH = "ROLE_ROMS_REF_ART_SEARCH";
		public final String ROLE_ROMS_REF_ART_EDIT = "ROLE_ROMS_REF_ART_EDIT";
		public final String ROLE_ROMS_REF_ART_ADD = "ROLE_ROMS_REF_ART_ADD";
		/**************** Court ****************************/
		public final String ROLE_ROMS_REF_COURT_SEARCH = "ROLE_ROMS_REF_COURT_SEARCH";
		public final String ROLE_ROMS_REF_COURT_EDIT = "ROLE_ROMS_REF_COURT_EDIT";
		public final String ROLE_ROMS_REF_COURT_ADD = "ROLE_ROMS_REF_COURT_ADD";
		/**************** ITA Examiner ****************************/
		public final String ROLE_ROMS_REF_ITA_SEARCH = "ROLE_ROMS_REF_ITA_SEARCH";
		public final String ROLE_ROMS_REF_ITA_EDIT = "ROLE_ROMS_REF_ITA_EDIT";
		public final String ROLE_ROMS_REF_ITA_ADD = "ROLE_ROMS_REF_ITA_ADD";
		/**************** JP ****************************/
		public final String ROLE_ROMS_REF_JP_SEARCH = "ROLE_ROMS_REF_JP_SEARCH";
		public final String ROLE_ROMS_REF_JP_EDIT = "ROLE_ROMS_REF_JP_EDIT";
		public final String ROLE_ROMS_REF_JP_ADD = "ROLE_ROMS_REF_JP_ADD";
		public final String ROLE_ROMS_REF_JP_REST_PIN = "ROLE_ROMS_REF_JP_REST_PIN";
		/**************** Location ****************************/
		public final String ROLE_ROMS_REF_LOC_SEARCH = "ROLE_ROMS_REF_LOC_SEARCH";
		public final String ROLE_ROMS_REF_LOC_EDIT = "ROLE_ROMS_REF_LOC_EDIT";
		public final String ROLE_ROMS_REF_LOC_ADD = "ROLE_ROMS_REF_LOC_ADD";
		/**************** Offence ****************************/
		public final String ROLE_ROMS_REF_OFFENCE_SEARCH = "ROLE_ROMS_REF_OFFENCE_SEARCH";
		public final String ROLE_ROMS_REF_OFFENCE_EDIT = "ROLE_ROMS_REF_OFFENCE_EDIT";
		public final String ROLE_ROMS_REF_OFFENCE_ADD = "ROLE_ROMS_REF_OFFENCE_ADD";
		/**************** Parish ****************************/
		public final String ROLE_ROMS_REF_PARISH_SEARCH = "ROLE_ROMS_REF_PARISH_SEARCH";
		public final String ROLE_ROMS_REF_PARISH_EDIT = "ROLE_ROMS_REF_PARISH_EDIT";
		/**************** Pound ****************************/
		public final String ROLE_ROMS_REF_POUND_SEARCH = "ROLE_ROMS_REF_POUND_SEARCH";
		public final String ROLE_ROMS_REF_POUND_EDIT = "ROLE_ROMS_REF_POUND_EDIT";
		public final String ROLE_ROMS_REF_POUND_ADD = "ROLE_ROMS_REF_POUND_ADD";
		/**************** Reason ****************************/
		public final String ROLE_ROMS_REF_REASON_SEARCH = "ROLE_ROMS_REF_REASON_SEARCH";
		public final String ROLE_ROMS_REF_REASON_EDIT = "ROLE_ROMS_REF_REASON_EDIT";
		public final String ROLE_ROMS_REF_REASON_ADD = "ROLE_ROMS_REF_REASON_ADD";
		/**************** Strategy ****************************/
		public final String ROLE_ROMS_REF_STRATEGY_SEARCH = "ROLE_ROMS_REF_STRATEGY_SEARCH";
		public final String ROLE_ROMS_REF_STRATEGY_EDIT = "ROLE_ROMS_REF_STRATEGY_EDIT";
		public final String ROLE_ROMS_REF_STRATEGY_ADD = "ROLE_ROMS_REF_STRATEGY_ADD";
		/**************** TA Vehicles ****************************/
		public final String ROLE_ROMS_REF_TAVHCLE_SEARCH = "ROLE_ROMS_REF_TAVHCLE_SEARCH";
		public final String ROLE_ROMS_REF_TAVHCLE_EDIT = "ROLE_ROMS_REF_TAVHCLE_EDIT";
		public final String ROLE_ROMS_REF_TAVHCLE_ADD = "ROLE_ROMS_REF_TAVHCLE_ADD";
		/**************** Wrecking Co ****************************/
		public final String ROLE_ROMS_REF_WRKCOM_SEARCH = "ROLE_ROMS_REF_WRKCOM_SEARCH";
		public final String ROLE_ROMS_REF_WRKCOM_EDIT = "ROLE_ROMS_REF_WRKCOM_EDIT";
		public final String ROLE_ROMS_REF_WRKCOM_ADD = "ROLE_ROMS_REF_WRKCOM_ADD";

		// Getters

		public String getROLE_ROMS_REF_ART_MENU() {
			return ROLE_ROMS_REF_ART_MENU;
		}

		public String getROLE_ROMS_REF_ART_SEARCH() {
			return ROLE_ROMS_REF_ART_SEARCH;
		}

		public String getROLE_ROMS_REF_ART_EDIT() {
			return ROLE_ROMS_REF_ART_EDIT;
		}

		public String getROLE_ROMS_REF_ART_ADD() {
			return ROLE_ROMS_REF_ART_ADD;
		}

		public String getROLE_ROMS_REF_COURT_SEARCH() {
			return ROLE_ROMS_REF_COURT_SEARCH;
		}

		public String getROLE_ROMS_REF_COURT_EDIT() {
			return ROLE_ROMS_REF_COURT_EDIT;
		}

		public String getROLE_ROMS_REF_COURT_ADD() {
			return ROLE_ROMS_REF_COURT_ADD;
		}

		public String getROLE_ROMS_REF_ITA_SEARCH() {
			return ROLE_ROMS_REF_ITA_SEARCH;
		}

		public String getROLE_ROMS_REF_ITA_EDIT() {
			return ROLE_ROMS_REF_ITA_EDIT;
		}

		public String getROLE_ROMS_REF_ITA_ADD() {
			return ROLE_ROMS_REF_ITA_ADD;
		}

		public String getROLE_ROMS_REF_JP_SEARCH() {
			return ROLE_ROMS_REF_JP_SEARCH;
		}

		public String getROLE_ROMS_REF_JP_EDIT() {
			return ROLE_ROMS_REF_JP_EDIT;
		}

		public String getROLE_ROMS_REF_JP_ADD() {
			return ROLE_ROMS_REF_JP_ADD;
		}

		public String getROLE_ROMS_REF_JP_REST_PIN() {
			return ROLE_ROMS_REF_JP_REST_PIN;
		}

		public String getROLE_ROMS_REF_LOC_SEARCH() {
			return ROLE_ROMS_REF_LOC_SEARCH;
		}

		public String getROLE_ROMS_REF_LOC_EDIT() {
			return ROLE_ROMS_REF_LOC_EDIT;
		}

		public String getROLE_ROMS_REF_LOC_ADD() {
			return ROLE_ROMS_REF_LOC_ADD;
		}

		public String getROLE_ROMS_REF_OFFENCE_SEARCH() {
			return ROLE_ROMS_REF_OFFENCE_SEARCH;
		}

		public String getROLE_ROMS_REF_OFFENCE_EDIT() {
			return ROLE_ROMS_REF_OFFENCE_EDIT;
		}

		public String getROLE_ROMS_REF_OFFENCE_ADD() {
			return ROLE_ROMS_REF_OFFENCE_ADD;
		}

		public String getROLE_ROMS_REF_PARISH_SEARCH() {
			return ROLE_ROMS_REF_PARISH_SEARCH;
		}

		public String getROLE_ROMS_REF_PARISH_EDIT() {
			return ROLE_ROMS_REF_PARISH_EDIT;
		}

		public String getROLE_ROMS_REF_POUND_SEARCH() {
			return ROLE_ROMS_REF_POUND_SEARCH;
		}

		public String getROLE_ROMS_REF_POUND_EDIT() {
			return ROLE_ROMS_REF_POUND_EDIT;
		}

		public String getROLE_ROMS_REF_POUND_ADD() {
			return ROLE_ROMS_REF_POUND_ADD;
		}

		public String getROLE_ROMS_REF_REASON_SEARCH() {
			return ROLE_ROMS_REF_REASON_SEARCH;
		}

		public String getROLE_ROMS_REF_REASON_EDIT() {
			return ROLE_ROMS_REF_REASON_EDIT;
		}

		public String getROLE_ROMS_REF_REASON_ADD() {
			return ROLE_ROMS_REF_REASON_ADD;
		}

		public String getROLE_ROMS_REF_STRATEGY_SEARCH() {
			return ROLE_ROMS_REF_STRATEGY_SEARCH;
		}

		public String getROLE_ROMS_REF_STRATEGY_EDIT() {
			return ROLE_ROMS_REF_STRATEGY_EDIT;
		}

		public String getROLE_ROMS_REF_STRATEGY_ADD() {
			return ROLE_ROMS_REF_STRATEGY_ADD;
		}

		public String getROLE_ROMS_REF_TAVHCLE_SEARCH() {
			return ROLE_ROMS_REF_TAVHCLE_SEARCH;
		}

		public String getROLE_ROMS_REF_TAVHCLE_EDIT() {
			return ROLE_ROMS_REF_TAVHCLE_EDIT;
		}

		public String getROLE_ROMS_REF_TAVHCLE_ADD() {
			return ROLE_ROMS_REF_TAVHCLE_ADD;
		}

		public String getROLE_ROMS_REF_WRKCOM_SEARCH() {
			return ROLE_ROMS_REF_WRKCOM_SEARCH;
		}

		public String getROLE_ROMS_REF_WRKCOM_EDIT() {
			return ROLE_ROMS_REF_WRKCOM_EDIT;
		}

		public String getROLE_ROMS_REF_WRKCOM_ADD() {
			return ROLE_ROMS_REF_WRKCOM_ADD;
		}

	}

	/*************************** FILE UPLOAD ****************************************/
	public static class FileUpload {
		public static String FILE_SCANNED_DOC_LIST = "fileUploadList";
		public static String FILE_CONTENTS = "fileContents";
		public static String FILE_NAME = "fileName";
		public static String FILE_SIZE = "fileSize";
		public static String FILE_MIME_TYPE = "fileMimeType";
		public static String FILE_EXTENSION = "fileExtension";
		public static String FILE_MOST_RECENT_UPLOAD = "fileLatest";
	}

	/****************** GENERATING DOCUMENTS (SUMMONS/WARNING NOTICE ETC) *********************/

	public static class DocumentPrinting {
		public static String DOCUMENT_LIST = "documentList";
	}

	/************************************* PLEA **************************************************/
	public static class PLEA {
		public static Integer GUILTY = 1;
		public static Integer NOT_GUILTY = 2;
	}

	/************************************* VERDICT **************************************************/
	public static class VERDICT {
		public static Integer GUILTY = 1;
		public static Integer NOT_GUILTY = 2;
		public static Integer WITHDRAWN = 3;
	}

	/************************************* Court Ruling **************************************************/
	public static class RULING {
		public static String POSTPONE_STRING = "POSTPONE";
		public static String POSTPONE_CODE = "POS";
		public static String SENTENCED = "008";

	}

	/**
	 * Th public static String FILE_MIME_TYPE = "fileMimeType"; public static String FILE_EXTENSION = "fileExtension"; o
	 * the document type status
	 * 
	 * @param status
	 * @return
	 */
	public static String mapStatusToDocumentType(String status) {

		String docTypePrefix, docTypeSuffix;
		String documentStatus;

		if (StringUtils.isBlank(status))
			return "";

		docTypePrefix = StringUtils.substringBefore(status, "_");
		docTypeSuffix = StringUtils.substringAfter(status, "_");
		// System.err.println(" document prefix " + docTypePrefix);

		documentStatus = DocumentStatus.DOCUMENT_MANAGER_PREFIX + docTypeSuffix;

		// System.err.println(" document status " + docTypePrefix);
		return documentStatus;

	}

	public interface DataEnteredParam {
		public Integer DIRECTIVE = 1;
		public Integer INSPECTOR = 2;
		public Integer NO_OF_PASSENGERS = 13;
	}

	public interface OffenceParam {
		public String DIRECTIVE = "Directive_of_Inspector";
		public String INSPECTOR = "Inspector_Threatened";
		public String VEHICLE_TYPE = "Vehicle_Type";
		public String PLATE_NUMBER = "Plate_Number";
		public String ROUTE = "Route";
		public String PARISH_OF_OFFENCE = "Parish_of_Offence";
		public String ROUTE_START = "Route_Start";
		public String ROUTE_END = "Route_End";
		public String ARTERY = "Artery";
		public String DRIVER_NAME = "Drivers_Name";
		public String LICENCE_TYPE = "Licence_Type";
		public String PARISH_OF_OFFENDER = "Parish_of_Offender";
		public String SEATING_CAPACITY = "Seating_capacity";
		public String NO_OF_PASSENGERS = "Number_of_Passengers";
	}

	public static final Integer DEFAULT_OFFENCE = 9999;
	public static final String DEFAULT_OFFENCE_DESCRIPTION = "No Offence Related to this Road Check";
	public static final String DEFAULT_OFFENCE_SHORT_DESCRIPTION = "No Related Offence";

	public interface PrintApp {
		public static final String PATH = "PRINT_APP_PATH";
		public static final String NAME = "PRINT_APP_NAME";
		public static final String FILE = "PRINT_APP_FILE";

	}

	public interface ActionToTake{
		public static String ALL_IN_ORDER = "A";
		public static String RECORD_OFFENCE = "R";
	}
	
	public interface OffenceAidAndAbbet{
		public static Integer NO_ROAD_LIC = 35;
		public static Integer NO_PPV_INSUR = 36;
	}
	
	public interface ParishKingstonAndStAndrew{
		public static String KINGSTON = "01";
		public static String ST_ANDREW = "02";
	}
	
	public static Set<String> MergeKingstonAndStAndrew(){
		Set<String> selParCodes = new HashSet<String>();
		selParCodes.add(Constants.ParishKingstonAndStAndrew.KINGSTON);
		selParCodes.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
		return selParCodes;
	}
}
