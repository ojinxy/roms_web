package org.fsl.roms.security;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fsl.ta.toms.roms.bo.LMISUserViewBO;

/**
 * 
 * @author jreid
 *
 */
public class ROMSUserDetails implements UserDetails {

	private String username;
	private String password;//hash of password 
	
	private Collection<GrantedAuthority> authorities;
	
	private String locationCode;
	private String location;
	private String userFirstName;
	private String userLastName;
	
	
	/** CURRENT ROAD OPERATION DETAILS**/	
	private Integer currentRoadOperationId;
	private String currentRoadOperationName;
	private String currentRoadOperationLocation;
	private Date currentRoadOperationStartDateNTime;
	private Date currentRoadOperationEndDateNTime;

	

	public ROMSUserDetails() {
		super();
	
	}

	public ROMSUserDetails(LMISUserViewBO userView) {
		super();
		
		try {
			
			if(userView != null ){
				this.username = userView.getUsername();
				this.password = userView.getPasswordHash();				
				
				this.locationCode = userView.getLocationCode();
				this.location = userView.getLocationDescription();
				this.userFirstName = userView.getFirstName();
				this.userLastName = userView.getLastName();
				
				if(userView.getCurrentRoadOperationEndDateNTime() != null)
					this.currentRoadOperationEndDateNTime = userView.getCurrentRoadOperationEndDateNTime();
				this.currentRoadOperationId= userView.getCurrentRoadOperationId();
				this.currentRoadOperationLocation = userView.getCurrentRoadOperationLocation();
				this.currentRoadOperationName= userView.getCurrentRoadOperationName();
				
				if(userView.getCurrentRoadOperationStartDateNTime() != null)
					this.currentRoadOperationStartDateNTime = userView.getCurrentRoadOperationStartDateNTime();
				
				
				/*BaseServiceAction bsAction  = new BaseServiceAction();
				fsl.ta.toms.roms.webservices.roadoperation.RoadOperationBO  roadOp = bsAction.getRoadOperationPortProxy().findActiveOperationForUser(username);
			*/	
				/*if(roadOp != null){
					this.currentRoadOperation = roadOp.getOperationName();
					this.currentRoadOperationLocation = roadOp.getOfficeLocCode();
					
					if(roadOp.getScheduledStartDate() != null)
						this.currentRoadOperationStartTime = roadOp.getScheduledStartDate().toString();
				}
				*/
			}else{
				System.err.println("USER DOES NTO EXIST: " + username);
			}
		} 
		catch 
		(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return this.locationCode;
	}

	/**
	 * @param locationCode
	 *            the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName() {
		return this.userFirstName;
	}

	/**
	 * @param userFirstName
	 *            the userFirstName to set
	 */
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	/**
	 * @return the userLastName
	 */
	public String getUserLastName() {
		return this.userLastName;
	}

	/**
	 * @param userLastName
	 *            the userLastName to set
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -5104758892507086051L;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {

		return this.authorities;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	public boolean hasPermission(String role) {
//System.err.println(" role being tested in base " + role);
		boolean result = false;
		for (GrantedAuthority authority : this.authorities) {

			if (authority.getAuthority().equalsIgnoreCase(role)) {

				result = true;
				break;
			}
		}

		return result;

	}

	// NEED these to use concurrency
	@Override
	public boolean equals(Object user) {

		if (user != null) {
			ROMSUserDetails details = (ROMSUserDetails) user;
			if (StringUtils.isNotBlank(details.getUsername())) {

				return this.username.equalsIgnoreCase(details.getUsername());
			}
		}
		return false;
	}

	@Override
	public int hashCode() {

		int hash = 0;

		hash += (username != null ? username.hashCode() : 0);

		return hash;

	}

	@Override
	public String toString() {

		return "User ID[id=" + username + "]";

	}
	

}
