package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.StrategyBO;

public class StrategyTableBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2986297779194435737L;

	private List<StrategyBO> strategyList;
	
	private StrategyBO selectedStrategy;

	//private Selectable
	
	/* public void onRowSelect(SelectEvent event) {  
		 
		 System.err.println("On row select called");
	        FacesMessage msg = new FacesMessage("Selected", ((StrategyBO) event.getObject()));  
	  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	    }  */
	
	public StrategyTableBean() {
		strategyList = new ArrayList<StrategyBO>();
		selectedStrategy = new StrategyBO();
	}
	
	public StrategyTableBean(List<StrategyBO> var) {
		strategyList = var;
		selectedStrategy = new StrategyBO();
	}
	
	
	public List<StrategyBO> getStrategyList() {
		return strategyList;
	}

	public void setStrategyList(List<StrategyBO> strategyList) {
		this.strategyList = strategyList;
	}

	public StrategyBO getSelectedStrategy() {
		return selectedStrategy;
	}

	public void setSelectedStrategy(StrategyBO selectedStrategy) {
		this.selectedStrategy = selectedStrategy;
	}
	
	
	

}
