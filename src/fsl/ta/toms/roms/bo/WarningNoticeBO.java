package fsl.ta.toms.roms.bo;

import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.util.NameUtil;

public class WarningNoticeBO extends DocumentViewBO {

	private static final long	serialVersionUID	= -6855615164654140063L;

	public WarningNoticeBO() {
		super();

	}

	Integer				poundId;
	//String				poundName;
	PoundBO				poundBO;
	String				poundAddressNL;

	Boolean				isManualWarningNotice;				
	String				allegation;
	Date				seizureDtime;

	// Wrecker company details
	Integer				wreckingCompanyId;
	WreckingCompanyBO	wreckingCompanyBO;
	String				wreckingCompanyAddressNL;
	
	// person who transport vehicle details
	Integer				vehicleMoverPersonID;
	PersonBO			vehicleMoverBO;
	
	// type of person who move vehicle i.e. police, wrecker company driver etc
	String				vehicleMoverPersonType;
	String				vehicleMoverPersonTypeDescription;

	// details of wrecker vehicle was hooked to
	Integer				wreckerVehicleID;
	VehicleBO			wreckerVehicleBO;

	List<PersonBO>		listOfWitnesses;

	public WarningNoticeBO(Integer roadOperationId, Integer roadCheckOffenceOutcomeCode, Integer offenderId, String taStaffId, Integer poundId, Integer wreckingCompanyId,
		Boolean isManualWarningNotice, String manualSerialNumber, Date seizureDtime, String statusCode, Integer reasonCode, String comment, Character allowUpload, String printUsername,
		Date printDtime, String reprintUsername, Date reprintDtime, Character authorizedFlag, String authorizeUsername, Date authorizeDtime, String issueUsername, Date issueDate,
		List<ScannedDocBO> scannedDocList, String currentUsername, String allegation) {

		super();

		this.roadOperationId = roadOperationId;
		this.roadCheckOffenceOutcomeCode = roadCheckOffenceOutcomeCode;
		this.offenderId = offenderId;
		this.taStaffId = taStaffId;
		this.poundId = poundId;
		this.wreckingCompanyId = wreckingCompanyId;
		this.isManualWarningNotice = isManualWarningNotice;
		this.manualSerialNo = manualSerialNumber;
		this.seizureDtime = seizureDtime;
		this.statusId = statusCode;
		this.reasonCode = reasonCode;
		this.comment = comment;

		if (allowUpload != null)
			this.allowUpload = allowUpload.toString();

		this.printUsername = printUsername;
		this.printDtime = printDtime;
		this.reprintUsername = reprintUsername;
		this.reprintDtime = reprintDtime;
		// this.authorizedFlag = authorizedFlag;
		this.authorizeUsername = authorizeUsername;
		this.authorizeDtime = authorizeDtime;
		this.issueUsername = issueUsername;
		this.issueDate = issueDate;
		this.scannedDocList = scannedDocList;
		this.currentUsername = currentUsername;
		this.documentTypeCode = Constants.DocumentType.WARNING_NOTICE;
		this.documentTypeName = Constants.DocumentTypeLong.WARNING_NOTICE;
		this.allegation = allegation;
	}

	/**
	 * 
	 * @param warningNotice
	 */
	public WarningNoticeBO(WarningNoticeDO warningNotice) {

		super(warningNotice);
		//NameUtil util = new NameUtil();
		if (warningNotice.getPound() != null) {
			this.poundId = warningNotice.getPound().getPoundId();
			//this.poundName = warningNotice.getPound().getPoundName();
			this.poundBO = new PoundBO(warningNotice.getPound());
			
			if (warningNotice.getPound().getAddress() != null) {
				this.poundAddressNL = warningNotice.getPound().getAddress().getAddressLineWithNewLine();				
			}
		}

		if (warningNotice.getWreckingCompany() != null) {
			this.wreckingCompanyId = warningNotice.getWreckingCompany().getWreckingCompanyId();
			this.wreckingCompanyBO = new WreckingCompanyBO(warningNotice.getWreckingCompany());

			if (warningNotice.getWreckingCompany().getAddress() != null) {
				this.wreckingCompanyAddressNL = warningNotice.getWreckingCompany().getAddress().getAddressLineWithNewLine();				
			}
		}

		if (warningNotice.getVehicleDeliveredByPerson() != null) {
			this.vehicleMoverBO = new PersonBO(warningNotice.getVehicleDeliveredByPerson());			
		}
		
		if (warningNotice.getWreckerVehicle() != null) {
			this.wreckerVehicleBO = new VehicleBO(warningNotice.getWreckerVehicle());			
		}

		if (warningNotice.getVehicleDeliveredByPersonType() != null) {
			this.vehicleMoverPersonType = warningNotice.getVehicleDeliveredByPersonType().getPersonTypeId();
			this.vehicleMoverPersonTypeDescription = warningNotice.getVehicleDeliveredByPersonType().getDescription();
		}

		if (origin.equalsIgnoreCase(Constants.DocumentType.ORIGIN_MANUAL))
			setIsManualWarningNotice(true);

		this.seizureDtime = warningNotice.getSeizureDtime();

		this.allegation = warningNotice.getAllegation();

	}

	public Integer getPoundId() {
		return poundId;
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}



	/**
	 * @return the poundBO
	 */
	public PoundBO getPoundBO() {
		return poundBO;
	}

	/**
	 * @param poundBO the poundBO to set
	 */
	public void setPoundBO(PoundBO poundBO) {
		this.poundBO = poundBO;
	}

	/**
	 * @return the poundAddressNL
	 */
	public String getPoundAddressNL() {
		return poundAddressNL;
	}

	/**
	 * @param poundAddressNL the poundAddressNL to set
	 */
	public void setPoundAddressNL(String poundAddressNL) {
		this.poundAddressNL = poundAddressNL;
	}

	public Integer getWreckingCompanyId() {
		return wreckingCompanyId;
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}
	
	public Boolean getIsManualWarningNotice() {
		return isManualWarningNotice;
	}

	public void setIsManualWarningNotice(Boolean isManualWarningNotice) {
		this.isManualWarningNotice = isManualWarningNotice;
	}

	public Date getSeizureDtime() {
		return seizureDtime;
	}

	public void setSeizureDtime(Date seizureDtime) {
		this.seizureDtime = seizureDtime;
	}

	public List<PersonBO> getListOfWitnesses() {
		return listOfWitnesses;
	}

	public void setListOfWitnesses(List<PersonBO> listOfWitnesses) {
		this.listOfWitnesses = listOfWitnesses;
	}

	public String getAllegation() {
		return allegation;
	}

	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}

	public Integer getVehicleMoverPersonID() {
		return vehicleMoverPersonID;
	}

	public void setVehicleMoverPersonID(Integer vehicleMoverPersonID) {
		this.vehicleMoverPersonID = vehicleMoverPersonID;
	}

	public Integer getWreckerVehicleID() {
		return wreckerVehicleID;
	}

	public void setWreckerVehicleID(Integer wreckerVehicleID) {
		this.wreckerVehicleID = wreckerVehicleID;
	}

	public String getVehicleMoverPersonType() {
		return vehicleMoverPersonType;
	}

	public void setVehicleMoverPersonType(String vehicleMoverPersonType) {
		this.vehicleMoverPersonType = vehicleMoverPersonType;
	}	

	/**
	 * @return the vehicleMoverPersonTypeDescription
	 */
	public String getVehicleMoverPersonTypeDescription() {
		return vehicleMoverPersonTypeDescription;
	}

	/**
	 * @param vehicleMoverPersonTypeDescription
	 *            the vehicleMoverPersonTypeDescription to set
	 */
	public void setVehicleMoverPersonTypeDescription(String vehicleMoverPersonTypeDescription) {
		this.vehicleMoverPersonTypeDescription = vehicleMoverPersonTypeDescription;
	}
	
	/**
	 * @return the wreckingCompanyAddressNL
	 */
	public String getWreckingCompanyAddressNL() {
		return wreckingCompanyAddressNL;
	}

	/**
	 * @param wreckingCompanyAddressNL
	 *            the wreckingCompanyAddressNL to set
	 */
	public void setWreckingCompanyAddressNL(String wreckingCompanyAddressNL) {
		this.wreckingCompanyAddressNL = wreckingCompanyAddressNL;
	}

	/**
	 * @return the wreckingCompanyBO
	 */
	public WreckingCompanyBO getWreckingCompanyBO() {
		return wreckingCompanyBO;
	}

	/**
	 * @param wreckingCompanyBO the wreckingCompanyBO to set
	 */
	public void setWreckingCompanyBO(WreckingCompanyBO wreckingCompanyBO) {
		this.wreckingCompanyBO = wreckingCompanyBO;
	}

	/**
	 * @return the vehicleMoverBO
	 */
	public PersonBO getVehicleMoverBO() {
		return vehicleMoverBO;
	}

	/**
	 * @param vehicleMoverBO the vehicleMoverBO to set
	 */
	public void setVehicleMoverBO(PersonBO vehicleMoverBO) {
		this.vehicleMoverBO = vehicleMoverBO;
	}

	/**
	 * @return the wreckerVehicleBO
	 */
	public VehicleBO getWreckerVehicleBO() {
		return wreckerVehicleBO;
	}

	/**
	 * @param wreckerVehicleBO the wreckerVehicleBO to set
	 */
	public void setWreckerVehicleBO(VehicleBO wreckerVehicleBO) {
		this.wreckerVehicleBO = wreckerVehicleBO;
	}



}
