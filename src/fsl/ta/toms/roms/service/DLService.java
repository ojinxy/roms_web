/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.service;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public interface DLService 
{
	/**
	 * This function looks up the drivers license web service WSDL URL from the 
	 * configuration table. THe field being searched for in the database is {DL_WEBSERV}
	 * @return
	 */
	public String getDLWebServiceURL();
}
