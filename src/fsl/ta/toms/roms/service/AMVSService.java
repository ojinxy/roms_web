/**
 * Created By: oanguin
 * Date: May 17, 2013
 *
 */
package fsl.ta.toms.roms.service;

/**
 * @author oanguin
 * Created Date: May 17, 2013
 */
public interface AMVSService 
{
	/**
	 * This function searchers the configuration table for {AMVS_WEBSERV} to find URL value for location of WSDL file.
	 * @return String - The UL for the AMVS WSDL URL
	 */
	public String getAMVSWebServiceUrl();
}
