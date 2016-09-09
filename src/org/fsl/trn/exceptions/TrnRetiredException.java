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
 * Indicates that a trn can be retired
 */
public class TrnRetiredException extends TrnException{

	/**
	 * @param string
	 */
	public TrnRetiredException(String string) {
		super(string);
	}
	
	public TrnRetiredException()
	{
		super();
	}

	

}
