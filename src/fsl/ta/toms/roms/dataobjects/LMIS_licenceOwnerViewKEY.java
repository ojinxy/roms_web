/**
 * Created By: oanguin
 * Date: Jun 6, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author oanguin
 * Created Date: Jun 6, 2013
 */
public class LMIS_licenceOwnerViewKEY implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="licence_no")
	Integer licenceNo;
	
	@Column(name="cust_id")
	String customerId;

	@Column(name="owner_type")
	String ownerType;
	
	
	public Integer getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}



	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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
