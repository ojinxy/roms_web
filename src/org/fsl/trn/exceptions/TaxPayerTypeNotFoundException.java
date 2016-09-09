package org.fsl.trn.exceptions;

/**
 * @author hhill
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TaxPayerTypeNotFoundException extends RuntimeException {
	
	public TaxPayerTypeNotFoundException(){
		super("There is no such taxpayer type");
	}
	
	public TaxPayerTypeNotFoundException(String message){
		super(message);	
	}
}
