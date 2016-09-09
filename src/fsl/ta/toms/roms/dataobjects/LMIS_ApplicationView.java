/**
 * Created By: oanguin
 * Date: Dec 8, 2014
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author oanguin
 * Created Date: Dec 8, 2014
 */
@Entity
@Table(name="roms_lmis_application_view")
public class LMIS_ApplicationView implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="appl_no")
	private Integer applNo;
	
	@Column(name="appl_date")
	private Date applDate;
	
	@Column(name="lic_desc")
	private String licDesc;
	
	@Column(name="status_desc")
	private String statusDesc;
	
	@Column(name="lic_plate")
	private String licPlate;
	
	
	
	
	
	public LMIS_ApplicationView()
	{
		super();
		
	}

	public LMIS_ApplicationView(Integer applNo, Date applDate, String licDesc,
			String statusDesc, String licPlate)
	{
		super();
		this.applNo = applNo;
		this.applDate = applDate;
		this.licDesc = licDesc;
		this.statusDesc = statusDesc;
		this.licPlate = licPlate;
	}

	public Integer getApplNo()
	{
		return applNo;
	}

	public void setApplNo(Integer applNo)
	{
		this.applNo = applNo;
	}

	public Date getApplDate()
	{
		return applDate;
	}

	public void setApplDate(Date applDate)
	{
		this.applDate = applDate;
	}

	public String getLicDesc()
	{
		return licDesc;
	}

	public void setLicDesc(String licDesc)
	{
		this.licDesc = licDesc;
	}

	public String getStatusDesc()
	{
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc)
	{
		this.statusDesc = statusDesc;
	}

	public String getLicPlate()
	{
		return licPlate;
	}

	public void setLicPlate(String licPlate)
	{
		this.licPlate = licPlate;
	}
	
	
	
	
	
}
