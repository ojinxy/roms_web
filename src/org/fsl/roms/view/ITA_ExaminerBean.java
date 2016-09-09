package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fsl.ta.toms.roms.bo.ITAExaminerBO;

public class ITA_ExaminerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2365921570849794413L;

	
	private List<ITAExaminerBO> itaExaminerList;
	private ITAExaminerBO selectedItaExaminer;
	
	public ITA_ExaminerBean() {
		itaExaminerList = new ArrayList<ITAExaminerBO>();
		selectedItaExaminer = new ITAExaminerBO();
	}
	
	public ITA_ExaminerBean(List<ITAExaminerBO> var)
	{
		itaExaminerList = var;
		selectedItaExaminer = new ITAExaminerBO();
	}

	public List<ITAExaminerBO> getItaExaminerList() {
		return itaExaminerList;
	}

	public void setItaExaminerList(List<ITAExaminerBO> itaExaminerList) {
		this.itaExaminerList = itaExaminerList;
	}

	public ITAExaminerBO getSelectedItaExaminer() {
		return selectedItaExaminer;
	}

	public void setSelectedItaExaminer(ITAExaminerBO selectedItaExaminer) {
		this.selectedItaExaminer = selectedItaExaminer;
	}
	
	
	
}
