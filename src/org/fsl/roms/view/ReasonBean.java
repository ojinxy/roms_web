package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.ReasonBO;


public class ReasonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5235199725191227658L;
	

	private List<ReasonBO> reasonList;
	private ReasonBO selectedReason;
	
	public ReasonBean()
	{
		reasonList = new ArrayList<ReasonBO>();
		selectedReason = new ReasonBO();
	}
	
	public ReasonBean(List<ReasonBO> var)
	{
		reasonList = var;
		selectedReason = new ReasonBO();
	}

	public List<ReasonBO> getReasonList() {
		return reasonList;
	}

	public void setReasonList(List<ReasonBO> reasonList) {
		this.reasonList = reasonList;
	}

	public ReasonBO getSelectedReason() {
		return selectedReason;
	}

	public void setSelectedReason(ReasonBO selectedReason) {
		this.selectedReason = selectedReason;
	}


	
	

}
