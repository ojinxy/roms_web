package fsl.ta.toms.roms.exception;

public class InvalidFieldException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFieldException(){
		super("Invalid Field");
	}
	
	public InvalidFieldException(String message){
		super(message);
	}
}
