package fsl.ta.toms.roms.util;

public class TRNFormatter {
	
	public static String getFormattedBadgeNoTRN(String trn){
		String unformattedTRN="";
		String formattedTRN="";
		int trnLength=0;
		int trnLengthDiff=0;
		
		String first3Numbers="";
		String second3Numbers="";
		String last3Numbers="";
		
		unformattedTRN = trn.toString();
		trnLength = unformattedTRN.length();
		
		if(trnLength<9){
			trnLengthDiff = 9-trnLength;
			
			for(int i = 0; i<trnLengthDiff; i++){
				unformattedTRN = "0" + unformattedTRN;
			}
		}
	
		first3Numbers = unformattedTRN.substring(0, 3);
		second3Numbers = unformattedTRN.substring(3, 6);
		last3Numbers = unformattedTRN.substring(6, 9);
		
		formattedTRN = first3Numbers + " " + second3Numbers + " " + last3Numbers;
		
		return formattedTRN;
	}
	
	
	public static String getPaddedTRN(String trn){
		String unPaddedTRN="";
		String paddedTRN="";
		int trnLength=0;
		int trnLengthDiff=0;
	
		unPaddedTRN = trn.toString();
		trnLength = unPaddedTRN.length();
		
		paddedTRN = unPaddedTRN;
		
		if(trnLength<9){
			trnLengthDiff = 9-trnLength;
			
			for(int i = 0; i<trnLengthDiff; i++){
				paddedTRN = "0" + paddedTRN;
			}
		}
		
		return paddedTRN;
	}
	
	public static String getFormattedBadgeNoStaffID(String staffID){
		String unformattedStaffID="";
		String formattedStaffID="";
		int staffIDLength=0;
			
		String first3Numbers="";
		String second3Numbers="";
		String last3Numbers="";
		
		unformattedStaffID = staffID;
		staffIDLength = staffID.length();
		
		if(staffIDLength==9){
			try{
				Integer.parseInt(unformattedStaffID);
				first3Numbers = unformattedStaffID.substring(0, 3);
				second3Numbers = unformattedStaffID.substring(3, 6);
				last3Numbers = unformattedStaffID.substring(6, 9);
				formattedStaffID = first3Numbers + " " + second3Numbers + " " + last3Numbers;
				return formattedStaffID;
			}
			catch (NumberFormatException e) {
				return staffID;
			}
		}
		
		return staffID;
	}
	
	public static String getFormattedDlNo(String dlNo){
		String unformattedDlNo="";
		String formattedDlNo="";
		int dlNoLength=0;
			
		String first3Numbers="";
		String second3Numbers="";
		String last3Numbers="";
		
		unformattedDlNo = dlNo;
		dlNoLength = dlNo.length();
		
		if(dlNoLength==9){
			try{
				Integer.parseInt(unformattedDlNo);
				first3Numbers = unformattedDlNo.substring(0, 3);
				second3Numbers = unformattedDlNo.substring(3, 6);
				last3Numbers = unformattedDlNo.substring(6, 9);
				formattedDlNo = first3Numbers + "-" + second3Numbers + "-" + last3Numbers;
				return formattedDlNo;
			}
			catch (NumberFormatException e) {
				return dlNo;
			}
		}
		
		return dlNo;
	}
	
	public static boolean validateTRN(String trn){
	
		try{
			if(trn.length()>9){
				return false;
			}
			
			Integer.parseInt(trn);
			
			return true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
	}
}
