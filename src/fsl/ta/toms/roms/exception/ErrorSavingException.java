package fsl.ta.toms.roms.exception;

public class ErrorSavingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorSavingException(){
		super("Error Saving");
	}
	
	public ErrorSavingException(String message){
		super(message);
	}
}
