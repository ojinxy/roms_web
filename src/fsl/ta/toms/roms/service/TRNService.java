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
public interface TRNService 
{
	/**
	 * This function returns the WSDL url to be used with the Spring Proxy Configuration.
	 * The configuration is pulled from the configuration table in the database.
	 * @return
	 */
	public String getTRNWebServiceURL();
}
