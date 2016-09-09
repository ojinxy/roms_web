package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.BIMS_CDStaffTypeView;

public class TAStaffTypeBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3427550203571845370L;

	private String staffTypeCode;
	private String staffTypeDesc;
	
	public TAStaffTypeBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TAStaffTypeBO(BIMS_CDStaffTypeView staffTypeView) {
		super();
		this.staffTypeCode = staffTypeView.getStaffTypeCode();
		this.staffTypeDesc = staffTypeView.getStaffTypeDesc();
	}
	public String getStaffTypeCode() {
		return staffTypeCode;
	}
	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}
	public String getStaffTypeDesc() {
		return staffTypeDesc;
	}
	public void setStaffTypeDesc(String staffTypeDesc) {
		this.staffTypeDesc = staffTypeDesc;
	}
	
	
}
