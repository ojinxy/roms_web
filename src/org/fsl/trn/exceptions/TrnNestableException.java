/*
 * Created on Feb 14, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.fsl.trn.exceptions;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableException;



/**
 * @author othomas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TrnNestableException extends NestableException {
	
	public TrnNestableException(){
		super();
	}
	
	public TrnNestableException(Throwable e) {
		super(e);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TrnNestableException(String message, Throwable cause) {
		super(message,cause);
	}	
	
	/**
	 * @param message
	 */
	public TrnNestableException(String message){
		super(message);
	}
	
	/**
	 * @param exception
	 */
	public TrnNestableException(Exception exception){
		super(exception);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.lang.exception.NestableException#getMessage()
	 */
	public String getMessage() {
		if(StringUtils.isEmpty(super.getMessage())){
			String msg =  this.getClass().getName();
			int lastDot = msg.lastIndexOf(".");
			return msg.substring(lastDot+1,msg.length());	
		}else{
			return super.getMessage();
		}
	}

}
