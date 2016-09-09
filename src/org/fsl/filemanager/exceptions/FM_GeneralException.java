package org.fsl.filemanager.exceptions;

public class FM_GeneralException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_GeneralException(){
		super("There was an error .");
	}
	
	public FM_GeneralException(String message){
		super(message);
	}
}