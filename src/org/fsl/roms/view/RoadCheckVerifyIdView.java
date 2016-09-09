package org.fsl.roms.view;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.webflow.execution.RequestContext;

public class RoadCheckVerifyIdView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8415383275440926180L;
	
	private String trn;
	private String lastName;
	private String firstName;
	private String middleName;
	private boolean disableTrnField;
	private boolean disableNameField; 
	private boolean disableInitiateBtn;
	private String searchType;
	private boolean showDialog;
	private String nameSearchTitle;
	
	private String prevLastName;
	private String prevFirstName;
	private String prevMiddleName;
	
	//this stores the name of the road operation the user is currently on
	private Integer currentRoadOperationId;
	private String currentRoadOperationName;
	private String currentRoadOperationLocation;
	private Date currentRoadOperationStartDateNTime;
	private Date currentRoadOperationEndDateNTime;
		
	public RoadCheckVerifyIdView() {
		super();
		this.trn = "";
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.disableTrnField = false;
		this.disableNameField = true;
		this.disableInitiateBtn = true;
		this.searchType="T";
		this.showDialog=false;
		this.nameSearchTitle="";
		this.prevFirstName="";
		this.prevLastName="";
		this.prevMiddleName="";
	}
	


	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		if(trn!=null){
			this.trn = trn.trim();
		}
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		if(lastName!=null){
			this.lastName = lastName.trim();
		}
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		if(firstName!=null){
			this.firstName = firstName.trim();
		}
	}

	
	

	public String getMiddleName() {
		return middleName;
	}



	public void setMiddleName(String middleName) {
		if(middleName!=null){
			this.middleName = middleName.trim();
		}
	}



	public boolean isDisableTrnField() {
		return disableTrnField;
	}



	public void setDisableTrnField(boolean disableTrnField) {
		this.disableTrnField = disableTrnField;
	}



	public boolean isDisableNameField() {
		return disableNameField;
	}



	public void setDisableNameField(boolean disableNameField) {
		this.disableNameField = disableNameField;
	}



	public boolean isDisableInitiateBtn() {
		return disableInitiateBtn;
	}



	public void setDisableInitiateBtn(boolean disableInitiateBtn) {
		this.disableInitiateBtn = disableInitiateBtn;
	}



	public String getSearchType() {
		return searchType;
	}



	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}



	public boolean isShowDialog() {
		return showDialog;
	}



	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}



	public String getNameSearchTitle() {
		return nameSearchTitle;
	}



	public void setNameSearchTitle(String nameSearchTitle) {
		this.nameSearchTitle = nameSearchTitle;
	}



	public String getPrevLastName() {
		return prevLastName;
	}



	public void setPrevLastName(String prevLastName) {
		this.prevLastName = prevLastName;
	}



	public String getPrevFirstName() {
		return prevFirstName;
	}



	public void setPrevFirstName(String prevFirstName) {
		this.prevFirstName = prevFirstName;
	}



	public String getPrevMiddleName() {
		return prevMiddleName;
	}



	public void setPrevMiddleName(String prevMiddleName) {
		this.prevMiddleName = prevMiddleName;
	}



	public void clear() {
		this.trn = ""; 
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.disableTrnField = false;
		this.disableNameField = true;
		this.disableInitiateBtn = true;
		this.searchType="T"; 
		this.showDialog=false;
		this.nameSearchTitle="";
	}
	
	
	public void changeName(){
		this.disableInitiateBtn=true;
		this.showDialog=false;
	}
	
	public void changeLastName(){
		if(!lastName.equalsIgnoreCase(prevLastName)){
			this.disableInitiateBtn=true;
		}
		setPrevLastName(lastName);
		this.showDialog=false;
	}
	
	public void changeFirstName(){
		if(!firstName.equalsIgnoreCase(prevFirstName)){
			this.disableInitiateBtn=true;
		}
		setPrevFirstName(firstName);
		this.showDialog=false;
	}
	
	public void changeMiddleName(){
		if(!middleName.equalsIgnoreCase(prevMiddleName)){
			this.disableInitiateBtn=true;
		}
		setPrevMiddleName(middleName);
		this.showDialog=false;
		
	}
	

	public void changeDisableFields(){
		this.trn = ""; 
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.showDialog=false;
		this.nameSearchTitle="";
		this.disableInitiateBtn = true;
		
		if(this.disableTrnField==true){
			this.disableTrnField=false;
		}
		else{
			this.disableTrnField=true;
		}
		
		if(this.disableNameField==true){
			this.disableNameField=false;
			FacesMessage msg = new FacesMessage("Names will be retrieved from BIMS and may not be consistent with the names from the TRN service.");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		else{
			this.disableNameField=true;
		}
		
		this.prevFirstName="";
		this.prevLastName="";
		this.prevMiddleName="";
	}
	

	public Integer getCurrentRoadOperationId() {
		return currentRoadOperationId;
	}



	public void setCurrentRoadOperationId(Integer currentRoadOperationId) {
		this.currentRoadOperationId = currentRoadOperationId;
	}



	public String getCurrentRoadOperationName() {
		return currentRoadOperationName;
	}



	public void setCurrentRoadOperationName(String currentRoadOperationName) {
		this.currentRoadOperationName = currentRoadOperationName;
	}



	public String getCurrentRoadOperationLocation() {
		return currentRoadOperationLocation;
	}



	public void setCurrentRoadOperationLocation(String currentRoadOperationLocation) {
		this.currentRoadOperationLocation = currentRoadOperationLocation;
	}



	public Date getCurrentRoadOperationStartDateNTime() {
		return currentRoadOperationStartDateNTime;
	}



	public void setCurrentRoadOperationStartDateNTime(
			Date currentRoadOperationStartDateNTime) {
		this.currentRoadOperationStartDateNTime = currentRoadOperationStartDateNTime;
	}



	public Date getCurrentRoadOperationEndDateNTime() {
		return currentRoadOperationEndDateNTime;
	}



	public void setCurrentRoadOperationEndDateNTime(
			Date currentRoadOperationEndDateNTime) {
		this.currentRoadOperationEndDateNTime = currentRoadOperationEndDateNTime;
	}
}
