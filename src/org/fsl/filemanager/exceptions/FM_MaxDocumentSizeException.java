package org.fsl.filemanager.exceptions;

public class FM_MaxDocumentSizeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_MaxDocumentSizeException(){
		super("This file is too large.");
	}
	
	public FM_MaxDocumentSizeException(String message){
		super(message);
	}
}
