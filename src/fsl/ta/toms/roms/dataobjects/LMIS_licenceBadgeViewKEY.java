
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;


public class LMIS_licenceBadgeViewKEY implements Serializable
{

	private static final long serialVersionUID = 1L;

	
	@Column(name="licence_no")
	Integer licenceNo;
	
	
	@Column(name="first_name")
	String firstName;
	
	
	@Column(name="last_name")
	String lastName;

	
	@Column(name="badge_number")
	String badgeNumber;
	
	
	@Column(name="badge_type")
	String badgeType;
	
	
	public Integer getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
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

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getBadgeType() {
		return badgeType;
	}

	public void setBadgeType(String badgeType) {
		this.badgeType = badgeType;
	}
	
	
}
