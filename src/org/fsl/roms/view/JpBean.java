package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.JPBO;

public class JpBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 677972907168118630L;

	
	private List<JPBO> jpList;
	private JPBO selectedJP;
	
	public JpBean()
	{
		jpList = new ArrayList<JPBO>();
		selectedJP = new JPBO();
	}
	
	public JpBean(List<JPBO> var)
	{
		jpList = var;
		selectedJP = new JPBO();
	}

	public List<JPBO> getJpList() {
		return jpList;
	}

	public void setJpList(List<JPBO> jpList) {
		this.jpList = jpList;
	}

	public JPBO getSelectedJP() {
		return selectedJP;
	}

	public void setSelectedJP(JPBO selectedJP) {
		this.selectedJP = selectedJP;
	}
	
	
}
