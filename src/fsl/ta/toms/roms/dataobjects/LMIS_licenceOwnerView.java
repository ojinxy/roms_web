/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 * @category This is a hibernae class used to represent roms_lmis_licence_owner_view
 */
@Entity()
@Table(name="roms_lmis_licence_owner_view")
@IdClass(LMIS_licenceOwnerViewKEY.class)
public class LMIS_licenceOwnerView implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LMIS_licenceOwnerView()
	{
		super();
	}
	
	@Id
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Id
	@Column(name="owner_type")
	String ownerType;
	
	@Id
	@Column(name="cust_id")
	String customerId;
	
	
	@Column(name="first_name")
	String firstName;
	
	
	@Column(name="last_name")
	String lastName;

	
	@Column(name="trn_nbr")
	String trnNbr;
	
	
	
	@Column(name="street_no")
	String streetNo;
	
	
	@Column(name="street_name")
	String streetName;
	
	
	@Column(name="mark")
	String mark;
	
	
	@Column(name="po_box")
	String poBox;
	
	
	
	@Column(name="post_office")
	String postOffice;
	
	
	@Column(name="parish_code")
	String parishCode;
	
	
	@Column(name="home_phone_no")
	String homePhoneNo;
	
	
	
	@Column(name="work_phone_no")
	String workPhoneNo;
	
	
	@Column(name="mobile_phone_no")
	String mobilePhoneNo;
	
	
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

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getPostOffice() {
		return postOffice;
	}

	public void setPostOffice(String postOffice) {
		this.postOffice = postOffice;
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
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

	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}

	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}
	
	
}
