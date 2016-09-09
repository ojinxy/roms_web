package org.fsl.filemanager.exceptions;

public class FM_RootDirectoryException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_RootDirectoryException(){
		super("The directory does not exist.");
	}
	
	public FM_RootDirectoryException(String nameOfdirectory){
		super("The directory " + nameOfdirectory +" does not exist.");
	}
}