package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.PoundBO;

public class PoundBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8570875872783038164L;


	private List<PoundBO> poundList;
	private PoundBO selectedPound;
	
	public PoundBean()
	{
		poundList = new ArrayList<PoundBO>();
		selectedPound = new PoundBO();
	}
	
	public PoundBean(List<PoundBO> var)
	{
		poundList = var;
		selectedPound = new PoundBO();
	}

	public List<PoundBO> getPoundList() {
		return poundList;
	}

	public void setPoundList(List<PoundBO> poundList) {
		this.poundList = poundList;
	}

	public PoundBO getSelectedPound() {
		return selectedPound;
	}

	public void setSelectedPound(PoundBO selectedPound) {
		this.selectedPound = selectedPound;
	}

	
}
