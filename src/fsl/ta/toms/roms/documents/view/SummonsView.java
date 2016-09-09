package fsl.ta.toms.roms.documents.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * 
 * @author jreid Created Date: Sep 26, 2013
 */
public class SummonsView implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5836739088945295396L;
	Integer						summonsId;
	String						manualSerialNumber;

	String						offenderFirstName;
	String						offenderLastName;
	String						offenderTRN;

	String						offenderMiddleName;
	String						offenderAddress;
	String						offenderAddressParish;

	String						taStaffFirstName;
	String						taStaffLastName;

	String						jpFirstName;
	String						jpLastName;
	String						jpParish;

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
	String						offenceYear;
	String						offenceDescription;

	// court
	String						courtName;
	String						courtLocation;
	String						courtAddress;
	String						courtTime;
	String						courtDay;
	String						courtDayWord;
	String						courtMonth;
	String						courtYear;

	// served
	String						servedParish;
	String						servedAddress;
	String						servedTime;
	String						servedDay;
	String						servedMonth;
	String						servedYear;

	String						excerpt;
	String				referenceNo;
	
	
	// for displaying word reprinted on report
		String						reprinted;

	public SummonsView() {
		super();

	}

	/**
	 * Populate summons for Printing
	 * 
	 * @param summonsDO
	 */
	public SummonsView(SummonsDO summonsDO, CourtAppearanceDO courtAppearanceDO) {
		this.summonsId = summonsDO.getSummonsId();
		this.manualSerialNumber = summonsDO.getManualSerialNumber();
		this.referenceNo = Constants.formatRefSeqNo(summonsDO.getReferenceNo());
		
		if(summonsDO.getPrintCount() != null && summonsDO.getPrintCount() > 1)
			this.reprinted = YesNo.YES;

		
		/**
		 * If offence type is Aiding and Abetting Driver etc.(Summons 35 & 36) change offender to the owner		 * 
		 */
//		try{
//			Integer offenceCode = summonsDO.getRoadCheckOffenceOutcome().
//					getRoadCheckOffence().getOffence().getOffenceId();
//		
//			if(offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue()||
//					offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue()){
//				PersonDO personDO = summonsDO.getRoadCheckOffenceOutcome().
//						getRoadCheckOffence().getRoadCheck().getCompliance().getAidAbetVehicleOwner();
//				
//				summonsDO.setOffender(personDO);			
//			}
//		}catch(Exception exception){
//			exception.printStackTrace();
//		}
		
		
		if (summonsDO.getOffender() != null) 
		{
			this.offenderFirstName = StringUtils.trimToEmpty(summonsDO.getOffender().getFirstName());
			this.offenderLastName = StringUtils.trimToEmpty(summonsDO.getOffender().getLastName());
			this.offenderAddress = summonsDO.getOffender().getAddress().getAddressLineWithNewLine();
			this.offenderAddressParish = summonsDO.getOffender().getAddress().getParish() != null ?
						summonsDO.getOffender().getAddress().getParish().getDescription() : "                                ";
			this.offenderMiddleName = StringUtils.trimToEmpty(summonsDO.getOffender().getMiddleName());
			this.offenderTRN = summonsDO.getOffender().getTrnNbr();
		}

		if (summonsDO.getTaStaff() != null) {
			this.taStaffFirstName =(StringUtils.isNotBlank(summonsDO.getTaStaff().getPerson().getFirstName()) ? WordUtils.capitalize(summonsDO.getTaStaff().getPerson().getFirstName().toLowerCase(),Constants.STRING_DELIM_ARRAY) : ""); 
			this.taStaffLastName = (StringUtils.isNotBlank(summonsDO.getTaStaff().getPerson().getLastName()) ? WordUtils.capitalize(summonsDO.getTaStaff().getPerson().getLastName().toLowerCase(),Constants.STRING_DELIM_ARRAY) : ""); 
		}

		if (summonsDO.getJusticeOfPeace() != null) {
			this.jpLastName = summonsDO.getJusticeOfPeace().getPerson().getLastName();
			this.jpFirstName = summonsDO.getJusticeOfPeace().getPerson().getFirstName();

			if (summonsDO.getJusticeOfPeace().getParish() != null)
				this.jpParish = summonsDO.getJusticeOfPeace().getParish().getDescription();
		}

		if (summonsDO.getOffenceDtime() != null) {
			this.offenceDtime = summonsDO.getOffenceDtime();
			this.offenceDayWord = DateUtils.getDayOfWeekFromDate(summonsDO.getOffenceDtime());
			this.offenceDay = DateUtils.getOrdinalDayOfMonthFromDate(summonsDO.getOffenceDtime());
			this.offenceMonth = DateUtils.getMonthFromDate(summonsDO.getOffenceDtime());
			this.offenceYearWord = DateUtils.getPartYearFromDate(summonsDO.getOffenceDtime());
			this.offenceTime = DateUtils.getTimeFromDate(summonsDO.getOffenceDtime());
			this.offenceYear = DateUtils.getYearFromDate(summonsDO.getOffenceDtime());
		}

		if (summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence() != null) {
			/*
			 * this.offenceExcerpt = summonsDO.getRoadCheckOffenceOutcome()
			 * .getRoadCheckOffence().getOffence().getExcerpt();
			 */
			this.offenceDescription = summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getOffence().getDescription();
			this.offenceLocation = summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getShortDescription() + ", "
				+ summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getShortDesc();

			this.offenceParish = summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getCompliancyArtery().getLocation().getParish().getDescription();
		}

		if (courtAppearanceDO != null) {
			if (courtAppearanceDO.getCourt() != null) {
				courtName = courtAppearanceDO.getCourt().getShortDesc();
			}
			if (courtAppearanceDO.getCourt().getAddress() != null) {
				courtLocation = courtAppearanceDO.getCourt().getAddress().getParish().getDescription();
				courtAddress = courtAppearanceDO.getCourt().getAddress().getAddressLine1();
			}

			if (courtAppearanceDO.getCourtDTime() != null) {
				courtTime = DateUtils.getTimeFromDate(courtAppearanceDO.getCourtDTime());
				courtDay = DateUtils.getOrdinalDayOfMonthFromDate(courtAppearanceDO.getCourtDTime());
				courtDayWord = DateUtils.getDayOfWeekFromDate(courtAppearanceDO.getCourtDTime());
				courtMonth = DateUtils.getMonthFromDate(courtAppearanceDO.getCourtDTime());
				;
				courtYear = DateUtils.getYearFromDate(courtAppearanceDO.getCourtDTime());
			}
		}

		if (summonsDO.getServedOnDate() != null) {
			servedParish = // summonsDO.getStreetName();
			servedAddress = "";
			servedTime = DateUtils.getTimeFromDate(summonsDO.getServedOnDate());
			servedDay = DateUtils.getDayOfWeekFromDate(summonsDO.getServedOnDate());
			servedMonth = DateUtils.getMonthFromDate(summonsDO.getServedOnDate());
			servedYear = DateUtils.getPartYearFromDate(summonsDO.getServedOnDate());
		}

		this.excerpt = summonsDO.getExcerpt();
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
		this.manualSerialNumber = manualSerialNumber;
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
	 * @param taStaffLastName
	 *            the taStaffLastName to set
	 */
	public void setTaStaffLastName(String taStaffLastName) {
		this.taStaffLastName = taStaffLastName;
	}

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
	 * @return the courtLocation
	 */
	public String getCourtLocation() {
		return courtLocation;
	}

	/**
	 * @param courtLocation
	 *            the courtLocation to set
	 */
	public void setCourtLocation(String courtLocation) {
		this.courtLocation = courtLocation;
	}

	/**
	 * @return the courtAddress
	 */
	public String getCourtAddress() {
		return courtAddress;
	}

	/**
	 * @param courtAddress
	 *            the courtAddress to set
	 */
	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}

	/**
	 * @return the courtDay
	 */
	public String getCourtDay() {
		return courtDay;
	}

	/**
	 * @param courtDay
	 *            the courtDay to set
	 */
	public void setCourtDay(String courtDay) {
		this.courtDay = courtDay;
	}

	/**
	 * @return the courtMonth
	 */
	public String getCourtMonth() {
		return courtMonth;
	}

	/**
	 * @param courtMonth
	 *            the courtMonth to set
	 */
	public void setCourtMonth(String courtMonth) {
		this.courtMonth = courtMonth;
	}

	/**
	 * @return the courtYear
	 */
	public String getCourtYear() {
		return courtYear;
	}

	/**
	 * @param courtYear
	 *            the courtYear to set
	 */
	public void setCourtYear(String courtYear) {
		this.courtYear = courtYear;
	}

	/**
	 * @return the servedParish
	 */
	public String getServedParish() {
		return servedParish;
	}

	/**
	 * @param servedParish
	 *            the servedParish to set
	 */
	public void setServedParish(String servedParish) {
		this.servedParish = servedParish;
	}

	/**
	 * @return the servedAddress
	 */
	public String getServedAddress() {
		return servedAddress;
	}

	/**
	 * @param servedAddress
	 *            the servedAddress to set
	 */
	public void setServedAddress(String servedAddress) {
		this.servedAddress = servedAddress;
	}

	/**
	 * @return the servedTime
	 */
	public String getServedTime() {
		return servedTime;
	}

	/**
	 * @param servedTime
	 *            the servedTime to set
	 */
	public void setServedTime(String servedTime) {
		this.servedTime = servedTime;
	}

	/**
	 * @return the servedDay
	 */
	public String getServedDay() {
		return servedDay;
	}

	/**
	 * @param servedDay
	 *            the servedDay to set
	 */
	public void setServedDay(String servedDay) {
		this.servedDay = servedDay;
	}

	/**
	 * @return the servedMonth
	 */
	public String getServedMonth() {
		return servedMonth;
	}

	/**
	 * @param servedMonth
	 *            the servedMonth to set
	 */
	public void setServedMonth(String servedMonth) {
		this.servedMonth = servedMonth;
	}

	/**
	 * @return the servedYear
	 */
	public String getServedYear() {
		return servedYear;
	}

	/**
	 * @param servedYear
	 *            the servedYear to set
	 */
	public void setServedYear(String servedYear) {
		this.servedYear = servedYear;
	}

	public String getOffenderAddressParish() {
		return offenderAddressParish;
	}

	public void setOffenderAddressParish(String offenderAddressParish) {
		this.offenderAddressParish = offenderAddressParish;
	}

	public String getOffenceYear() {
		return offenceYear;
	}

	public void setOffenceYear(String offenceYear) {
		this.offenceYear = offenceYear;
	}

	public String getCourtDayWord() {
		return courtDayWord;
	}

	public void setCourtDayWord(String courtDayWord) {
		this.courtDayWord = courtDayWord;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getOffenceExcerpt() {
		return offenceExcerpt;
	}

	public void setOffenceExcerpt(String offenceExcerpt) {
		this.offenceExcerpt = offenceExcerpt;
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
	 * @return the offenderTRN
	 */
	public String getOffenderTRN() {
		return offenderTRN;
	}

	/**
	 * @param offenderTRN the offenderTRN to set
	 */
	public void setOffenderTRN(String offenderTRN) {
		this.offenderTRN = offenderTRN;
	}

	/**
	 * @return the offenderMiddleName
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * @param offenderMiddleName the offenderMiddleName to set
	 */
	public void setOffenderMiddleName(String offenderMiddleName) {
		this.offenderMiddleName = offenderMiddleName;
	}
	
	/**
	 * @return the offenceDescription
	 */
	public String getOffenceDescription() {
		return offenceDescription;
	}

	/**
	 * @param offenceDescription the offenceDescription to set
	 */
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

}
