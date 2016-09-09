package org.fsl.filemanager.exceptions;

public class FM_ByteStreamEmptyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_ByteStreamEmptyException(){
		super("The byte stream is empty. Please upload file again.");
	}
	
	public FM_ByteStreamEmptyException(String message){
		super(message);
	}
}
