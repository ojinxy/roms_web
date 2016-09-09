package org.fsl.trn.exceptions;


/**
 * @author hhill
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TaxPayerUnintendedException extends Exception{
	
	public TaxPayerUnintendedException(){
		super("Tax Payer not found");
	}
	
	public TaxPayerUnintendedException(String message){
		super(message);	
	}
}
