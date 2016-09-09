package fsl.ta.toms.roms.exception;

public class UserMappingException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserMappingException(){
		super("User not mapped ");
	}
	
	public UserMappingException(String message){
		super(message);
	}
}
