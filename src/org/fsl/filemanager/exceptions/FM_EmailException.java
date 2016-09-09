package org.fsl.filemanager.exceptions;

public class FM_EmailException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_EmailException(){
		super("There was an error while attempting to send email. Email was not sent.");
	}
	
	public FM_EmailException(String message){
		super(message);
	}
}