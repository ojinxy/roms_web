package org.fsl.trn.exceptions;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 
 * @author $Author: oanguin $
 * 
 */
public class TrnApplicationException extends NestableRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -357771194931480601L;

	public TrnApplicationException(){
		super();		
	}
	
	public TrnApplicationException(String message){
		super(message);	
	}
	
	public TrnApplicationException(String message, Throwable t){
		super(message, t);	
	}
	
	public TrnApplicationException(Exception e)
	{
		super(e);
	}

}
