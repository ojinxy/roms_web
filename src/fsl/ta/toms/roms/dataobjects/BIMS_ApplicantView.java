package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_bims_applicant_view")
public class BIMS_ApplicantView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3015290033693219057L;

	public BIMS_ApplicantView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="trn_nbr") 
	String trnNbr;
	
	@Column(name="first_name")
	String firstName;
	
	@Column(name="mid_name")
	String midName;
	
	@Column(name="last_name")
	 String lastName;
	
	@Column(name="birth_date")
	 Date birthDate;	   
	 
	@Column(name="gender_code")
	Character genderCode;
	
	@Column(name="hm_mark_text")
	String homeMarkText;
	
	@Column(name="hm_street_no")
	String homeStreetNo;
	
	@Column(name="hm_street_name")
	 String homeStreetName;
	
	@Column(name="hm_po_loc_name")
	String homePOLocationName;
	
	@Column(name="hm_po_box_no")
	 String homePOBoxNo;
	
	@Column(name="hm_parish_code")
	 String homeParishCode;
	
	@Column(name="tel_home")
	String telephoneHome;			   
	
	@Column(name="tel_cell")
	String telephoneCell;
	
	@Column(name="tel_work")
	 String telephoneWork;			   
	
	@Column(name="badge_desc")
	String badgeDescription;

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Character getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(Character genderCode) {
		this.genderCode = genderCode;
	}

	public String getHomeMarkText() {
		return homeMarkText;
	}

	public void setHomeMarkText(String homeMarkText) {
		this.homeMarkText = homeMarkText;
	}

	public String getHomeStreetNo() {
		return homeStreetNo;
	}

	public void setHomeStreetNo(String homeStreetNo) {
		this.homeStreetNo = homeStreetNo;
	}

	public String getHomeStreetName() {
		return homeStreetName;
	}

	public void setHomeStreetName(String homeStreetName) {
		this.homeStreetName = homeStreetName;
	}

	public String getHomePOLocationName() {
		return homePOLocationName;
	}

	public void setHomePOLocationName(String homePOLocationName) {
		this.homePOLocationName = homePOLocationName;
	}

	public String getHomePOBoxNo() {
		return homePOBoxNo;
	}

	public void setHomePOBoxNo(String homePOBoxNo) {
		this.homePOBoxNo = homePOBoxNo;
	}

	public String getHomeParishCode() {
		return homeParishCode;
	}

	public void setHomeParishCode(String homeParishCode) {
		this.homeParishCode = homeParishCode;
	}

	public String getTelephoneHome() {
		return telephoneHome;
	}

	public void setTelephoneHome(String telephoneHome) {
		this.telephoneHome = telephoneHome;
	}

	public String getTelephoneCell() {
		return telephoneCell;
	}

	public void setTelephoneCell(String telephoneCell) {
		this.telephoneCell = telephoneCell;
	}

	public String getTelephoneWork() {
		return telephoneWork;
	}

	public void setTelephoneWork(String telephoneWork) {
		this.telephoneWork = telephoneWork;
	}

	public String getBadgeDescription() {
		return badgeDescription;
	}

	public void setBadgeDescription(String badgeDescription) {
		this.badgeDescription = badgeDescription;
	}
	
	


}
