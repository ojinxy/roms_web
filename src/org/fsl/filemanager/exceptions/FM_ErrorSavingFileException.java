package org.fsl.filemanager.exceptions;

public class FM_ErrorSavingFileException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_ErrorSavingFileException(){
		super("There was an eror saving the file to the File Manager system.");
	}
	
	public FM_ErrorSavingFileException(String message){
		super(message);
	}
}
