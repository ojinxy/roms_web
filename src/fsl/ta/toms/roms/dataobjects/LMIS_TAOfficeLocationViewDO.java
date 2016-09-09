package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_lmis_ta_office_location_view")
public class LMIS_TAOfficeLocationViewDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3633210327194953631L;

	@Id
	@Column(name="loc_code")
	String locationCode;
	
	@Column(name="loc_desc")
	String locationDesc;
	
	@Column(name="internal_loc_desc")
	String internalLocationDesc;
	
	@Column(name="onl_loc_desc")
	String onlineLocationDesc;
	
	@Column(name="status_code")
	String statusCode;
	
	@Column(name="gl_region_id")
	String glRegionId;
	
	@Column(name="addr_line1")
	String addressLine1;
	
	@Column(name="addr_line2")
	String addressLine2;
	
	@Column(name="parish_code")
	String parishCode;
	
	@Column(name="phone_no")
	String phoneNo;
	
	@Column(name="fax_no")
	String faxNo;
	
	@Column(name="email_addr")
	String emailAddress;

	public LMIS_TAOfficeLocationViewDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public String getInternalLocationDesc() {
		return internalLocationDesc;
	}

	public void setInternalLocationDesc(String internalLocationDesc) {
		this.internalLocationDesc = internalLocationDesc;
	}

	public String getOnlineLocationDesc() {
		return onlineLocationDesc;
	}

	public void setOnlineLocationDesc(String onlineLocationDesc) {
		this.onlineLocationDesc = onlineLocationDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getGlRegionId() {
		return glRegionId;
	}

	public void setGlRegionId(String glRegionId) {
		this.glRegionId = glRegionId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
}
