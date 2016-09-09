package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;

public class UserSetupView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3157464791976326822L;
	
	
	private String userName;
	private String staffType;
	private List<String> selectedRegions;
	private BIMSStaffViewBO staffViewBO;
	
	private String currentStaffId;
	private String currentUserName;
	
	private boolean tableError;
	
	public UserSetupView() {
		super();
		this.currentStaffId="";
		this.staffType="";
		this.userName="";
		this.selectedRegions = new ArrayList<String>();
		this.staffViewBO = new BIMSStaffViewBO();
		this.tableError = false;
		this.currentUserName="";
	}



	public String getCurrentStaffId() {
		return currentStaffId;
	}



	public void setCurrentStaffId(String currentStaffId) {
		this.currentStaffId = currentStaffId;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public List<String> getSelectedRegions() {
		return selectedRegions;
	}

	public void setSelectedRegions(List<String> selectedRegions) {
		this.selectedRegions = selectedRegions;
	}

	public BIMSStaffViewBO getStaffViewBO() {
		return staffViewBO;
	}

	public void setStaffViewBO(BIMSStaffViewBO staffViewBO) {
		this.staffViewBO = staffViewBO;
	}

	public boolean isTableError() {
		return tableError;
	}



	public void setTableError(boolean tableError) {
		this.tableError = tableError;
	}



	public String getCurrentUserName() {
		return currentUserName;
	}



	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}



	public void clear(){
		this.currentStaffId="";
		this.staffType="";
		this.userName="";
		this.selectedRegions = new ArrayList<String>();
		this.staffViewBO = new BIMSStaffViewBO();
		this.tableError = false;
		this.currentUserName="";
	}
	
	
}
