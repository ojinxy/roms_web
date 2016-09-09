
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 22, 2014
 */
public class RoadLicenceDriverConductorBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	String badgeNumber, firstName, lastName, badgeType;


	public String getBadgeNumber() {
		return badgeNumber;
	}


	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
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


	public String getBadgeType() {
		return badgeType;
	}


	public void setBadgeType(String badgeType) {
		this.badgeType = badgeType;
	}
	
	
}