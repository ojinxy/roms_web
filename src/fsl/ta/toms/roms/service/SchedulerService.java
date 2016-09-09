package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.ProcessControlBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;


public interface SchedulerService {
	

	public boolean updatePoliceOfficer(String currentUser) throws NoRecordFoundException, ErrorSavingException;
	public boolean updateTAStaff(String currentUser) throws ErrorSavingException;
	public boolean updatePlea(String currentUser) throws NoRecordFoundException, ErrorSavingException;
	public boolean updateVerdict(String currentUser) throws NoRecordFoundException, ErrorSavingException;
	public boolean updateCourtRuling(String currentUser) throws NoRecordFoundException, ErrorSavingException;
	public List<ProcessControlBO> getLasRunProcesses() throws ErrorSavingException;
	
}
