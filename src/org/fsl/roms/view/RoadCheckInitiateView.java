package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.fsl.roms.util.DateUtils;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.RoadLicenceOwnerBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.TAStaffBO;


public class RoadCheckInitiateView  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8415383275440926180L;
	
	private boolean mobileDevice;
	private String activityType;
	private String operationName;
	//private Integer operationId;
	//private String offencePlace;
	//private Integer offencePlaceId;
	private String offenceParish;
	private Date offenceDate;

	//Person Details
	private String photoURL;
	private boolean showPhoto;
	private boolean vehicleInfoDifferent;
	private String trn;
	private String roleObserved;
	private String lastName;
	private String firstName;
	private String middleName;
	
	private AddressView addressView;
	private String mobilePhoneNo;
	private String homePhoneNo;
	private String workPhoneNo;
	
	
	//Motor Vehicle

	private String chassisNo;
	private String colour;
	private String engineNo;
	private String makeDescription;
	private String model;
	
	private String plateRegNo;
	private Integer year; 
	private String prevPlateRegNo;
	private String type;
	private String ownerName;
	
	//Checks
	private boolean roadLicQuery;
	private boolean dlQuery;
	private boolean badgeQuery;
	private boolean citationHistQuery;
	private boolean showCitationHistQuery;
	

	
	//Required Info
	private String dlNo;
	private String badgeNo;
	private String roadLicNo;
	
	private String prevPlateRegNoCH;
	private String prevDlNoCH;
	private String prevDlNo;
	private String prevBadgeNo;
	private String prevRoadLicNo;
	
	
	private boolean disableMotorVehicleFields;

	private Integer complianceId;

	

	private RoadOperationBO roadOperationBO;
	
	private boolean performQuery;
	
	private boolean citationHistQueryDone;
	private boolean badgeQueryDone;
	private boolean dlQueryDone;
	private boolean rlQueryDone;
	
	
	private boolean noBadgeFound;
	private boolean noRlFound;
	private boolean noDLFound;
	private boolean noCitationFound;
	private boolean noVehicleFound;

	
	private String citationPersonLabel;
	private String citationVehicleLabel;
	
	private boolean roadCheckProceeded;

	private TAStaffBO taStaffBO;
	private ArteryBO offencePlace;
	
	private boolean driverBadge;
	private boolean conductionBadge;
	
	private String bimsBadgeType;
	
	private boolean completeInitiate;
	private boolean completeRecordOffOutcome;
	private boolean completeSupportingDetails;
	private boolean completeReviewSummary;
	
	private boolean fromSearch;
	
	//additional info for ROad Licence
	private List<RoadLicenceOwnerBO> roadLicenceOwners;
	private String routeEnd;
	private String routeStart;
	private Integer seatCapacity;
	private String status;
	
	private boolean rlCkFl = false;
	private boolean dlCkFl = false;
	private boolean mVCkFl = false;
	private boolean bGCkFl = false;
	private boolean inCkFl = false;
	private boolean fTCkFl = false;
	private boolean rlInCkFl= false;
	private boolean rlFtCkFl= false;
	private boolean rlExpire = false;
	private String otherRoleId;
	
	private boolean byPassValidation = false;
	
	private String licenceType;
	private Date roadLicExpiryDate;
	
	private Boolean includeTTMSResults;
	
	
	public RoadCheckInitiateView() {
		super();
		this.activityType = "";
		this.operationName = "";
		this.photoURL = "";
		this.trn = "";
		this.roleObserved = "";
		this.otherRoleId = "";		
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.addressView = new AddressView();
		this.mobilePhoneNo = "";
		this.homePhoneNo = "";
		this.workPhoneNo = "";
		this.chassisNo = "";
		this.colour = "";
		this.engineNo = "";
		this.makeDescription = "";
		this.model = "";
		this.plateRegNo = "";
		this.year = null;
		this.ownerName = "";
		this.roadLicQuery = true;
		this.dlQuery = false;
		this.badgeQuery = false;
		this.citationHistQuery = false;
		this.dlNo = "";
		this.badgeNo = "";
		this.roadLicNo = "";
		this.disableMotorVehicleFields=true;
		this.showPhoto = false;
		this.prevPlateRegNo = "";
		this.mobileDevice = false;
		this.complianceId=null;
		this.showCitationHistQuery = true;
		this.performQuery=false;
		
		this.prevPlateRegNoCH="";
		this.prevDlNoCH="";
		this.prevDlNo = "";
		this.prevBadgeNo = "";
		this.prevRoadLicNo = "";
		this.citationHistQueryDone=false;
		this.badgeQueryDone=false;
		this.dlQueryDone=false;
		this.rlQueryDone=false;
		
		
		this.noBadgeFound=false;
		this.noRlFound=false;
		this.noDLFound=false;
		this.noCitationFound=false;
		this.roadOperationBO = new RoadOperationBO();
		this.roadCheckProceeded=false;
		this.taStaffBO = new TAStaffBO();
		this.offencePlace = new ArteryBO();
		this.driverBadge=false;
		this.conductionBadge=false;
		this.bimsBadgeType="";
		this.completeInitiate = false;
		this.completeRecordOffOutcome = false;
		this.completeSupportingDetails = false;
		this.completeReviewSummary = false;
		initializeOffenceDate();
		this.type="";
		
		//additional info for ROad Licence
		roadLicenceOwners = new ArrayList<RoadLicenceOwnerBO>();;
		routeEnd ="";
		routeStart = "";
		seatCapacity = null;
		status ="";
	}


	private void initializeOffenceDate(){
		Date offDate = null;
		//Date convertedDate = null;
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		 /*try {
    	convertedDate = dateFormat.parse(StringUtil.getCurrentDateString());
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} */
		
	     
	    Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		
	   try {
	    	offDate = currentDate;
	    	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	   
		this.offenceDate=offDate;  
		
	}

	public String getActivityType() {
		return activityType;
	}


	public void setActivityType(String activityType) {
		if(activityType!=null){
			this.activityType = activityType.trim();
		}
	}


	public String getOperationName() {
		return operationName;
	}


	public void setOperationName(String operationName) {
			this.operationName = operationName;
	}


	



	public String getPhotoURL() {
		return photoURL;
	}


	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}


	public String getTrn() {
		return trn;
	}


	public void setTrn(String trn) {
			this.trn = trn;
	}




	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public AddressView getAddressView() {
		return addressView;
	}


	public void setAddressView(AddressView addressView) {
		this.addressView = addressView;
	}


	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}


	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}


	public String getHomePhoneNo() {
		return homePhoneNo;
	}


	public void setHomePhoneNo(String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}


	public String getWorkPhoneNo() {
		return workPhoneNo;
	}


	public void setWorkPhoneNo(String workPhoneNo) {
		this.workPhoneNo = workPhoneNo;
	}


	public String getChassisNo() {
		return chassisNo;
	}


	public void setChassisNo(String chassisNo) {
		if(chassisNo!=null){
			this.chassisNo = chassisNo.trim();
		}
	}


	public String getColour() {
		return colour;
	}


	public void setColour(String colour) {
		if(colour!=null){
			this.colour = colour.trim();
		}
	}


	public String getEngineNo() {
		return engineNo;
	}


	public void setEngineNo(String engineNo) {
		if(engineNo!=null){
			this.engineNo = engineNo.trim();
		}
	}


	public String getMakeDescription() {
		return makeDescription;
	}


	public void setMakeDescription(String makeDescription) {
		if(makeDescription!=null){
			this.makeDescription = makeDescription.trim();
		}
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		if(model!=null){
			this.model = model.trim();
		}
	}


	public String getPlateRegNo() {
		return plateRegNo.toUpperCase();
	}


	public void setPlateRegNo(String plateRegNo) {
		if(plateRegNo!=null){
			this.plateRegNo = plateRegNo.trim().toUpperCase();
		}
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public boolean isRoadLicQuery() {
		return roadLicQuery;
	}


	public void setRoadLicQuery(boolean roadLicQuery) {
		this.roadLicQuery = roadLicQuery;
	}


	public boolean isDlQuery() {
		return dlQuery;
	}


	public void setDlQuery(boolean dlQuery) {
		this.dlQuery = dlQuery;
	}


	public boolean isBadgeQuery() {
		return badgeQuery;
	}


	public void setBadgeQuery(boolean badgeQuery) {
		this.badgeQuery = badgeQuery;
	}


	public boolean isCitationHistQuery() {
		return citationHistQuery;
	}


	public void setCitationHistQuery(boolean citationHistQuery) {
		this.citationHistQuery = citationHistQuery;
	}


	public String getDlNo() {
		return dlNo;
	}


	public void setDlNo(String dlNo) {
		if(dlNo!=null){
			this.dlNo = dlNo.trim();
		}
	}


	public String getBadgeNo() {
		return badgeNo;
	}


	public void setBadgeNo(String badgeNo) {
		if(badgeNo!=null){
			this.badgeNo = badgeNo.trim();
		}
	}


	public String getRoadLicNo() {
		return roadLicNo;
	}


	public void setRoadLicNo(String roadLicNo) {
		if(roadLicNo!=null){
			this.roadLicNo = roadLicNo.trim();
		}
	}



	public boolean isDisableMotorVehicleFields() {
		return disableMotorVehicleFields;
	}



	public void setDisableMotorVehicleFields(boolean disableMotorVehicleFields) {
		this.disableMotorVehicleFields = disableMotorVehicleFields;
	}



	public boolean isShowPhoto() {
		return showPhoto;
	}



	public void setShowPhoto(boolean showPhoto) {
		this.showPhoto = showPhoto;
	}



	public String getPrevPlateRegNo() {
		return prevPlateRegNo;
	}



	public void setPrevPlateRegNo(String prevPlateRegNo) {
		if(prevPlateRegNo!=null){
			this.prevPlateRegNo = prevPlateRegNo.trim();
		}
	}



	

	public boolean isMobileDevice() {
		return mobileDevice;
	}



	public void setMobileDevice(boolean mobileDevice) {
		this.mobileDevice = mobileDevice;
	}



	public Integer getComplianceId() {
		return complianceId;
	}



	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}


	public Date getOffenceDate() {
		return offenceDate;
	}



	public void setOffenceDate(Date offenceDate) {
		this.offenceDate = offenceDate;
	}



	public boolean isShowCitationHistQuery() {
		return showCitationHistQuery;
	}



	public void setShowCitationHistQuery(boolean showCitationHistQuery) {
		this.showCitationHistQuery = showCitationHistQuery;
	}



	public void clear() {
		this.activityType = "";
		this.operationName = "";
		this.photoURL = "";
		this.trn = "";
		this.roleObserved = "";
		this.lastName = "";
		this.firstName = "";
		this.middleName = "";
		this.addressView = new AddressView();
		this.mobilePhoneNo = "";
		this.homePhoneNo = "";
		this.workPhoneNo = "";
		this.chassisNo = "";
		this.colour = "";
		this.engineNo = "";
		this.makeDescription = "";
		this.model = "";
		this.plateRegNo = "";
		this.year = null;
		this.roadLicQuery = true;
		this.dlQuery = false;
		this.badgeQuery = false;
		this.citationHistQuery = false;
		this.dlNo = "";
		this.badgeNo = "";
		this.roadLicNo = "";
		this.disableMotorVehicleFields=true;
		this.showPhoto = false;
		this.prevPlateRegNo = "";
		this.mobileDevice = false;
		this.complianceId=null;
		this.showCitationHistQuery = true;
		this.performQuery=false;
		
		this.prevDlNo = "";
		this.prevBadgeNo = "";
		this.prevRoadLicNo = "";
		
		this.citationHistQueryDone=false;
		this.badgeQueryDone=false;
		this.dlQueryDone=false;
		this.rlQueryDone=false;
		
		this.noBadgeFound=false;
		this.noRlFound=false;
		this.noDLFound=false;
		this.noCitationFound=false;
		this.roadCheckProceeded=false;

		this.roadOperationBO = new RoadOperationBO();
		this.roadCheckProceeded=false;
		this.taStaffBO = new TAStaffBO();
		this.offencePlace = new ArteryBO();
		this.driverBadge=false;
		this.conductionBadge=false;
		this.bimsBadgeType="";
		this.completeInitiate = false;
		this.completeRecordOffOutcome = false;
		this.completeSupportingDetails = false;
		initializeOffenceDate();
		this.type="";
	}


	/*public Integer getOperationId() {
		return operationId;
	}


	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}*/


	





	public RoadOperationBO getRoadOperationBO() {
		return roadOperationBO;
	}


	public void setRoadOperationBO(RoadOperationBO roadOperationBO) {
		this.roadOperationBO = roadOperationBO;
	}


	public String getOffenceParish() {
		return offenceParish;
	}


	public void setOffenceParish(String offenceParish) {
		this.offenceParish = offenceParish;
	}


	public boolean isPerformQuery() {
		return performQuery;
	}


	public void setPerformQuery(boolean performQuery) {
		this.performQuery = performQuery;
	}


	public String getPrevDlNo() {
		return prevDlNo;
	}


	public void setPrevDlNo(String prevDlNo) {
		if(prevDlNo!=null){
			this.prevDlNo = prevDlNo.trim();
		}
	}


	public String getPrevBadgeNo() {
		return prevBadgeNo;
	}


	public void setPrevBadgeNo(String prevBadgeNo) {
		if(prevBadgeNo!=null){
			this.prevBadgeNo = prevBadgeNo.trim();
		}
	}


	public String getPrevRoadLicNo() {
		return prevRoadLicNo;
	}


	public void setPrevRoadLicNo(String prevRoadLicNo) {
		if(prevRoadLicNo!=null){
			this.prevRoadLicNo = prevRoadLicNo.trim();
		}
	}


	public boolean isCitationHistQueryDone() {
		return citationHistQueryDone;
	}


	public void setCitationHistQueryDone(boolean citationHistQueryDone) {
		this.citationHistQueryDone = citationHistQueryDone;
	}


	public boolean isBadgeQueryDone() {
		return badgeQueryDone;
	}


	public void setBadgeQueryDone(boolean badgeQueryDone) {
		this.badgeQueryDone = badgeQueryDone;
	}


	public boolean isDlQueryDone() {
		return dlQueryDone;
	}


	public void setDlQueryDone(boolean dlQueryDone) {
		this.dlQueryDone = dlQueryDone;
	}


	public boolean isRlQueryDone() {
		return rlQueryDone;
	}


	public void setRlQueryDone(boolean rlQueryDone) {
		this.rlQueryDone = rlQueryDone;
	}


	public boolean isNoBadgeFound() {
		return noBadgeFound;
	}


	public void setNoBadgeFound(boolean noBadgeFound) {
		this.noBadgeFound = noBadgeFound;
	}


	public boolean isNoRlFound() {
		return noRlFound;
	}


	public void setNoRlFound(boolean noRlFound) {
		this.noRlFound = noRlFound;
	}


	public boolean isNoDLFound() {
		return noDLFound;
	}


	public void setNoDLFound(boolean noDLFound) {
		this.noDLFound = noDLFound;
	}


	public boolean isNoCitationFound() {
		return noCitationFound;
	}


	public void setNoCitationFound(boolean noCitationFound) {
		this.noCitationFound = noCitationFound;
	}


	public String getPrevDlNoCH() {
		return prevDlNoCH;
	}


	public void setPrevDlNoCH(String prevDlNoCH) {
		if(prevDlNoCH!=null){
			this.prevDlNoCH = prevDlNoCH.trim();
		}
	}


	public String getPrevPlateRegNoCH() {
		return prevPlateRegNoCH;
	}


	public void setPrevPlateRegNoCH(String prevPlateRegNoCH) {
		if(prevPlateRegNoCH!=null){
			this.prevPlateRegNoCH = prevPlateRegNoCH.trim();
		}
	}


	public String getCitationPersonLabel() {
		return citationPersonLabel;
	}


	public void setCitationPersonLabel(String citationPersonLabel) {
		if(citationPersonLabel!=null){
			this.citationPersonLabel = citationPersonLabel.trim();
		}
	}


	public String getCitationVehicleLabel() {
		return citationVehicleLabel;
	}


	public void setCitationVehicleLabel(String citationVehicleLabel) {
		this.citationVehicleLabel = citationVehicleLabel;
	}


	public boolean isRoadCheckProceeded() {
		return roadCheckProceeded;
	}


	public void setRoadCheckProceeded(boolean roadCheckProceeded) {
		this.roadCheckProceeded = roadCheckProceeded;
	}


	public TAStaffBO getTaStaffBO() {
		return taStaffBO;
	}


	public void setTaStaffBO(TAStaffBO taStaffBO) {
		this.taStaffBO = taStaffBO;
	}


	public ArteryBO getOffencePlace() {
		return offencePlace;
	}


	public void setOffencePlace(ArteryBO offencePlace) {
		this.offencePlace = offencePlace;
	}


	public String getRoleObserved() {
		return roleObserved;
	}


	public void setRoleObserved(String roleObserved) {
		this.roleObserved = roleObserved;
	}


	public boolean isDriverBadge() {
		return driverBadge;
	}


	public void setDriverBadge(boolean driverBadge) {
		this.driverBadge = driverBadge;
	}


	public boolean isConductionBadge() {
		return conductionBadge;
	}


	public void setConductionBadge(boolean conductionBadge) {
		this.conductionBadge = conductionBadge;
	}


	public String getBimsBadgeType() {
		return bimsBadgeType;
	}


	public void setBimsBadgeType(String bimsBadgeType) {
		this.bimsBadgeType = bimsBadgeType;
	}


	public boolean isCompleteInitiate() {
		return completeInitiate;
	}


	public void setCompleteInitiate(boolean completeInitiate) {
		this.completeInitiate = completeInitiate;
	}


	public boolean isCompleteRecordOffOutcome() {
		return completeRecordOffOutcome;
	}


	public void setCompleteRecordOffOutcome(boolean completeRecordOffOutcome) {
		this.completeRecordOffOutcome = completeRecordOffOutcome;
	}





	public boolean isCompleteSupportingDetails() {
		return completeSupportingDetails;
	}


	public void setCompleteSupportingDetails(boolean completeSupportingDetails) {
		this.completeSupportingDetails = completeSupportingDetails;
	}


	public boolean isFromSearch() {
		return fromSearch;
	}


	public void setFromSearch(boolean fromSearch) {
		this.fromSearch = fromSearch;
	}


	public boolean isCompleteReviewSummary() {
		return completeReviewSummary;
	}


	public void setCompleteReviewSummary(boolean completeReviewSummary) {
		this.completeReviewSummary = completeReviewSummary;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public List<RoadLicenceOwnerBO> getRoadLicenceOwners() {
		return roadLicenceOwners;
	}


	public void setRoadLicenceOwners(List<RoadLicenceOwnerBO> roadLicenceOwners) {
		this.roadLicenceOwners = roadLicenceOwners;
	}


	public String getRouteEnd() {
		return routeEnd;
	}


	public void setRouteEnd(String routeEnd) {
		this.routeEnd = routeEnd;
	}


	public String getRouteStart() {
		return routeStart;
	}


	public void setRouteStart(String routeStart) {
		this.routeStart = routeStart;
	}


	public Integer getSeatCapacity() {
		return seatCapacity;
	}


	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public boolean isRlCkFl() {
		return rlCkFl;
	}


	public void setRlCkFl(boolean rlCkFl) {
		this.rlCkFl = rlCkFl;
	}


	public boolean isDlCkFl() {
		return dlCkFl;
	}


	public void setDlCkFl(boolean dlCkFl) {
		this.dlCkFl = dlCkFl;
	}


	public boolean ismVCkFl() {
		return mVCkFl;
	}


	public void setmVCkFl(boolean mVCkFl) {
		this.mVCkFl = mVCkFl;
	}


	public boolean isbGCkFl() {
		return bGCkFl;
	}


	public void setbGCkFl(boolean bGCkFl) {
		this.bGCkFl = bGCkFl;
	}


	public boolean isInCkFl() {
		return inCkFl;
	}


	public void setInCkFl(boolean inCkFl) {
		this.inCkFl = inCkFl;
	}


	public boolean isfTCkFl() {
		return fTCkFl;
	}


	public void setfTCkFl(boolean fTCkFl) {
		this.fTCkFl = fTCkFl;
	}


	public String getLicenceType()
	{
		return licenceType;
	}


	public void setLicenceType(String licenceType)
	{
		this.licenceType = licenceType;
	}


	public Date getRoadLicExpiryDate()
	{
		return roadLicExpiryDate;
	}


	public void setRoadLicExpiryDate(Date roadLicExpiryDate)
	{
		this.roadLicExpiryDate = roadLicExpiryDate;
	}


	public boolean isVehicleInfoDifferent()
	{
		return vehicleInfoDifferent;
	}


	public void setVehicleInfoDifferent(boolean vehicleInfoDifferent)
	{
		this.vehicleInfoDifferent = vehicleInfoDifferent;
	}


	public boolean isRlInCkFl() {
		return rlInCkFl;
	}


	public void setRlInCkFl(boolean rlInCkFl) {
		this.rlInCkFl = rlInCkFl;
	}


	public boolean isRlFtCkFl() {
		return rlFtCkFl;
	}


	public void setRlFtCkFl(boolean rlFtCkFl) {
		this.rlFtCkFl = rlFtCkFl;
	}


	public boolean isNoVehicleFound()
	{
		return noVehicleFound;
	}


	public void setNoVehicleFound(boolean noVehicleFound)
	{
		this.noVehicleFound = noVehicleFound;
	}


	public Boolean getIncludeTTMSResults()
	{
		return includeTTMSResults;
	}


	public void setIncludeTTMSResults(Boolean includeTTMSResults)
	{
		this.includeTTMSResults = includeTTMSResults;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public boolean isByPassValidation()
	{
		return byPassValidation;
	}


	public void setByPassValidation(boolean byPassValidation)
	{
		this.byPassValidation = byPassValidation;
	}


	public boolean isRlExpire() {
		return rlExpire;
	}


	public void setRlExpire(boolean rlExpire) {
		this.rlExpire = rlExpire;
	}


	public String getOtherRoleId() {
		return otherRoleId;
	}


	public void setOtherRoleId(String otherRoleId) {
		this.otherRoleId = otherRoleId;
	}


	



}
