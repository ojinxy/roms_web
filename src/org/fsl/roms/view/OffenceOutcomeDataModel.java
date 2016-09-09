package org.fsl.roms.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class OffenceOutcomeDataModel extends ListDataModel<OffenceOutcomeView> implements Serializable,SelectableDataModel<OffenceOutcomeView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -188482990744899361L;

	/**
	 * 
	 */
	
	public OffenceOutcomeDataModel(){
		
	}
	
	public OffenceOutcomeDataModel(List<OffenceOutcomeView> offenceOutcomeList){
		super(offenceOutcomeList);
	}
	@SuppressWarnings("unchecked")
	@Override
	public OffenceOutcomeView getRowData(String rowKey) {
		System.err.println("getRowData ");
		try{
		 List<OffenceOutcomeView> offenceOutcome = (List<OffenceOutcomeView>) getWrappedData();  
         
	        for(OffenceOutcomeView offOut : offenceOutcome) {  
	            if(offOut.getOffenceId().equals(rowKey))  
	                return offOut;  
	        }  
	          
	        return null;
		}
		catch (Exception e) {
			System.err.println("getRowData exception");
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public Object getRowKey(OffenceOutcomeView offenceOutcome) {
		// TODO Auto-generated method stub
		return offenceOutcome.getOffenceId();
	}

}
