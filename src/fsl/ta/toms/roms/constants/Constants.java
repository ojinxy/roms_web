package fsl.ta.toms.roms.constants;

public class Constants {
	
	//String delimiter array
	public static final char[] STRING_DELIM_ARRAY = {'.',' ','-','\n','/'};
	
	//Maximum value for a Strategy Rule that was not specified
	public static final Integer maxStrategyRuleValue = 9999;
	
	/*PERMISSIONS*/
	public interface Permissions
	{
		public static String ROMS_ROAD_OP_SCHEDULE_SPC = "ROMS_ROAD_OP_SCHEDULE_SPC";
	}
	
	
	public interface Category{
		public String REGIONAL = "REG";
		public String SPECIAL = "SPC";
	}
	
	public interface Configuration
	{
		public String MAX_ROMS_OFFENCES = "MAX_ROMS_OFF";
		public String MAX_TTMS_OFFENCES = "MAX_TTMS_OFF";
	}
	
	public interface OperationType{
		public String REGIONAL = "R";
		public String SPECIAL = "S";
	}
	
	
	public interface PersonType{
		public String JP = "JP";
		public String TA_STAFF = "TA";
		public String POLICE_OFFCER = "PO";
		public String ITA_EXAMINER = "IE";
		public String WITNESS = "WT";
		public String DEFAULT = "DF";
		public String OTHER = "OT";
		public String WRECKERDRIVER = "WD";
	}
	
	
	public interface DocumentType{
		public String SUMMONS = "SUM";
		public String WARNING_NOTICE = "WNO";		
		public String WARNING_NOTICE_NO_PROSECUTION = "WNP";
		public String WITHDRAWAL_OF_SUMMONS = "WTH";
		
		public String ORIGIN_MANUAL = "Manual";
		public String ORIGIN_AUTOMATIC = "System Generated";		
		
		public static String SUMMONS_STRING= "Summons";		
		public static String WARNING_NOTICE_STRING = "Warning Notice";		
		public static String WARNING_NOTICE_NO_PROSECUTION_STRING = "Warning Notice No Prosecution";
		
	}
	
	
	public interface ScannedDocumentTypeShort {
		public static String MANUAL_DOCUMENT = "MAN";
		public static String SIGNED_DOCUMENT = "SGN";
		public static String EVIDENCE_DOCUMENT = "EVI";
		public static String DISCHARGE_FORM = "DCG";
		public static String STATEMENT_DOCUMENT = "STM";
		public static String OTHER_DOCUMENT = "OTR";
	}

		public interface ScannedDocumentType{
			public static String MANUAL_DOCUMENT ="Manual Document";
			public static String SIGNED_DOCUMENT = "Signed Document";
			public static String EVIDENCE_DOCUMENT = "Evidence Document";
			public static String DISCHARGE_FORM = "Release & Discharge Form";
			public static String STATEMENT_DOCUMENT = "Officer's Statement";
		public static String OTHER_DOCUMENT = "Other";
		}
	
	
	public interface DocumentTypeLong{
		public String SUMMONS = "Summons";
		public String WARNING_NOTICE = "Warning Notice";
		//public String WARNED_FOR_PROSECUTION = "Warning For Prosecution";
		public String WARNING_NOTICE_NO_PROSECUTION = "Warning Notice No Prosecution";
		public String WITHDRAWAL_OF_SUMMONS = "Withdrawal Of Summons";
	}
	
	public interface PersonRole{
		public String DRIVER = "D";
		public String CONDUCTOR = "C";
		public String OWNER = "O";
		public String OTHER = "T";
		public String DRIVER_STRING = "Driver";
		public String CONDUCTOR_STRING = "Conductor";
		public String OWNER_STRING = "Owner";
	}
	
	
	public interface VehicleType{
		public String TRANSPORT_AUTHORITY = "TA";
	}
	
	
	public interface OutcomeType{
		public String ISSUE_SUMMONS = "IS";
		public String ISSUE_WARNING_NOTICE = "IW";
		public String WARNED_FOR_PROSECUTION = "WP";
		public String VEHICLE_SEIZURE = "VS";
		public String REMOVE_PLATES = "RP";
		public String ALL_IN_ORDER = "AO";
		public String WARNED_NO_PROSECUTION = "WN";
	}
	
	
	public interface RoadCheckType{
		public String MOTOR_VEHICLE = "MV";
		public String DRIVERS_LICENCE = "DL";
		public String BADGE = "BA";
		public String ROAD_LICENCE = "RL";
		public String CITATION = "CI";
		public String OTHER = "OT";
		
	}
	
	
	public interface StatusType{
		public String ROAD_OPERATION = "Road Operation";
		public String SUMMONS = "Summons";
		public String WARNING_NOTICE = "Warning Notice";
		public String COURT_CASE = "Court Case";
		public String MAINTENANCE = "Maintenance";
	}
	
	
	public interface DocumentStatus{
		public String DOCUMENT_MANAGER_PREFIX = "DOC_";
		public String CREATED = "DOC_CR";
		public String AUTHORISED = "DOC_AU";//used for validation
		public String VERIFIED = "DOC_VE";// used for validation
		public String DENY_AUTHORISATION = "DOC_DA";// used for validation
		public String CANCELLED = "DOC_CA";
		public String EDITED = "DOC_ED";//used for validation
		public String PRINTED= "DOC_PR";
		public String REPRINTED= "DOC_RP";//used for validation
		public String SERVED = "DOC_SE";
		public String CLOSED = "DOC_CL";
		public String WITHDRAWN = "DOC_WI";
		public String DISCHARGED_FORM_PRINTED = "DFP";
		
	}
	
	
	public interface Status{
		public String ROAD_OPERATION_SCHEDULING = "RO_SCH";
		public String ROAD_OPERATION_STARTED = "RO_STA";
		public String ROAD_OPERATION_CANCELLED = "RO_CAN";
		public String ROAD_OPERATION_TERMINATED = "RO_TER";
		public String ROAD_OPERATION_COMPLETED = "RO_COM";
		public String ROAD_OPERATION_CLOSED = "RO_CLO";
		public String ROAD_OPERATION_NO_ACTION = "RO_NOA";
		public String ROAD_CHECK_COMPLETE = "RC_COM";
		public String ROAD_CHECK_INCOMPLETE = "RC_INC";
		public String ROAD_CHECK_CANCELLED = "RC_CAN";		
				
		public String COURT_CASE_OPEN = "CC_OPE";
		public String COURT_CASE_CANCELLED = "CC_CAN";
		public String COURT_CASE_CLOSED = "CC_CLO";
		public String COURT_CASE_WITHDRAWN = "CC_WTD";
		
		public String ACTIVE = "ACT";
		public String INACTIVE = "INA";
		
	}
	
	public static class VERDICT{
		public static Integer GUILTY = 1;
		public static Integer NOT_GUILTY =2;
		public static Integer WITHDRAWN = 3;
	}
	
	/************************************* Court Ruling **************************************************/
	public static class RULING {
		public static String SENTENCED = "008";

	}

	
	
	
	public interface BadgeStatusCode{
		public String APPROVED = "A";
		public String PRINTED = "P";
		public String DELIVERED = "D";
		public String EXPIRED = "E";
		public String REVOKED = "K";
		public String SUSPENDED = "S";
		public String REPLACED = "R";
	}
	
	
	public interface LMISUserStatusCode{
		public String ACTIVE = "A";
		public String DEACTIVATED = "D";
	}
	
	public interface LMISLicenceStatusCode{
		public String  SUSPENDED = "U";
		public String WITHDRAWN = "W";
		public String SURRENDERED = "S";
		public String EXPIRED = "E";
		public String DELIVERED = "D";
		public String PRINTED = "P";
		public String RENEWED = "R";
		public String BEINGRENEWED = "B";
		public String PAID = "P";
		public String CANCELLED = "C";
		public String AMENDED = "A";
	}
	
	public interface StaffType{
		public String ADMINISTRATIVE = "A";
		public String INSPECTOR = "I";
	}
	
	
	public interface EventCode{
		public Integer LOG_IN = 10;
		public Integer ROMS_TABLE_UPDATES_SHEDULER = 20;
		public Integer ADD_TA_STAFF_USER_MAPPING = 30;
		public Integer UPDATE_TA_STAFF_USER_MAPPING = 40;
		public Integer SCHEDULE_OPERATION = 100;
		public Integer UPDATE_OPERATION = 110;
		public Integer CANCEL_OPERATION = 120;
		public Integer START_OPERATION = 130;
		public Integer TERMINATE_OPERATION = 140;
		public Integer COMPLETE_OPERATION = 150;
		public Integer CLOSE_OPERATION = 160;
		public Integer AUTHORIZE_OPERATION = 170;
		public Integer COMPLIANCY_CHECK = 200;
		public Integer QUERY_DRIVERS_LICENCE = 210;
		public Integer QUERY_VEHICLE_REGISTRATION_DETAILS = 220;
		public Integer LOOKUP_CITATION_HISTORY = 230;
		public Integer LOOKUP_DRIVER_CONDUCTOR_BADGE_INFORMATION = 240;
		public Integer VERIFY_ROAD_LICENCE = 250;
		public Integer QUICK_QUERY = 260;
		public Integer CANCEL_ROAD_CHECK = 280;
		
		/************* DOCUMENT MANAGER ************/
		public Integer UPLOAD_SCANNED_DOCUMENT = 290;
		public Integer DELETE_SCANNED_DOCUMENT = 292;
		public Integer CREATE_SCANNED_DOCUMENT_HISTORY = 294;
		
		public Integer CREATE_DOCUMENT = 300;
		public Integer UPDATE_DOCUMENT = 310;		
		public Integer PRINT_DOCUMENT = 320;
		public Integer REPRINT_DOCUMENT = 330;
		public Integer REPRINT_SCANNED_DOCUMENT = 331;
		public Integer SERVE_DOCUMENT = 340;
		public Integer CANCEL_DOCUMENT = 350;
		public Integer CLOSE_DOCUMENT = 355;
		public Integer WITHDRAW_DOCUMENT = 360;		
		
		/*
		 * removed in response to ROMS1.0-231
		 *  The VERIFY_DETAILS_DOCUMENT event was replaced with VERIFY_DOCUMENT
		 */
		//public Integer VERIFY_DETAILS_DOCUMENT = 370;
		public Integer VERIFY_DOCUMENT = 370;
		
		
		public Integer VERIFY_JP_DETAILS_DOCUMENT = 380;		
		public Integer JP_CONFIRMATION = 391;
		
		/*
		 * removed in response to ROMS1.0-231. 
		 * The DENY_AUTHORIZATION_DOCUMENT event was replaced with REJECT_DOCUMENT
		 * The AUTHORIZE_DOCUMENT event was replaced with VERIFY_DOCUMENT
		 */
		//public Integer AUTHORIZE_DOCUMENT = 390;
		//public Integer DENY_AUTHORIZATION_DOCUMENT = 395;
		public Integer REJECT_DOCUMENT = 395;
		
		/********** COURT CASE *******************/
		public Integer CREATE_COURT_CASE = 500;
		public Integer UPDATE_COURT_CASE = 510;
		public Integer WITHDRAW_COURT_CASE = 520;
		public Integer CLOSE_COURT_CASE = 525;
		public Integer CANCEL_COURT_CASE = 530;
		public Integer OVERRIDE_COURT_CASE=540;		
		
		
		public Integer CREATE_COURT_APPEARANCE = 550;	
		public Integer UPDATE_COURT_APPEARANCE = 560;
		public Integer WITHDRAW_COURT_APPEARANCE = 570;
		public Integer CLOSE_COURT_APPEARANCE = 575;
		public Integer CANCEL_COURT_APPEARANCE = 580;
		public Integer OVERRIDE_COURT_APPEARANCE=590;
		//removed following event audit review
//		public Integer OVERRIDE_COURT_APPEARANCE_COURT=595;
//		public Integer OVERRIDE_COURT_APPEARANCE_DATE=595;

		/******************** END *******************/
		
		public Integer CREATE_REFERENCE_CODE = 610;
		public Integer UPDATE_REFERENCE_CODE = 620;
		
		public Integer CREATE_JP_PIN = 630;
		public Integer UPDATE_JP_PIN = 640;
		public Integer RESET_JP_PIN = 650;
		public Integer UPDATE_POLICE_OFFICER = 700;
		public Integer UPDATE_TA_STAFF = 710;
		public Integer UPDATE_PLEA = 720;
		public Integer UPDATE_VERDICT = 730;
		public Integer UPDATE_COURT_RULING = 740;
		
		/*public Integer CREATE_WARNING_NO_PROSECUTION = 800;
		public Integer UPDATE_WARNING_NO_PROSECUTION = 810;
		public Integer CANCEL_WARNING_NO_PROSECUTION = 820;
		public Integer AUTHORIZE_WARNING_NO_PROSECUTION = 830;
		public Integer PRINT_WARNING_NO_PROSECUTION = 840;
		public Integer ISSUE_WARNING_NO_PROSECUTION = 850;		
		public Integer REPRINT_WARNING_NO_PROSECUTION = 870;
		public Integer VERIFY_DETAILS_WARNING_NO_PROSECUTION = 880;*/
		//public Integer UPLOAD_SCANNED_WARNING_NO_PROSECUTION = 860;
		
		
		public Integer LOG_OUT = 999;
			
	}

	
	public interface EventRefTypeCode{
		public String REFERENCE_TABLE_NAME= "TAB";
		public String REFERENCE_CODE = "COD";
		public String PLATE_NUMBER = "PLN";
		public String DRIVERS_LICENCE_NUMBER = "DLN";
		public String ROAD_LICENCE_NUMBER = "RLN";
		public String BADGE_NO = "BNO";
		public String BADGE_TYPE = "BDT";
		public String TRN = "TRN";
		public String OVERRIDE_USER = "OUS";
		public String OVERRIDE_DATE = "ODA";
		public String SCHEDULED_DATE = "SCD";
		public String SCHEDULED_LOCATION = "SCL";
		public String PERSON_ID = "PID";
		public String STAFF_ID = "SID";
		public String SUMMONS="SUM";
		public String WARNING_NOTICE="WRN";
		public String WARNING_NO_PROSECUTION="WNP";
		public String SUMMONS_ID = "SMD";
		public String COURT_APPEARANCE_ID = "CID";
		public String COURT_CASE_ID = "CCI";
		public String USER_NAME = "USR";
		public String OPERATION_CATEGORY = "CTG";
		public String OPERATION_NAME = "OPN";
		public String CURRENT_STATUS = "CST";
		public String REASON = "RSN";
		public String PERSON_FNAME = "FNM";
		public String PERSON_LNAME = "LNM";
		public String JP_REG_NUMBER = "JRN";
		public String ROAD_CHECK_ID = "RCI";
		public String NUM_RECS_PROCESSED = "NRP";
		public String SCANNED_DOCUMENT="SDC";
		public String COURT_ID = "CRT";
		public String COURT_APPEARANCE_DATE = "CAD";
		
		public String REFERENCE_NUMBER = "REF";
		public String DOCUMENT_TYPE = "DTP";
		public String OFFENCE_DATE = "OFD";
		public String COURT_CASE_NUMBER = "CCN";
		public String OFFNEDER_NAME = "OFN";
		
		public String SCANNED_DOC_TYPE="SDT";
		
	}

	public interface YesNo{
		public char YES_LABEL = 'Y';
		public char NO_LABEL = 'N';
		public char YES_FLAG = 'Y';
		public char NO_FLAG = 'N';
		public String YES = "YES";
		public String NO = "NO";
		public String YES_LABEL_STR = "Y";
		public String NO_LABEL_STR = "N";
	}
	
	public interface TTMSCodeTypes{
		public String POLICE_OFFICER= "PoliceOfficer";
		public String VERDICT = "Verdict";
		public String PLEA = "Plea";
		public String COURT_RULING = "CourtRuling";
		public String POLICE_STATION = "PoliceStn";
		public String POLICE_RANK = "PoliceRank";
	}
	
	
	public interface WebServiceConfig{
		public String TTMS_WS_URL= "TTMS_WSURL";
		public String TTMS_WS_USER = "TTMS_USER";
		public String TTMS_WS_PASSWD = "TTMS_PASSWD";
		public String TICKET_WS_URL = "TICKET_WSURL";
		public String TRN_WS_URL = "TTMS_WSURL";
		public String AMVS_WS_URL = "AMVS_WSURL";
		public String DL_WS_URL = "TTMS_WSURL";

	}
	
	public interface ReasonType
	{
		public String ROAD_OPERATION = "R";
		public String SUMMONS = "S";
		public String WARNING_NOTICE = "W";
	}
	
	public interface ReasonTypeCode 
	{
		public String DOCUMENT_CANCELLED = "DC";
		public String DOCUMENT_WITHDRAWN = "DW";
		public String DOCUMENT_REPRINTED = "DR";
		public String ROAD_CHECK_CANCELLED = "RC";
		public String ROAD_CHECK_TERMINATED = "RT";
	}
	
	public interface FileManagerConfig{
		public String FILE_MANAGER_ROOT_DIRECTORY = "FM_ROOT";
		public String FILE_MANAGER_MAX_FOLDER_SIZE = "FM_MAX_FLDR";
		public String FILE_MANAGER_ROOT_FULL_PERCENT = "FM_FULL_PCNT";
		public String FILE_MANAGER_ROOT_NOTIFICATION_PERCENT = "FM_NOTI_PCNT";
		public String FILE_MANAGER_FOLDER_PREFIX = "FM_FLDR_PRE";
		public String FILE_MANAGER_MAX_FILE_SIZE = "FM_MAX_FILE";
		public String FILE_MANAGER_NOTIFICATION_EMAIL = "FM_EMAIL";
	}

	
	public interface SummonsConfig{
		public int MIN_DAYS_TO_COURT_DATE = 3;
		
	}
	
	public interface ComplianceStatus{
		public String IS_COMPLETE = "COMPL_COMPLETE";
		public String NOT_COMPLETE = "COMPL_NOT_COMPLETE";
	}
	
	public interface FslWebServiceExceptionCodes
	{
		public String NO_REC_FOUND = "NO_RECORD_FOUND";
	}
	
	
	public interface ProcessControl 
	{
		public String STARTED = "S";
		public String COMPLETED = "C";
		
	}
	
	public interface TRNStatus{
		public String ACTIVE = "A";
		public String CLOSED = "C";
		public String DECEASED = "D";
		public String RETIRED = "R";
		public String EXPIRED = "X";
		public String UNINTENDED = "U";
	}
	
	public interface TRNStatusMessages{
		public String ACTIVE_MESSAGE = "Active";
		public String CLOSED_MESSAGE = "Closed";
		public String DECEASED_MESSAGE = "Deceased";
		public String RETIRED_MESSAGE = "Retired";
		public String EXPIRED_MESSAGE = "Expired";
		public String UNINTENDED_MESSAGE = "Unintended";
	}
	
	public interface OffenceAidAndAbbet{
		public static Integer NO_ROAD_LIC = 35;
		public static Integer NO_PPV_INSUR = 36;
	}
	
	public interface ParishKingstonAndStAndrew{
		public static String KINGSTON = "01";
		public static String ST_ANDREW = "02";
	}

	public static class SecurityRoles {
		public static String ROLE_PREFIX = "ROLE_";
		public static String GENERAL_USER_ROLE = "ROLE_USER";
	}

	public static String formatRefSeqNo(Integer referenceNo) {
		String refSeqNoStr;
		refSeqNoStr = String.format("%09d", referenceNo);
		return refSeqNoStr;
	}
}
