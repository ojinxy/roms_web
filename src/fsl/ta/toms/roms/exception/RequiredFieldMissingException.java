package fsl.ta.toms.roms.exception;

public class RequiredFieldMissingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequiredFieldMissingException(){
		super("Required Field Missing");
	}
	
	public RequiredFieldMissingException(String message){
		super(message);
	}
}
