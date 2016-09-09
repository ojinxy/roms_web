package org.fsl.trn.exceptions;


/**
 * @author hhill
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TaxPayerNotFoundException extends TrnApplicationException{
	
	public TaxPayerNotFoundException(){
		super("Tax Payer not found");
	}
	
	public TaxPayerNotFoundException(String message){
		super(message);	
	}
}
