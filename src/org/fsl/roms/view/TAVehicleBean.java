package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.TAVehicleBO;

public class TAVehicleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2224589538548443761L;
	
	private List<TAVehicleBO> taVehicleList;
	private TAVehicleBO selectedTaVehicle;
	
	public TAVehicleBean()
	{
		taVehicleList = new ArrayList<TAVehicleBO>();
		selectedTaVehicle = new TAVehicleBO();
	}
	
	public TAVehicleBean(List<TAVehicleBO> var)
	{
		taVehicleList = var;
		selectedTaVehicle = new TAVehicleBO();
	}

	public List<TAVehicleBO> getTaVehicleList() {
		return taVehicleList;
	}

	public void setTaVehicleList(List<TAVehicleBO> taVehicleList) {
		this.taVehicleList = taVehicleList;
	}

	public TAVehicleBO getSelectedTaVehicle() {
		return selectedTaVehicle;
	}

	public void setSelectedTaVehicle(TAVehicleBO selectedTaVehicle) {
		this.selectedTaVehicle = selectedTaVehicle;
	}

	
}
