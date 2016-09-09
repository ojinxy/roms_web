package fsl.ta.toms.roms.service;

import java.util.HashMap;

import fsl.ta.toms.roms.exception.NoRecordFoundException;

public interface TTMSCodeService {

	public String updateTTMSCode(String currentUser, String ttmsCodeType, Integer eventCode ) throws NoRecordFoundException;

	String getTTMSCodeDescription(String currentUser, String ttmsCodeType,String code) throws NoRecordFoundException;
	
	public HashMap<String,String> getTTMSPoliceStations() throws NoRecordFoundException;
	
	public HashMap<String,String> getTTMSPoliceRanks() throws NoRecordFoundException;
}
