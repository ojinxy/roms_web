/***********************************************************************
 * Module: NMLIS-EJB
 * Team Member: hhill
 * Created: Aug 25, 2005
 ***********************************************************************/
package org.fsl.trn.exceptions;


/**
 * @author hhill
 * <br>
 * <b>Purpose:</b>
 * Indicates that a Trn is Invalid that is check digit check
 */
public class InvalidTrnBranchException extends TrnException {

	/**
	 * @param string
	 */
	public InvalidTrnBranchException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	public InvalidTrnBranchException() {
	}




}
