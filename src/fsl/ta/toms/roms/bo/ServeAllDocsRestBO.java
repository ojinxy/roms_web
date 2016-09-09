package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.util.AddressView;

public class ServeAllDocsRestBO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<DocumentViewBO> documents; 
	public String servedByTAUser; 
	public Date servedDate; 
	public AddressView docAddress; 
	public String servedAddress;
	public String updateUser;
}
