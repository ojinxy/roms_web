package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_bims_badge_view")
public class BIMS_BadgeView implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4214804824278221242L;

	public BIMS_BadgeView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="badge_no")
	Integer badgeNo;
	
	@Column(name="trn")
	String trn;
	
	@Column(name="badge_type_code")
	 String badgeTypeCode;
	
	@Column(name="badge_desc")
	 String badgeDesc;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String midName;
	
	@Column(name="last_name")
	String lastName;
	
	@Column(name="biometrics_sno")
	Integer biometricsSno;
	
	@Column(name="photo_img")
	Blob photoImg;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Column(name="status_desc")
	String statusDesc;




	public Integer getBadgeNo() {
		return badgeNo;
	}

	public void setBadgeNo(Integer badgeNo) {
		this.badgeNo = badgeNo;
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

	public Blob getPhotoImg() {
		return photoImg;
	}

	public void setPhotoImg(Blob photoImg) {
		this.photoImg = photoImg;
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

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
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

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getBadgeTypeCode() {
		return badgeTypeCode;
	}

	public void setBadgeTypeCode(String badgeTypeCode) {
		this.badgeTypeCode = badgeTypeCode;
	}

	public Integer getBiometricsSno() {
		return biometricsSno;
	}

	public void setBiometricsSno(Integer biometricsSno) {
		this.biometricsSno = biometricsSno;
	}			 
	
	


}
