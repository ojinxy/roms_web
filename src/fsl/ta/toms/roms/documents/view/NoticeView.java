package fsl.ta.toms.roms.documents.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.service.RoadCompliancyService;
import fsl.ta.toms.roms.service.impl.RoadCompliancyServiceImpl;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.NameUtil;
import fsl.ta.toms.roms.util.StringUtil;
import fsl.ta.toms.roms.webservices.RoadCompliancy;

/**
 * 
 * @author jreid Created Date: Sep 26, 2013
 */
public class NoticeView implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -9005353375633823225L;
	Integer						automaticSerialNo;
	String						manualSerialNo;
	String						referenceNo;

	// offender
	String						offenderFirstName;
	String						offenderLastName;
	String						offenderMiddleName;
	String						offenderFullName;
	String						offenderAddress;
	String						offenderTRN;
	String						offenderDriverLicenceNo;

	// owner
	String						ownerFirstName;
	String						ownerLastName;
	String						ownerMiddleName;
	String						ownerFullName;
	String						ownerAddress;

	// ta staff
	String						taStaffFirstName;
	String						taStaffLastName;
	String						taStaffFullName;
	String						taStaffBadgeNo;
	String						taStaffAssignedLocation;

	// offence
	Date						offenceDtime;
	String						offenceLocation;
	String						offenceExcerpt;
	String						offenceDayWord;
	String						offenceDay;
	String						offenceMonth;
	String						offenceYearWord;
	String						offenceTime;
	String						offenceParish;
	String						allegations;

	Date						issueDate;

	// vehicle details
	String						vehicleType;
	String						vehicleMake;
	String						vehicleModel;
	String						vehiclePlateNo;
	String						vehicleColor;
	String						vehicleChassisNo;
	String						vehicleEngineNo;

	// pound Info
	String						poundAddress;
	String						poundName;

	// wrecking company
	String						wreckingCompanyName;
	
	// wrecker vehicle
	String						wreckerPlateNo;

	// witness details
	String						witnessFirstName;
	String						witnessLastName;
	String						witnessFullName;
	String						witnessAddress;

	// for each string[], [0] is name, [1] is address
	List<String[]>				witnessesDetails;

	// for displaying word reprinted on report
	String						reprinted;

	public NoticeView() {
		super();

	}

	/**
	 * Populate warningNotice for Printing
	 * 
	 * @param warningNoticeDO
	 */
	public NoticeView(WarningNoticeDO warningNoticeDO) {
		super();
		this.automaticSerialNo = warningNoticeDO.getWarningNoticeId();
		this.manualSerialNo = warningNoticeDO.getManualSerialNumber();
		this.referenceNo = Constants.formatRefSeqNo(warningNoticeDO.getReferenceNo());
		NameUtil nameUtil = new NameUtil();

		if (warningNoticeDO.getOffender() != null) {
			this.offenderFirstName = StringUtils.trimToEmpty(warningNoticeDO.getOffender().getFirstName());
			this.offenderLastName = StringUtils.trimToEmpty(warningNoticeDO.getOffender().getLastName());
			this.offenderMiddleName = StringUtils.trimToEmpty(warningNoticeDO.getOffender().getMiddleName());

			this.offenderFullName = StringUtils.trimToEmpty(nameUtil.toString(offenderFirstName, offenderLastName, offenderMiddleName));

			this.offenderAddress = WordUtils.capitalize(warningNoticeDO.getOffender().getAddress().getAddressLineWithNewLine(), Constants.STRING_DELIM_ARRAY);
			this.offenderTRN = warningNoticeDO.getOffender().getTrnNbr();
			RoadCompliancy roadCompliancy = new RoadCompliancy();
			try {
				DLCheckResultBO dlCheckResultBO = new DLCheckResultBO();
				dlCheckResultBO= (roadCompliancy
						.getComplianceDetails(warningNoticeDO
								.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getComplianceId()))
						.getDlCheckResult();
				
				this.offenderDriverLicenceNo = dlCheckResultBO != null ? dlCheckResultBO
						.getDlNo() : null;
			} catch (InvalidFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Offender DLN:"+this.offenderDriverLicenceNo);
		}

		if (warningNoticeDO.getTaStaff() != null) {
			this.taStaffFirstName = (StringUtils.isNotBlank(warningNoticeDO
					.getTaStaff().getPerson().getFirstName()) ? WordUtils
					.capitalize(warningNoticeDO.getTaStaff().getPerson()
							.getFirstName().toLowerCase(),
							Constants.STRING_DELIM_ARRAY) : "");
			this.taStaffLastName = (StringUtils.isNotBlank(warningNoticeDO
					.getTaStaff().getPerson().getLastName()) ? WordUtils
					.capitalize(warningNoticeDO.getTaStaff().getPerson()
							.getLastName().toLowerCase(),
							Constants.STRING_DELIM_ARRAY) : "");

			this.taStaffFullName = WordUtils.capitalize(
					NameUtil.getProperName(taStaffFirstName.toLowerCase(), taStaffLastName.toLowerCase()));

			this.taStaffAssignedLocation = warningNoticeDO.getTaStaff().getOfficeLocationCode();
			this.taStaffBadgeNo = warningNoticeDO.getTaStaff().getStaffId(); // badge
																				// no?
		}
		
		if(warningNoticeDO.getPrintCount() != null && warningNoticeDO.getPrintCount() > 1)
			this.reprinted = YesNo.YES;

		if (warningNoticeDO.getRoadCheckOffenceOutcome() != null) {
			Date offenceDTime = warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence()
								.getRoadCheck().getCompliance().getOffenceDateTime();
			this.offenceDtime = offenceDTime;
			this.offenceDayWord = DateUtils.getDayOfWeekFromDate(offenceDTime);
			this.offenceDay = DateUtils.getOrdinalDayOfMonthFromDate(offenceDTime);
			this.offenceYearWord = DateUtils.getYearFromDate(offenceDTime);
			this.offenceTime = DateUtils.getTimeFromDate(offenceDTime);
			this.offenceMonth = DateUtils.getMonthFromDate(offenceDTime);

		}

		if (warningNoticeDO.getAllegation() != null)
			this.allegations = WordUtils.capitalize(warningNoticeDO.getAllegation(), Constants.STRING_DELIM_ARRAY);

		if (warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence() != null) {
			/*
			 * this.offenceExcerpt =
			 * warningNoticeDO.getRoadCheckOffenceOutcome()
			 * .getRoadCheckOffence().getOffence().getExcerpt();
			 */
			String artery, location;
			artery = warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getShortDescription();
			location = warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getShortDesc();
			this.offenceLocation = (StringUtils.isNotBlank(artery) ? artery : "") + (StringUtils.isNotBlank(artery) && StringUtils.isNotBlank(location) ? ", " : "")
				+ (StringUtils.isNotBlank(location) ? location : "");
			this.offenceParish = warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getParish().getDescription();
		}

		if (warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getVehicle() != null) {

			VehicleDO vehicle = warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getVehicle();

			// vehicle
			if (vehicle.getTypeDesc() != null)
				vehicleType = WordUtils.capitalize(vehicle.getTypeDesc());

			if (vehicle.getMakeDescription() != null)
				vehicleMake = WordUtils.capitalize(vehicle.getMakeDescription());

			if (vehicle.getModel() != null)
				vehicleModel = WordUtils.capitalize(vehicle.getModel());

			vehiclePlateNo = vehicle.getPlateRegNo().toUpperCase();
			vehicleColor = vehicle.getColour();
			vehicleChassisNo = vehicle.getChassisNo();
			vehicleEngineNo = vehicle.getEngineNo();

		}

		if (warningNoticeDO.getPound() != null) {
			this.poundName = warningNoticeDO.getPound().getPoundName();

			if (warningNoticeDO.getPound().getAddress() != null)
				this.poundAddress = WordUtils.capitalize(warningNoticeDO.getPound().getAddress().getAddressLineWithCommas(), Constants.STRING_DELIM_ARRAY);

		}

		if (warningNoticeDO.getWreckingCompany() != null) {
			this.wreckingCompanyName = warningNoticeDO.getWreckingCompany().getCompanyName();
		}
		
		if(warningNoticeDO.getWreckerVehicle() != null){
			this.wreckerPlateNo = warningNoticeDO.getWreckerVehicle().getPlateRegNo().toUpperCase();
		}

	}

	public NoticeView(WarningNoProsecutionDO warningNoProsecutionDO) {
		this.automaticSerialNo = warningNoProsecutionDO.getWarningNoProsecutionID();
		this.manualSerialNo = warningNoProsecutionDO.getManualSerialNumber();
		this.referenceNo = Constants.formatRefSeqNo(warningNoProsecutionDO.getReferenceNo());
		NameUtil nameUtil = new NameUtil();

		if (warningNoProsecutionDO.getOffender() != null) {
			this.offenderFirstName = warningNoProsecutionDO.getOffender().getFirstName();
			this.offenderLastName = warningNoProsecutionDO.getOffender().getLastName();
			this.offenderMiddleName = warningNoProsecutionDO.getOffender().getMiddleName();

			this.offenderFullName = nameUtil.toString(offenderFirstName, offenderLastName, offenderMiddleName);

			this.offenderAddress = WordUtils.capitalize(warningNoProsecutionDO.getOffender().getAddress().getAddressLineWithNewLine(), Constants.STRING_DELIM_ARRAY);

			this.offenderTRN = warningNoProsecutionDO.getOffender().getTrnNbr();
			RoadCompliancy roadCompliancy = new RoadCompliancy();
			try {
				DLCheckResultBO dlCheckResultBO = new DLCheckResultBO();
				dlCheckResultBO= (roadCompliancy
						.getComplianceDetails(warningNoProsecutionDO
								.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getComplianceId()))
						.getDlCheckResult();
				
				this.offenderDriverLicenceNo = dlCheckResultBO != null ? dlCheckResultBO
						.getDlNo() : null;
			} catch (InvalidFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (warningNoProsecutionDO.getTaStaff() != null) {
			this.taStaffFirstName = (StringUtils.isNotBlank(warningNoProsecutionDO
					.getTaStaff().getPerson().getFirstName()) ? WordUtils
					.capitalize(warningNoProsecutionDO.getTaStaff().getPerson()
							.getFirstName().toLowerCase(),
							Constants.STRING_DELIM_ARRAY) : "");
			this.taStaffLastName = (StringUtils.isNotBlank(warningNoProsecutionDO
					.getTaStaff().getPerson().getLastName()) ? WordUtils
					.capitalize(warningNoProsecutionDO.getTaStaff().getPerson()
							.getLastName().toLowerCase(),
							Constants.STRING_DELIM_ARRAY) : "");

			this.taStaffFullName = WordUtils.capitalize(nameUtil.getProperName(
					taStaffFirstName.toLowerCase(), taStaffLastName.toLowerCase()));

			this.taStaffAssignedLocation = warningNoProsecutionDO.getTaStaff().getOfficeLocationCode();
			this.taStaffBadgeNo = warningNoProsecutionDO.getTaStaff().getStaffId(); // badge
																					// no?
		}
		
		if(warningNoProsecutionDO.getPrintCount() != null && warningNoProsecutionDO.getPrintCount() > 1)
			this.reprinted = YesNo.YES;

		if (warningNoProsecutionDO.getRoadCheckOffenceOutcome() != null) {
			Date offenceDTime = warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence()
								.getRoadCheck().getCompliance().getOffenceDateTime();
			this.offenceDtime = offenceDTime;
			this.offenceDayWord = DateUtils.getDayOfWeekFromDate(offenceDTime);
			this.offenceDay = DateUtils.getOrdinalDayOfMonthFromDate(offenceDTime);
			this.offenceYearWord = DateUtils.getYearFromDate(offenceDTime);
			this.offenceTime = DateUtils.getTimeFromDate(offenceDTime);
			this.offenceMonth = DateUtils.getMonthFromDate(offenceDTime);
		}

		if (warningNoProsecutionDO.getAllegation() != null)
			this.allegations = WordUtils.capitalize(warningNoProsecutionDO.getAllegation(), Constants.STRING_DELIM_ARRAY);

		if (warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence() != null) {
			// this.offenceExcerpt =formattOffences(offence);
			String artery, location;
			artery = warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getDescription();
			location = warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getShortDesc();
			this.offenceLocation = (StringUtils.isNotBlank(artery) ? artery : "") + (StringUtils.isNotBlank(artery) && StringUtils.isNotBlank(location) ? ", " : "")
				+ (StringUtils.isNotBlank(location) ? location : "");
			this.offenceParish = warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getParish()
				.getDescription();
		}

		VehicleDO vehicle = warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getVehicle();

		if (vehicle != null) {

			// vehicle
			if (vehicle.getTypeDesc() != null)
				vehicleType = WordUtils.capitalize(vehicle.getTypeDesc());

			if (vehicle.getMakeDescription() != null)
				vehicleMake = WordUtils.capitalize(vehicle.getMakeDescription());

			if (vehicle.getModel() != null)
				vehicleModel = WordUtils.capitalize(vehicle.getModel());

			vehiclePlateNo = vehicle.getPlateRegNo().toUpperCase();
			vehicleColor = vehicle.getColour();
			vehicleChassisNo = vehicle.getChassisNo();
			vehicleEngineNo = vehicle.getEngineNo();
			
			
		}

		
	}

	public String getManualSerialNo() {
		return manualSerialNo;
	}

	public void setManualSerialNo(String manualSerialNo) {
		this.manualSerialNo = manualSerialNo;
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
	 * @return the offenderAddress
	 */
	public String getOffenderAddress() {
		return offenderAddress;
	}

	/**
	 * @param offenderAddress
	 *            the offenderAddress to set
	 */
	public void setOffenderAddress(String offenderAddress) {
		this.offenderAddress = offenderAddress;
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
	 * @return the offenderDriverLicenceNo
	 */
	public String getOffenderDriverLicenceNo() {
		return offenderDriverLicenceNo;
	}

	/**
	 * @param offenderDriverLicenceNo
	 *            the offenderDriverLicenceNo to set
	 */
	public void setOffenderDriverLicenceNo(String offenderDriverLicenceNo) {
		this.offenderDriverLicenceNo = offenderDriverLicenceNo;
	}

	/**
	 * @return the ownerFirstName
	 */
	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	/**
	 * @param ownerFirstName
	 *            the ownerFirstName to set
	 */
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	/**
	 * @return the ownerLastName
	 */
	public String getOwnerLastName() {
		return ownerLastName;
	}

	/**
	 * @param ownerLastName
	 *            the ownerLastName to set
	 */
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	/**
	 * @return the ownerMiddleName
	 */
	public String getOwnerMiddleName() {
		return ownerMiddleName;
	}

	/**
	 * @param ownerMiddleName
	 *            the ownerMiddleName to set
	 */
	public void setOwnerMiddleName(String ownerMiddleName) {
		this.ownerMiddleName = ownerMiddleName;
	}

	/**
	 * @return the ownerAddress
	 */
	public String getOwnerAddress() {
		return ownerAddress;
	}

	/**
	 * @param ownerAddress
	 *            the ownerAddress to set
	 */
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	/**
	 * @return the taStaffBadgeNo
	 */
	public String getTaStaffBadgeNo() {
		return taStaffBadgeNo;
	}

	/**
	 * @param taStaffBadgeNo
	 *            the taStaffBadgeNo to set
	 */
	public void setTaStaffBadgeNo(String taStaffBadgeNo) {
		this.taStaffBadgeNo = taStaffBadgeNo;
	}

	/**
	 * @return the taStaffAssignedLocation
	 */
	public String getTaStaffAssignedLocation() {
		return taStaffAssignedLocation;
	}

	/**
	 * @param taStaffAssignedLocation
	 *            the taStaffAssignedLocation to set
	 */
	public void setTaStaffAssignedLocation(String taStaffAssignedLocation) {
		this.taStaffAssignedLocation = taStaffAssignedLocation;
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
	 * @return the offenceExcerpt
	 */
	public String getOffenceExcerpt() {
		return offenceExcerpt;
	}

	/**
	 * @param offenceExcerpt
	 *            the offenceExcerpt to set
	 */
	public void setOffenceExcerpt(String offenceExcerpt) {
		this.offenceExcerpt = offenceExcerpt;
	}

	/**
	 * @return the offenceDayWord
	 */
	public String getOffenceDayWord() {
		return offenceDayWord;
	}

	/**
	 * @param offenceDayWord
	 *            the offenceDayWord to set
	 */
	public void setOffenceDayWord(String offenceDayWord) {
		this.offenceDayWord = offenceDayWord;
	}

	/**
	 * @return the offenceDay
	 */
	public String getOffenceDay() {
		return offenceDay;
	}

	/**
	 * @param offenceDay
	 *            the offenceDay to set
	 */
	public void setOffenceDay(String offenceDay) {
		this.offenceDay = offenceDay;
	}

	/**
	 * @return the offenceYearWord
	 */
	public String getOffenceYearWord() {
		return offenceYearWord;
	}

	/**
	 * @param offenceYearWord
	 *            the offenceYearWord to set
	 */
	public void setOffenceYearWord(String offenceYearWord) {
		this.offenceYearWord = offenceYearWord;
	}

	/**
	 * @return the offenceTime
	 */
	public String getOffenceTime() {
		return offenceTime;
	}

	/**
	 * @param offenceTime
	 *            the offenceTime to set
	 */
	public void setOffenceTime(String offenceTime) {
		this.offenceTime = offenceTime;
	}

	/**
	 * @return the offenceParish
	 */
	public String getOffenceParish() {
		return offenceParish;
	}

	/**
	 * @param offenceParish
	 *            the offenceParish to set
	 */
	public void setOffenceParish(String offenceParish) {
		this.offenceParish = offenceParish;
	}

	/**
	 * @return the allegations
	 */
	public String getAllegations() {
		return allegations;
	}

	/**
	 * @param allegations
	 *            the allegations to set
	 */
	public void setAllegations(String allegations) {
		this.allegations = allegations;
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
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType
	 *            the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return the vehicleMake
	 */
	public String getVehicleMake() {
		return vehicleMake;
	}

	/**
	 * @param vehicleMake
	 *            the vehicleMake to set
	 */
	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	/**
	 * @return the vehicleModel
	 */
	public String getVehicleModel() {
		return vehicleModel;
	}

	/**
	 * @param vehicleModel
	 *            the vehicleModel to set
	 */
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	/**
	 * @return the vehiclePlateNo
	 */
	public String getVehiclePlateNo() {
		return vehiclePlateNo;
	}

	/**
	 * @param vehiclePlateNo
	 *            the vehiclePlateNo to set
	 */
	public void setVehiclePlateNo(String vehiclePlateNo) {
		this.vehiclePlateNo = vehiclePlateNo;
	}

	/**
	 * @return the vehicleColor
	 */
	public String getVehicleColor() {
		return vehicleColor;
	}

	/**
	 * @param vehicleColor
	 *            the vehicleColor to set
	 */
	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	/**
	 * @return the vehicleChassisNo
	 */
	public String getVehicleChassisNo() {
		return vehicleChassisNo;
	}

	/**
	 * @param vehicleChassisNo
	 *            the vehicleChassisNo to set
	 */
	public void setVehicleChassisNo(String vehicleChassisNo) {
		this.vehicleChassisNo = vehicleChassisNo;
	}

	/**
	 * @return the vehicleEngineNo
	 */
	public String getVehicleEngineNo() {
		return vehicleEngineNo;
	}

	/**
	 * @param vehicleEngineNo
	 *            the vehicleEngineNo to set
	 */
	public void setVehicleEngineNo(String vehicleEngineNo) {
		this.vehicleEngineNo = vehicleEngineNo;
	}

	/**
	 * @param taStaffLastName
	 *            the taStaffLastName to set
	 */
	public void setTaStaffLastName(String taStaffLastName) {
		this.taStaffLastName = taStaffLastName;
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
	 * @return the ownerFullName
	 */
	public String getOwnerFullName() {
		return ownerFullName;
	}

	/**
	 * @param ownerFullName
	 *            the ownerFullName to set
	 */
	public void setOwnerFullName(String ownerFullName) {
		this.ownerFullName = ownerFullName;
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
	 * @return the offenceMonth
	 */
	public String getOffenceMonth() {
		return offenceMonth;
	}

	/**
	 * @param offenceMonth
	 *            the offenceMonth to set
	 */
	public void setOffenceMonth(String offenceMonth) {
		this.offenceMonth = offenceMonth;
	}

	/**
	 * @return the poundAddress
	 */
	public String getPoundAddress() {
		return poundAddress;
	}

	/**
	 * @param poundAddress
	 *            the poundAddress to set
	 */
	public void setPoundAddress(String poundAddress) {
		this.poundAddress = poundAddress;
	}

	/**
	 * @return the poundName
	 */
	public String getPoundName() {
		return poundName;
	}

	/**
	 * @param poundName
	 *            the poundName to set
	 */
	public void setPoundName(String poundName) {
		this.poundName = poundName;
	}

	/**
	 * @return the wreckingCompanyName
	 */
	public String getWreckingCompanyName() {
		return wreckingCompanyName;
	}

	/**
	 * @param wreckingCompanyName
	 *            the wreckingCompanyName to set
	 */
	public void setWreckingCompanyName(String wreckingCompanyName) {
		this.wreckingCompanyName = wreckingCompanyName;
	}

	/**
	 * @return the witnessFirstName
	 */
	public String getWitnessFirstName() {
		return witnessFirstName;
	}

	/**
	 * @param witnessFirstName
	 *            the witnessFirstName to set
	 */
	public void setWitnessFirstName(String witnessFirstName) {
		this.witnessFirstName = witnessFirstName;
	}

	/**
	 * @return the witnessLastName
	 */
	public String getWitnessLastName() {
		return witnessLastName;
	}

	/**
	 * @param witnessLastName
	 *            the witnessLastName to set
	 */
	public void setWitnessLastName(String witnessLastName) {
		this.witnessLastName = witnessLastName;
	}

	/**
	 * @return the witnessFullName
	 */
	public String getWitnessFullName() {
		return witnessFullName;
	}

	/**
	 * @param witnessFullName
	 *            the witnessFullName to set
	 */
	public void setWitnessFullName(String witnessFullName) {
		this.witnessFullName = witnessFullName;
	}

	/**
	 * @return the witnessAddress
	 */
	public String getWitnessAddress() {
		return witnessAddress;
	}

	/**
	 * @param witnessAddress
	 *            the witnessAddress to set
	 */
	public void setWitnessAddress(String witnessAddress) {
		this.witnessAddress = witnessAddress;
	}

	/**
	 * @return the witnessesDetails
	 */
	public List<String[]> getWitnessesDetails() {
		return witnessesDetails;
	}

	/**
	 * @param witnessesDetails
	 *            the witnessesDetails to set
	 */
	public void setWitnessesDetails(List<String[]> witnessesDetails) {
		this.witnessesDetails = witnessesDetails;
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
	 * @return the reprinted
	 */
	public String getReprinted() {
		return reprinted;
	}

	/**
	 * @param reprinted
	 *            the reprinted to set
	 */
	public void setReprinted(String reprinted) {
		this.reprinted = reprinted;
	}

	/**
	 * 
	 * @param offences
	 * @return
	 */
	private String formattOffences(List<OffenceDO> offences) {
		String offenceS = null;

		for (OffenceDO offence : offences) {
			if (offenceS != null) {
				offenceS = offenceS + offence.getDescription() + '\n';
			} else {
				offenceS = offence.getDescription() + '\n';
			}
		}

		return offenceS;
	}

	/**
	 * @return the wreckerPlateNo
	 */
	public String getWreckerPlateNo() {
		return wreckerPlateNo;
	}

	/**
	 * @param wreckerPlateNo the wreckerPlateNo to set
	 */
	public void setWreckerPlateNo(String wreckerPlateNo) {
		this.wreckerPlateNo = wreckerPlateNo;
	}

}
