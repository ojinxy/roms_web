package org.fsl.roms.view;

import java.io.Serializable;



public class RecordOffenceOutcomeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969806436269822700L;
	
	private boolean mvSelectAllOffenceOutcome;
	private boolean dlSelectAllOffenceOutcome;
	private boolean rlSelectAllOffenceOutcome;
	private boolean bdgSelectAllOffenceOutcome;
	private boolean chSelectAllOffenceOutcome;
	private boolean otSelectAllOffenceOutcome;
	
	private String mvActionToBeTaken;
	private String dlActionToBeTaken;
	private String rlActionToBeTaken;
	private String bdgActionToBeTaken;
	private String chActionToBeTaken;
	private String otActionToBeTaken;
	
	private boolean warningNoticeSelected;
	private boolean summonsSelected;
	
	
	private String badgePhotoURL; 
	private String dlPhotoURL; 
	
	
	
	private int mvOpenRecordCount;
	private int dlOpenRecordCount;
	private int rlOpenRecordCount;
	private int bdgOpenRecordCount;
	private int chOpenRecordCount;
	private int otOpenRecordCount;
	
	
	private boolean mvTableError;
	private boolean dlTableError;
	private boolean rlTableError;
	private boolean bdgTableError;
	private boolean chTableError;
	private boolean otTableError;
	//private boolean disableTableBtn;
	//private boolean disableBadgeTab;
	
	private OffenceOutcomeView currentMVOffenceOutcomeView;
	private OffenceOutcomeView currentRLOffenceOutcomeView;
	private OffenceOutcomeView currentDLOffenceOutcomeView;
	private OffenceOutcomeView currentBdgOffenceOutcomeView;
	private OffenceOutcomeView currentCHOffenceOutcomeView;
	private OffenceOutcomeView currentOTOffenceOutcomeView;
	
	private boolean recordOffOutcomeErr;
	
	
	private boolean mvOffenceOutcomeSelected;
	private boolean dlOffenceOutcomeSelected;
	private boolean rlOffenceOutcomeSelected;
	private boolean bdgOffenceOutcomeSelected;
	private boolean chOffenceOutcomeSelected;
	private boolean otOffenceOutcomeSelected;

	public RecordOffenceOutcomeView() {
		super();

		this.mvOpenRecordCount=0;
		this.dlOpenRecordCount=0;
		this.rlOpenRecordCount=0;
		this.bdgOpenRecordCount=0;
		this.otOpenRecordCount=0;
		this.chOpenRecordCount=0;
		this.currentRLOffenceOutcomeView = new OffenceOutcomeView();
		this.currentMVOffenceOutcomeView = new OffenceOutcomeView();
		this.currentBdgOffenceOutcomeView = new  OffenceOutcomeView();
		this.currentCHOffenceOutcomeView = new  OffenceOutcomeView();
		this.currentDLOffenceOutcomeView = new OffenceOutcomeView();
		this.currentOTOffenceOutcomeView = new OffenceOutcomeView();
		
		this.mvOffenceOutcomeSelected = false;
		this.dlOffenceOutcomeSelected = false;
		this.rlOffenceOutcomeSelected = false;
		this.bdgOffenceOutcomeSelected = false;
		this.chOffenceOutcomeSelected = false;
		this.otOffenceOutcomeSelected = false;
				
	}

	
	
	public boolean isMvSelectAllOffenceOutcome() {
		return mvSelectAllOffenceOutcome;
	}



	public void setMvSelectAllOffenceOutcome(boolean mvSelectAllOffenceOutcome) {
		this.mvSelectAllOffenceOutcome = mvSelectAllOffenceOutcome;
	}



	public boolean isDlSelectAllOffenceOutcome() {
		return dlSelectAllOffenceOutcome;
	}



	public void setDlSelectAllOffenceOutcome(boolean dlSelectAllOffenceOutcome) {
		this.dlSelectAllOffenceOutcome = dlSelectAllOffenceOutcome;
	}



	public boolean isRlSelectAllOffenceOutcome() {
		return rlSelectAllOffenceOutcome;
	}



	public void setRlSelectAllOffenceOutcome(boolean rlSelectAllOffenceOutcome) {
		this.rlSelectAllOffenceOutcome = rlSelectAllOffenceOutcome;
	}



	public boolean isBdgSelectAllOffenceOutcome() {
		return bdgSelectAllOffenceOutcome;
	}



	public void setBdgSelectAllOffenceOutcome(boolean bdgSelectAllOffenceOutcome) {
		this.bdgSelectAllOffenceOutcome = bdgSelectAllOffenceOutcome;
	}



	public boolean isChSelectAllOffenceOutcome() {
		return chSelectAllOffenceOutcome;
	}



	public void setChSelectAllOffenceOutcome(boolean chSelectAllOffenceOutcome) {
		this.chSelectAllOffenceOutcome = chSelectAllOffenceOutcome;
	}



	public boolean isOtSelectAllOffenceOutcome() {
		return otSelectAllOffenceOutcome;
	}



	public void setOtSelectAllOffenceOutcome(boolean otSelectAllOffenceOutcome) {
		this.otSelectAllOffenceOutcome = otSelectAllOffenceOutcome;
	}



	public String getMvActionToBeTaken() {
		return mvActionToBeTaken;
	}


	public void setMvActionToBeTaken(String mvActionToBeTaken) {
		this.mvActionToBeTaken = mvActionToBeTaken;
	}


	public String getDlActionToBeTaken() {
		return dlActionToBeTaken;
	}


	public void setDlActionToBeTaken(String dlActionToBeTaken) {
		this.dlActionToBeTaken = dlActionToBeTaken;
	}


	public String getRlActionToBeTaken() {
		return rlActionToBeTaken;
	}


	public void setRlActionToBeTaken(String rlActionToBeTaken) {
		this.rlActionToBeTaken = rlActionToBeTaken;
	}


	public String getBdgActionToBeTaken() {
		return bdgActionToBeTaken;
	}


	public void setBdgActionToBeTaken(String bdgActionToBeTaken) {
		this.bdgActionToBeTaken = bdgActionToBeTaken;
	}


	public String getChActionToBeTaken() {
		return chActionToBeTaken;
	}


	public void setChActionToBeTaken(String chActionToBeTaken) {
		this.chActionToBeTaken = chActionToBeTaken;
	}


	public String getOtActionToBeTaken() {
		return otActionToBeTaken;
	}


	public void setOtActionToBeTaken(String otActionToBeTaken) {
		this.otActionToBeTaken = otActionToBeTaken;
	}



	public boolean isWarningNoticeSelected() {
		return warningNoticeSelected;
	}



	public void setWarningNoticeSelected(boolean warningNoticeSelected) {
		this.warningNoticeSelected = warningNoticeSelected;
	}




	public String getBadgePhotoURL() {
		return badgePhotoURL;
	}



	public void setBadgePhotoURL(String badgePhotoURL) {
		this.badgePhotoURL = badgePhotoURL;
	}






	public int getMvOpenRecordCount() {
		return mvOpenRecordCount;
	}



	public void setMvOpenRecordCount(int mvOpenRecordCount) {
		this.mvOpenRecordCount = mvOpenRecordCount;
	}



	public boolean isMvTableError() {
		return mvTableError;
	}



	public void setMvTableError(boolean mvTableError) {
		this.mvTableError = mvTableError;
	}



	public String getDlPhotoURL() {
		return dlPhotoURL;
	}



	public void setDlPhotoURL(String dlPhotoURL) {
		this.dlPhotoURL = dlPhotoURL;
	}



	public boolean isDlTableError() {
		return dlTableError;
	}



	public void setDlTableError(boolean dlTableError) {
		this.dlTableError = dlTableError;
	}



	public boolean isRlTableError() {
		return rlTableError;
	}



	public void setRlTableError(boolean rlTableError) {
		this.rlTableError = rlTableError;
	}



	public boolean isBdgTableError() {
		return bdgTableError;
	}



	public void setBdgTableError(boolean bdgTableError) {
		this.bdgTableError = bdgTableError;
	}



	public boolean isChTableError() {
		return chTableError;
	}



	public void setChTableError(boolean chTableError) {
		this.chTableError = chTableError;
	}



	public boolean isOtTableError() {
		return otTableError;
	}

	public boolean getOtTableError() {
		return otTableError;
	}



	public void setOtTableError(boolean otTableError) {
		this.otTableError = otTableError;
	}



	public int getDlOpenRecordCount() {
		return dlOpenRecordCount;
	}



	public void setDlOpenRecordCount(int dlOpenRecordCount) {
		this.dlOpenRecordCount = dlOpenRecordCount;
	}



	public int getRlOpenRecordCount() {
		return rlOpenRecordCount;
	}



	public void setRlOpenRecordCount(int rlOpenRecordCount) {
		this.rlOpenRecordCount = rlOpenRecordCount;
	}



	public int getBdgOpenRecordCount() {
		return bdgOpenRecordCount;
	}



	public void setBdgOpenRecordCount(int bdgOpenRecordCount) {
		this.bdgOpenRecordCount = bdgOpenRecordCount;
	}



	public int getChOpenRecordCount() {
		return chOpenRecordCount;
	}



	public void setChOpenRecordCount(int chOpenRecordCount) {
		this.chOpenRecordCount = chOpenRecordCount;
	}



	public int getOtOpenRecordCount() {
		return otOpenRecordCount;
	}



	public void setOtOpenRecordCount(int otOpenRecordCount) {
		this.otOpenRecordCount = otOpenRecordCount;
	}



	public OffenceOutcomeView getCurrentRLOffenceOutcomeView() {
		return currentRLOffenceOutcomeView;
	}



	public void setCurrentRLOffenceOutcomeView(
			OffenceOutcomeView currentRLOffenceOutcomeView) {
		this.currentRLOffenceOutcomeView = currentRLOffenceOutcomeView;
	}



	public OffenceOutcomeView getCurrentMVOffenceOutcomeView() {
		return currentMVOffenceOutcomeView;
	}



	public void setCurrentMVOffenceOutcomeView(
			OffenceOutcomeView currentMVOffenceOutcomeView) {
		this.currentMVOffenceOutcomeView = currentMVOffenceOutcomeView;
	}



	public OffenceOutcomeView getCurrentDLOffenceOutcomeView() {
		return currentDLOffenceOutcomeView;
	}



	public void setCurrentDLOffenceOutcomeView(
			OffenceOutcomeView currentDLOffenceOutcomeView) {
		this.currentDLOffenceOutcomeView = currentDLOffenceOutcomeView;
	}



	public OffenceOutcomeView getCurrentBdgOffenceOutcomeView() {
		return currentBdgOffenceOutcomeView;
	}



	public void setCurrentBdgOffenceOutcomeView(
			OffenceOutcomeView currentBdgOffenceOutcomeView) {
		this.currentBdgOffenceOutcomeView = currentBdgOffenceOutcomeView;
	}



	public OffenceOutcomeView getCurrentCHOffenceOutcomeView() {
		return currentCHOffenceOutcomeView;
	}



	public void setCurrentCHOffenceOutcomeView(
			OffenceOutcomeView currentCHOffenceOutcomeView) {
		this.currentCHOffenceOutcomeView = currentCHOffenceOutcomeView;
	}



	public OffenceOutcomeView getCurrentOTOffenceOutcomeView() {
		return currentOTOffenceOutcomeView;
	}



	public void setCurrentOTOffenceOutcomeView(
			OffenceOutcomeView currentOTOffenceOutcomeView) {
		this.currentOTOffenceOutcomeView = currentOTOffenceOutcomeView;
	}



	public boolean isRecordOffOutcomeErr() {
		return recordOffOutcomeErr;
	}



	public void setRecordOffOutcomeErr(boolean recordOffOutcomeErr) {
		this.recordOffOutcomeErr = recordOffOutcomeErr;
	}



	public boolean isSummonsSelected() {
		return summonsSelected;
	}



	public void setSummonsSelected(boolean summonsSelected) {
		this.summonsSelected = summonsSelected;
	}



	public boolean isMvOffenceOutcomeSelected() {
		return mvOffenceOutcomeSelected;
	}



	public void setMvOffenceOutcomeSelected(boolean mvOffenceOutcomeSelected) {
		this.mvOffenceOutcomeSelected = mvOffenceOutcomeSelected;
	}



	public boolean isDlOffenceOutcomeSelected() {
		return dlOffenceOutcomeSelected;
	}



	public void setDlOffenceOutcomeSelected(boolean dlOffenceOutcomeSelected) {
		this.dlOffenceOutcomeSelected = dlOffenceOutcomeSelected;
	}



	public boolean isRlOffenceOutcomeSelected() {
		return rlOffenceOutcomeSelected;
	}



	public void setRlOffenceOutcomeSelected(boolean rlOffenceOutcomeSelected) {
		this.rlOffenceOutcomeSelected = rlOffenceOutcomeSelected;
	}



	public boolean isBdgOffenceOutcomeSelected() {
		return bdgOffenceOutcomeSelected;
	}



	public void setBdgOffenceOutcomeSelected(boolean bdgOffenceOutcomeSelected) {
		this.bdgOffenceOutcomeSelected = bdgOffenceOutcomeSelected;
	}



	public boolean isChOffenceOutcomeSelected() {
		return chOffenceOutcomeSelected;
	}



	public void setChOffenceOutcomeSelected(boolean chOffenceOutcomeSelected) {
		this.chOffenceOutcomeSelected = chOffenceOutcomeSelected;
	}



	public boolean isOtOffenceOutcomeSelected() {
		return otOffenceOutcomeSelected;
	}



	public void setOtOffenceOutcomeSelected(boolean otOffenceOutcomeSelected) {
		this.otOffenceOutcomeSelected = otOffenceOutcomeSelected;
	}






	

	

}
