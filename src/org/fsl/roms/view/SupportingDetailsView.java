package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.fsl.converter.PersonBOForRoadCompConverter;
import org.primefaces.event.SelectEvent;

import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;

public class SupportingDetailsView   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2340718151169809095L;
	private Integer poundId;
	private Integer wreckingCompanyId;

	private String  vehicleMoverType;
	
	private PersonBO vehicleMoverBO;
	
	private PersonBO vehicleMoverWreckerBO;
	private PersonBOForRoadCompConverter vehicleMoverTABO;
	private PersonBOForRoadCompConverter vehicleMoverITABO;
	private PersonBOForRoadCompConverter vehicleMoverPOBO;
	private PersonBOForRoadCompConverter vehicleMoverOTBO;
	
	private VehicleBO wreckerVehicleBO;
	
	private String vehicleMoverDLN;
	
	private String wreckerVehRegNum;
	
	private Integer courtId;
	private Date courtDate;
	private Date courtTime;
	private Date courtDateTime;
	
	private String comment;
	
	private boolean regionalOperation;
	
	private List<WitnessView> witnessList;
	private WitnessView currentWitness;
	private WitnessView selectedWitness;
	private boolean showWitnessDialog;
	private boolean updateWitness;
	private boolean selectAllWitness;
	
	private boolean directiveParamRequired;
	private boolean inspectorParamRequired;
	private boolean noOfPassengersRequired;
	
	private String directiveParam;
	private TAStaffBO inspectorParam;
	private Integer noOfPassengers;
	
	private boolean allegationRequired;
	private String allegation;
	
	private HashMap<String, String> offenceParamMap;

	private VehicleOwnerBO owner;
	
	private boolean issueToOwner;
	
	private String selectedWreckerParishCode,selectedPoundParishCode;
	
	private String prevWreckerPlateRegNo;
	
	public SupportingDetailsView() {
		super();
		this.witnessList = new ArrayList<WitnessView>();
		this.currentWitness = new WitnessView();
		this.selectedWitness = new WitnessView();
		this.updateWitness=false;
		this.inspectorParam = new TAStaffBO();
		this.vehicleMoverBO = new PersonBO();
		this.vehicleMoverITABO = new PersonBOForRoadCompConverter();
		this.vehicleMoverOTBO = new PersonBOForRoadCompConverter();
		this.vehicleMoverPOBO = new PersonBOForRoadCompConverter();
		this.vehicleMoverTABO = new PersonBOForRoadCompConverter();
		this.vehicleMoverWreckerBO = new PersonBOForRoadCompConverter();
		this.wreckerVehicleBO = new VehicleBO();
		this.prevWreckerPlateRegNo = "";
	}

	public Integer getPoundId() {
		return poundId;
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}

	public Integer getWreckingCompanyId() {
		return wreckingCompanyId;
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public Date getCourtTime() {
		return courtTime;
	}

	public void setCourtTime(Date courtTime) {
		this.courtTime = courtTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRegionalOperation() {
		return regionalOperation;
	}

	public void setRegionalOperation(boolean regionalOperation) {
		this.regionalOperation = regionalOperation;
	}

	public Date getCourtDateTime() {
		return courtDateTime;
	}

	public void setCourtDateTime(Date courtDateTime) {
		this.courtDateTime = courtDateTime;
	}

	public List<WitnessView> getWitnessList() {
		return witnessList;
	}

	public void setWitnessList(List<WitnessView> witnessList) {
		this.witnessList = witnessList;
	}

	public boolean isShowWitnessDialog() {
		return showWitnessDialog;
	}

	public void setShowWitnessDialog(boolean showWitnessDialog) {
		this.showWitnessDialog = showWitnessDialog;
	}

	public WitnessView getCurrentWitness() {
		return currentWitness;
	}

	public void setCurrentWitness(WitnessView currentWitness) {
		this.currentWitness = currentWitness;
	}


	public boolean isUpdateWitness() {
		return updateWitness;
	}

	public void setUpdateWitness(boolean updateWitness) {
		this.updateWitness = updateWitness;
	}

	public boolean isSelectAllWitness() {
		return selectAllWitness;
	}

	public void setSelectAllWitness(boolean selectAllWitness) {
		this.selectAllWitness = selectAllWitness;
	}
	
	 public boolean isDirectiveParamRequired() {
		return directiveParamRequired;
	}

	public void setDirectiveParamRequired(boolean directiveParamRequired) {
		this.directiveParamRequired = directiveParamRequired;
	}

	public boolean isInspectorParamRequired() {
		return inspectorParamRequired;
	}

	public void setInspectorParamRequired(boolean inspectorParamRequired) {
		this.inspectorParamRequired = inspectorParamRequired;
	}

	public String getDirectiveParam() {
		return directiveParam;
	}

	public void setDirectiveParam(String directiveParam) {
		if(directiveParam!=null){
			this.directiveParam = directiveParam.trim();
		}
	}

	

	public WitnessView getSelectedWitness() {
		return selectedWitness;
	}

	public void setSelectedWitness(WitnessView selectedWitness) {
		this.selectedWitness = selectedWitness;
	}

	public TAStaffBO getInspectorParam() {
		return inspectorParam;
	}

	public void setInspectorParam(TAStaffBO inspectorParam) {
		this.inspectorParam = inspectorParam;
	}

	public boolean isAllegationRequired() {
		return allegationRequired;
	}

	public void setAllegationRequired(boolean allegationRequired) {
		this.allegationRequired = allegationRequired;
	}

	public String getAllegation() {
		return allegation;
	}

	public void setAllegation(String allegation) {
		this.allegation = allegation;
	}

	public void onRowSelect(SelectEvent event) {  
		 WitnessView selectedWitness  = (WitnessView) event.getObject();  
		 System.err.println("selectedWitness: " + selectedWitness.getLastName());
	        //FacesContext.getCurrentInstance().addMessage(null, msg);  
	    }

	public boolean isNoOfPassengersRequired() {
		return noOfPassengersRequired;
	}

	public void setNoOfPassengersRequired(boolean noOfPassengersRequired) {
		this.noOfPassengersRequired = noOfPassengersRequired;
	}

	public Integer getNoOfPassengers() {
		return noOfPassengers;
	}

	public void setNoOfPassengers(Integer noOfPassengers) {
		this.noOfPassengers = noOfPassengers;
	}

	public HashMap<String, String> getOffenceParamMap() {
		return offenceParamMap;
	}

	public void setOffenceParamMap(HashMap<String, String> offenceParamMap) {
		this.offenceParamMap = offenceParamMap;
	}



	public String getVehicleMoverType()
	{
		return vehicleMoverType;
	}

	public void setVehicleMoverType(String vehicleMoverType)
	{
		this.vehicleMoverType = vehicleMoverType;
	}

	public PersonBO getVehicleMoverBO()
	{
		return vehicleMoverBO;
	}

	public void setVehicleMoverBO(PersonBO vehicleMoverBO)
	{
		this.vehicleMoverBO = vehicleMoverBO;
	}

	public VehicleBO getWreckerVehicleBO()
	{
		return wreckerVehicleBO;
	}

	public void setWreckerVehicleBO(VehicleBO wreckerVehicleBO)
	{
		this.wreckerVehicleBO = wreckerVehicleBO;
	}


	public String getWreckerVehRegNum()
	{
		return wreckerVehRegNum;
	}

	public void setWreckerVehRegNum(String wreckerVehRegNum)
	{
		this.wreckerVehRegNum = wreckerVehRegNum;
	}

	public String getVehicleMoverDLN()
	{
		return vehicleMoverDLN;
	}

	public void setVehicleMoverDLN(String vehicleMoverDLN)
	{
		this.vehicleMoverDLN = vehicleMoverDLN;
	}

	public PersonBO getVehicleMoverWreckerBO()
	{
		return vehicleMoverWreckerBO;
	}

	public PersonBOForRoadCompConverter getVehicleMoverTABO()
	{
		return vehicleMoverTABO;
	}

	public void setVehicleMoverTABO(PersonBOForRoadCompConverter vehicleMoverTABO)
	{
		this.vehicleMoverTABO = vehicleMoverTABO;
	}

	public PersonBOForRoadCompConverter getVehicleMoverITABO()
	{
		return vehicleMoverITABO;
	}

	public void setVehicleMoverITABO(PersonBOForRoadCompConverter vehicleMoverITABO)
	{
		this.vehicleMoverITABO = vehicleMoverITABO;
	}

	public PersonBOForRoadCompConverter getVehicleMoverPOBO()
	{
		return vehicleMoverPOBO;
	}

	public void setVehicleMoverPOBO(PersonBOForRoadCompConverter vehicleMoverPOBO)
	{
		this.vehicleMoverPOBO = vehicleMoverPOBO;
	}

	public PersonBOForRoadCompConverter getVehicleMoverOTBO()
	{
		return vehicleMoverOTBO;
	}

	public void setVehicleMoverOTBO(PersonBOForRoadCompConverter vehicleMoverOTBO)
	{
		this.vehicleMoverOTBO = vehicleMoverOTBO;
	}

	public void setVehicleMoverWreckerBO(
			PersonBOForRoadCompConverter vehicleMoverWreckerBO)
	{
		this.vehicleMoverWreckerBO = vehicleMoverWreckerBO;
	}

	public VehicleOwnerBO getOwner() {
		return owner;
	}

	public void setOwner(VehicleOwnerBO owner) {
		this.owner = owner;
	}

	/**
	 * @return the issueToOwner
	 */
	public boolean isIssueToOwner() {
		return issueToOwner;
	}

	/**
	 * @param issueToOwner the issueToOwner to set
	 */
	public void setIssueToOwner(boolean issueToOwner) {
		this.issueToOwner = issueToOwner;
	}

	public String getSelectedWreckerParishCode()
	{
		return selectedWreckerParishCode;
	}

	public void setSelectedWreckerParishCode(String selectedWreckerParishCode)
	{
		this.selectedWreckerParishCode = selectedWreckerParishCode;
	}

	public String getSelectedPoundParishCode()
	{
		return selectedPoundParishCode;
	}

	public void setSelectedPoundParishCode(String selectedPoundParishCode)
	{
		this.selectedPoundParishCode = selectedPoundParishCode;
	}

	/**
	 * @return the prevWreckerPlateRegNo
	 */
	public String getPrevWreckerPlateRegNo() {
		return prevWreckerPlateRegNo;
	}

	/**
	 * @param prevWreckerPlateRegNo the prevWreckerPlateRegNo to set
	 */
	public void setPrevWreckerPlateRegNo(String prevWreckerPlateRegNo) {
		if(prevWreckerPlateRegNo!=null){
			this.prevWreckerPlateRegNo = prevWreckerPlateRegNo.trim();
		}
	}

	
}
