package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.RowEditEvent;

public class OffenceOutcomeTableBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6131909419564729706L;

	private OffenceOutcomeDataModel offenceOutcomeDataModel;
	private List<OffenceOutcomeView> selectedOutcomes;
	

	public OffenceOutcomeTableBean() {
		super();
		OffenceOutcomeView offenceOutcomeView = new OffenceOutcomeView();
		List<OffenceOutcomeView> outcomeViewList = new ArrayList<OffenceOutcomeView>(); 
		offenceOutcomeView.setOffenceId(1);
		offenceOutcomeView.setIssueSummons(true);
		outcomeViewList.add(offenceOutcomeView);
		this.offenceOutcomeDataModel = new OffenceOutcomeDataModel(outcomeViewList);
	}

	
	
	

	public OffenceOutcomeDataModel getOffenceOutcomeDataModel() {
		return offenceOutcomeDataModel;
	}





	public void setOffenceOutcomeDataModel(
			OffenceOutcomeDataModel offenceOutcomeDataModel) {
		this.offenceOutcomeDataModel = offenceOutcomeDataModel;
	}





	

	public List<OffenceOutcomeView> getSelectedOutcomes() {
		return selectedOutcomes;
	}





	public void setSelectedOutcomes(List<OffenceOutcomeView> selectedOutcomes) {
		this.selectedOutcomes = selectedOutcomes;
	}





	public void onEdit(RowEditEvent event) {  
       System.err.println("onEdit");
    }  
      
    public void onCancel(RowEditEvent event) {  
    	System.err.println("onCancel"); 
    }  
	
}
