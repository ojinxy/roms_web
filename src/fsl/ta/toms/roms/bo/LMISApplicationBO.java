/**
 * Created By: oanguin
 * Date: Dec 8, 2014
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import fsl.ta.toms.roms.dataobjects.LMIS_ApplicationView;

/**
 * @author oanguin
 * Created Date: Dec 8, 2014
 */
public class LMISApplicationBO implements Serializable
{

	private Integer applNo;
	
	private Date applDate;
	
	private String licDesc;
	
	private String statusDesc;
	
	private String licPlate;

	
	
	public LMISApplicationBO()
	{
		super();
	
	}

	public LMISApplicationBO(Integer applNo, Date applDate, String licDesc,
			String statusDesc, String licPlate)
	{
		super();
		this.applNo = applNo;
		this.applDate = applDate;
		this.licDesc = licDesc;
		this.statusDesc = statusDesc;
		this.licPlate = licPlate;
	}
	
	public LMISApplicationBO(LMIS_ApplicationView view)
	{
		super();
		this.applNo = view.getApplNo();
		this.applDate = view.getApplDate();
		this.licDesc = view.getLicDesc();
		this.statusDesc = view.getStatusDesc();
		this.licPlate = view.getLicPlate();
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
