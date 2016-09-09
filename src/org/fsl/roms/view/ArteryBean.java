package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.ArteryBO;


public class ArteryBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8540088271357713294L;
	
	private List<ArteryBO> arteryList;
	
	private ArteryBO selectedArtery;
	
	
	public ArteryBean() {
		arteryList = new ArrayList<ArteryBO>();
		selectedArtery = new ArteryBO();
	}
	
	public ArteryBean(List<ArteryBO> var) {
		arteryList = var;
		selectedArtery = new ArteryBO();
	}
	
	public List<ArteryBO> getArteryList() {
		return arteryList;
	}

	public void setArteryList(List<ArteryBO> arteryList) {
		this.arteryList = arteryList;
	}
		
	public ArteryBO getSelectedArtery() {
		return selectedArtery;
	}

	public void setSelectedArtery(ArteryBO selectedArtery) {
		this.selectedArtery = selectedArtery;
	}


}
