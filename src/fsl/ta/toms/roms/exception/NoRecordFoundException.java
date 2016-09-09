package fsl.ta.toms.roms.exception;

public class NoRecordFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRecordFoundException(){
		super("No Record Found");
	}
	
	public NoRecordFoundException(String message){
		super(message);
	}
}
