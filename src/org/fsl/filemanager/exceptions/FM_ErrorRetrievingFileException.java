package org.fsl.filemanager.exceptions;

public class FM_ErrorRetrievingFileException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_ErrorRetrievingFileException(){
		super("There was an eror saving the file to the File Manager system.");
	}
	
	public FM_ErrorRetrievingFileException(String message){
		super(message);
	}
}
