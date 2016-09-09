package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.CourtBO;

public class CourtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6769889790679870738L;

	private List<CourtBO> courtList;
	
	private CourtBO selectedCourt;
	
	
	public CourtBean() {
		courtList = new ArrayList<CourtBO>();
		selectedCourt = new CourtBO();
	}
	
	public CourtBean(List<CourtBO> var) {
		courtList = var;
		selectedCourt = new CourtBO();
	}

	public List<CourtBO> getCourtList() {
		return courtList;
	}

	public void setCourtList(List<CourtBO> courtList) {
		this.courtList = courtList;
	}

	public CourtBO getSelectedCourt() {
		return selectedCourt;
	}

	public void setSelectedCourt(CourtBO selectedCourt) {
		this.selectedCourt = selectedCourt;
	}
	
	
}
