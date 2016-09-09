package org.fsl.roms.view;

import java.io.Serializable;

public class OffenceOutcomeView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2680732216544768415L;
	
	public Integer offenceId;
	public boolean issueSummons;
	public boolean issuewWarningNotice;
	public boolean issueWarningNP;
	public boolean warnForProsecution;
	public boolean removePlate;
	public boolean rowSelected;
	public String  offenceShortDescription;
	public String  roadCheckType;
	public boolean firstEdit;
	public String offenceDescription;
	
	public OffenceOutcomeView() {
		super();
		// TODO Auto-generated constructor stub
		this.firstEdit=true;
	}


	public void setValues(OffenceOutcomeView offenceOutcomeView) {
		setOffenceId(offenceOutcomeView.getOffenceId());
		setIssueSummons(offenceOutcomeView.isIssueSummons());
		setIssuewWarningNotice(offenceOutcomeView.isIssuewWarningNotice());
		setIssueWarningNP(offenceOutcomeView.isIssueWarningNP());
		setWarnForProsecution(offenceOutcomeView.isWarnForProsecution());
		setRemovePlate(offenceOutcomeView.isRemovePlate());
		//this.rowSelected = offenceOutcomeView.isRowSelected();
		setOffenceShortDescription(offenceOutcomeView.getOffenceShortDescription());
		setOffenceDescription(offenceOutcomeView.getOffenceDescription());
	}


	public Integer getOffenceId() {
		return offenceId;
	}

	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	public boolean isIssueSummons() {
		return issueSummons;
	}

	public void setIssueSummons(boolean issueSummons) {
		this.issueSummons = issueSummons;
	}

	public boolean isIssuewWarningNotice() {
		return issuewWarningNotice;
	}

	public void setIssuewWarningNotice(boolean issuewWarningNotice) {
		this.issuewWarningNotice = issuewWarningNotice;
	}

	public boolean isIssueWarningNP() {
		return issueWarningNP;
	}

	public void setIssueWarningNP(boolean issueWarningNP) {
		this.issueWarningNP = issueWarningNP;
	}

	public boolean isWarnForProsecution() {
		return warnForProsecution;
	}

	public void setWarnForProsecution(boolean warnForProsecution) {
		this.warnForProsecution = warnForProsecution;
	}

	public boolean isRemovePlate() {
		return removePlate;
	}

	public void setRemovePlate(boolean removePlate) {
		this.removePlate = removePlate;
	}

	public boolean isRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(boolean rowSelected) {
		this.rowSelected = rowSelected;
	}


	public String getOffenceShortDescription() {
		System.out.println("In offenceShortDescription and value is " + offenceShortDescription);
		return offenceShortDescription;
	}


	public void setOffenceShortDescription(String offenceShortDescription) {
		this.offenceShortDescription = offenceShortDescription;
	}


	public String getRoadCheckType() {
		return roadCheckType;
	}


	public void setRoadCheckType(String roadCheckType) {
		this.roadCheckType = roadCheckType;
	}


	public boolean isFirstEdit() {
		return firstEdit;
	}


	public void setFirstEdit(boolean firstEdit) {
		this.firstEdit = firstEdit;
	}


	public String getOffenceDescription() {
		return offenceDescription;
	}


	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

		
	
}
