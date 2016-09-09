
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 22, 2014
 */
@Entity
@Table(name="roms_lmis_licence_badge_view")
@IdClass(LMIS_licenceBadgeViewKEY.class)
public class LMIS_licenceBadgeView implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LMIS_licenceBadgeView()
	{
		super();
	}
	
	@Id
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Id
	@Column(name="first_name")
	String firstName;
	
	@Id
	@Column(name="last_name")
	String lastName;

	@Id
	@Column(name="badge_number")
	String badgeNumber;
	
	@Id
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
