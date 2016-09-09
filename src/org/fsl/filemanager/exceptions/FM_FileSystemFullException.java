package org.fsl.filemanager.exceptions;

public class FM_FileSystemFullException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FM_FileSystemFullException(){
		super("The file system is now full. Please ask have System Administator increase space on File Manager system.");
	}
	
	public FM_FileSystemFullException(String message){
		super(message);
	}
}
