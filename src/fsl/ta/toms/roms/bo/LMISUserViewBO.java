package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;

public class LMISUserViewBO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6413827918683175026L;
	private String username;
	private String passwordHash;
	private String firstName;
	private String lastName;
	private String status;
	private String locationCode;
	private String locationDescription;
	
	//add the permissions
	private List<String> permissions;
	
	//this stores the name of the road operation the user is currently on
	private Integer currentRoadOperationId;
	private String currentRoadOperationName;
	private String currentRoadOperationLocation;
	private Date currentRoadOperationStartDateNTime;
	private Date currentRoadOperationEndDateNTime;
	
	
	public LMISUserViewBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	public LMISUserViewBO(String username, String firstName, String lastName,
			String status, String locationCode) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
		this.locationCode = locationCode;
	}
	
	/**
	 * Constructed to create view from DO
	 * @param userDO
	 */
	public LMISUserViewBO(LMIS_UserViewDO userDO) {
		super();
		this.username = userDO.getUsername();
		this.firstName = userDO.getFirstName();
		this.lastName = userDO.getLastName();
		this.status = userDO.getStatus();
		this.passwordHash = userDO.getPasswordHash();
		this.locationCode = userDO.getLocationCode();
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}




	/**
	 * @return the locationDescription
	 */
	public String getLocationDescription() {
		return locationDescription;
	}




	/**
	 * @param locationDescription the locationDescription to set
	 */
	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}




	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		return permissions;
	}




	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}



	




	/**
	 * @return the currentRoadOperationName
	 */
	public String getCurrentRoadOperationName() {
		return currentRoadOperationName;
	}




	/**
	 * @param currentRoadOperationName the currentRoadOperationName to set
	 */
	public void setCurrentRoadOperationName(String currentRoadOperationName) {
		this.currentRoadOperationName = currentRoadOperationName;
	}


	/**
	 * @return the currentRoadOperationLocation
	 */
	public String getCurrentRoadOperationLocation() {
		return currentRoadOperationLocation;
	}




	/**
	 * @param currentRoadOperationLocation the currentRoadOperationLocation to set
	 */
	public void setCurrentRoadOperationLocation(String currentRoadOperationLocation) {
		this.currentRoadOperationLocation = currentRoadOperationLocation;
	}




	/**
	 * @return the currentRoadOperationId
	 */
	public Integer getCurrentRoadOperationId() {
		return currentRoadOperationId;
	}




	/**
	 * @param currentRoadOperationId the currentRoadOperationId to set
	 */
	public void setCurrentRoadOperationId(Integer currentRoadOperationId) {
		this.currentRoadOperationId = currentRoadOperationId;
	}




	/**
	 * @return the currentRoadOperationStartDateNTime
	 */
	public Date getCurrentRoadOperationStartDateNTime() {
		return currentRoadOperationStartDateNTime;
	}




	/**
	 * @param currentRoadOperationStartDateNTime the currentRoadOperationStartDateNTime to set
	 */
	public void setCurrentRoadOperationStartDateNTime(
			Date currentRoadOperationStartDateNTime) {
		this.currentRoadOperationStartDateNTime = currentRoadOperationStartDateNTime;
	}




	/**
	 * @return the currentRoadOperationEndDateNTime
	 */
	public Date getCurrentRoadOperationEndDateNTime() {
		return currentRoadOperationEndDateNTime;
	}




	/**
	 * @param currentRoadOperationEndDateNTime the currentRoadOperationEndDateNTime to set
	 */
	public void setCurrentRoadOperationEndDateNTime(
			Date currentRoadOperationEndDateNTime) {
		this.currentRoadOperationEndDateNTime = currentRoadOperationEndDateNTime;
	}




	
	
}
