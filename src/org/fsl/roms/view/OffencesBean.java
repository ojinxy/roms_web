package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.OffenceBO;

public class OffencesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3406622620218788977L;

	private List<OffenceBO> offenceList;
	private OffenceBO selectedOffence;
	
	public OffencesBean()
	{
		offenceList = new ArrayList<OffenceBO>();
		selectedOffence = new OffenceBO();
	}
	
	public OffencesBean(List<OffenceBO> var)
	{
		offenceList = var;
		selectedOffence = new OffenceBO();
	}

	public List<OffenceBO> getOffenceList() {
		return offenceList;
	}

	public void setOffenceList(List<OffenceBO> offenceList) {
		this.offenceList = offenceList;
	}

	public OffenceBO getSelectedOffence() {
		return selectedOffence;
	}

	public void setSelectedOffence(OffenceBO selectedOffence) {
		this.selectedOffence = selectedOffence;
	}
	
	
	
}
