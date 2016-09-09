/**
 * Created By: oanguin
 * Date: May 6, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * This class contains all the information from the BIMS Badge View
 * @author oanguin
 * Created Date: May 6, 2013
 */
public class BadgeBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String trn,
				   badgeDesc,
				   firstName,
				   midName,
				   lastName,
				   statusDesc;
	
	private Date issueDate,
				 expiryDate;
	
	private Integer badgeNo;
	private Integer biometricsID;
	
	private byte[] photoImg;
	
	
	
	public BadgeBO(String trn, String badgeDesc, String firstName,
			String midName, String lastName, String statusDesc, Date issueDate,
			Date expiryDate, Integer badgeNo, Integer biometricsID,
			byte[] photoImg) {
		super();
		this.trn = trn;
		this.badgeDesc = badgeDesc;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.statusDesc = statusDesc;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.badgeNo = badgeNo;
		this.biometricsID = biometricsID;
		this.photoImg = photoImg;
	}




	public BadgeBO() {
		super();
		// TODO Auto-generated constructor stub2
	}

	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getBadgeDesc() {
		return badgeDesc;
	}

	public void setBadgeDesc(String badgeDesc) {
		this.badgeDesc = badgeDesc;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getBadgeNo() {
		return badgeNo;
	}

	public void setBadgeNo(Integer badgeNo) {
		this.badgeNo = badgeNo;
	}

	public byte[] getPhotoImg() {
		return photoImg;
	}

	public void setPhotoImg(byte[] photoImg) {
		this.photoImg = photoImg;
	}

	public Integer getBiometricsID() {
		return biometricsID;
	}

	public void setBiometricsID(Integer biometricsID) {
		this.biometricsID = biometricsID;
	}

}
