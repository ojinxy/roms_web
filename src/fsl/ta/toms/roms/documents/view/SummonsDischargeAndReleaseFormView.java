package fsl.ta.toms.roms.documents.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * POPULATE RELEASE AND DISCHARGE FORM
 * @author jreid
 * Created Date: Sep 30, 2013
 */
public class SummonsDischargeAndReleaseFormView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3127778251203519017L;
	
	Integer summonsId;
	String manualSerialNumber;
	
	String offenderFirstName;
	String offenderLastName;
	String offenderAddress;

	//witness
	String witnessFirstName;
	String witnessLastName;
	String witnessOccupation;
	String witnessAddressString;	
	
	//vehicle
	String licencePlate;
	
	// offence
	Date offenceDtime;	
	String offenceMonthWord;
	String offenceDay;
	String offenceYearWord;	
	
	//for displaying word reprinted on report 
	String reprinted;
		
	public SummonsDischargeAndReleaseFormView() {
		super();		
	}
	
	/**
	 * 
	 * @param summonsDO
	 */
	public SummonsDischargeAndReleaseFormView(SummonsDO summonsDO) {
		
		this.summonsId = summonsDO.getSummonsId();
		this.manualSerialNumber = summonsDO.getManualSerialNumber();		

		if (summonsDO.getOffender() != null){
			this.offenderFirstName = StringUtils.trimToEmpty(summonsDO.getOffender().getFirstName());
			this.offenderLastName = StringUtils.trimToEmpty(summonsDO.getOffender().getLastName());
			this.offenderAddress = summonsDO.getOffender().getAddress().getAddressLine2();		
		}

		if (summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getVehicle().getPlateRegNo() != null){
			this.licencePlate = summonsDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getVehicle().getPlateRegNo().toUpperCase();
		}

		if (summonsDO.getDischargeWitness() != null){
			this.witnessLastName = summonsDO.getDischargeWitness().getLastName();
			this.witnessFirstName = summonsDO.getDischargeWitness().getFirstName();
			this.witnessOccupation = summonsDO.getDischargeWitness().getOccupation();
			this.witnessAddressString = summonsDO.getDischargeWitness().getAddress().getAddressLineWithNewLine();
		}

		if(summonsDO.getOffenceDtime() != null){
			this.offenceDtime = summonsDO.getOffenceDtime();
			this.offenceMonthWord = DateUtils.getMonthFromDate(summonsDO.getOffenceDtime());
			this.offenceDay= DateUtils.getOrdinalDayOfMonthFromDate(summonsDO.getOffenceDtime());
			this.offenceYearWord= DateUtils.getYearFromDate(summonsDO.getOffenceDtime());							
		}
		
		
	}

	/**
	 * @return the witnessOccupation
	 */
	public String getWitnessOccupation() {
		return witnessOccupation;
	}

	/**
	 * @param witnessOccupation the witnessOccupation to set
	 */
	public void setWitnessOccupation(String witnessOccupation) {
		this.witnessOccupation = witnessOccupation;
	}

	/**
	 * @return the summonsId
	 */
	public Integer getSummonsId() {
		return summonsId;
	}
	/**
	 * @param summonsId the summonsId to set
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
	 * @param manualSerialNumber the manualSerialNumber to set
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
	 * @param offenderFirstName the offenderFirstName to set
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
	 * @param offenderLastName the offenderLastName to set
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
	 * @param offenderAddress the offenderAddress to set
	 */
	public void setOffenderAddress(String offenderAddress) {
		this.offenderAddress = offenderAddress;
	}
	/**
	 * @return the witnessFirstName
	 */
	public String getWitnessFirstName() {
		return witnessFirstName;
	}
	/**
	 * @param witnessFirstName the witnessFirstName to set
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
	 * @param witnessLastName the witnessLastName to set
	 */
	public void setWitnessLastName(String witnessLastName) {
		this.witnessLastName = witnessLastName;
	}
	/**
	 * @return the witnessAddressString
	 */
	public String getWitnessAddressString() {
		return witnessAddressString;
	}
	/**
	 * @param witnessAddressString the witnessAddressString to set
	 */
	public void setWitnessAddressString(String witnessAddressString) {
		this.witnessAddressString = witnessAddressString;
	}
	/**
	 * @return the licencePlate
	 */
	public String getLicencePlate() {
		return licencePlate;
	}
	/**
	 * @param licencePlate the licencePlate to set
	 */
	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}
	/**
	 * @return the offenceDtime
	 */
	public Date getOffenceDtime() {
		return offenceDtime;
	}
	/**
	 * @param offenceDtime the offenceDtime to set
	 */
	public void setOffenceDtime(Date offenceDtime) {
		this.offenceDtime = offenceDtime;
	}
	
	/**
	 * @return the offenceMonthWord
	 */
	public String getOffenceMonthWord() {
		return offenceMonthWord;
	}

	/**
	 * @param offenceMonthWord the offenceMonthWord to set
	 */
	public void setOffenceMonthWord(String offenceMonthWord) {
		this.offenceMonthWord = offenceMonthWord;
	}

	/**
	 * @return the offenceDay
	 */
	public String getOffenceDay() {
		return offenceDay;
	}
	/**
	 * @param offenceDay the offenceDay to set
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
	 * @param offenceYearWord the offenceYearWord to set
	 */
	public void setOffenceYearWord(String offenceYearWord) {
		this.offenceYearWord = offenceYearWord;
	}

	/**
	 * @return the reprinted
	 */
	public String getReprinted() {
		return reprinted;
	}

	/**
	 * @param reprinted the reprinted to set
	 */
	public void setReprinted(String reprinted) {
		this.reprinted = reprinted;
	}

	

}
