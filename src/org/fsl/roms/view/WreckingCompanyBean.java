package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.WreckingCompanyBO;

public class WreckingCompanyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -378353677702247339L;



	private List<WreckingCompanyBO> wreckingList;
	private WreckingCompanyBO selectedWreckingCompany;
	
	public WreckingCompanyBean() {
		wreckingList = new ArrayList<WreckingCompanyBO>();
		selectedWreckingCompany = new WreckingCompanyBO();
	}
	
	public WreckingCompanyBean(List<WreckingCompanyBO> var)
	{
		wreckingList = var;
		selectedWreckingCompany = new WreckingCompanyBO();
	}

	public List<WreckingCompanyBO> getWreckingList() {
		return wreckingList;
	}

	public void setWreckingList(List<WreckingCompanyBO> wreckingList) {
		this.wreckingList = wreckingList;
	}

	public WreckingCompanyBO getSelectedWreckingCompany() {
		return selectedWreckingCompany;
	}

	public void setSelectedWreckingCompany(WreckingCompanyBO selectedWreckingCompany) {
		this.selectedWreckingCompany = selectedWreckingCompany;
	}

	
	
}
