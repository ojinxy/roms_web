//
// Generated By:JAX-WS RI IBM 2.2.1-07/09/2014 01:53 PM(foreman)- (JAXB RI IBM 2.2.3-07/07/2014 12:56 PM(foreman)-)
//


package fsl.ta.toms.roms.trnwebservice;

import javax.xml.ws.WebFault;

@WebFault(name = "SystemErrorException", targetNamespace = "http://exceptions.common.webservices.fsl.org")
public class SystemErrorException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SystemErrorException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public SystemErrorException_Exception(String message, SystemErrorException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public SystemErrorException_Exception(String message, SystemErrorException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: fsl.ta.toms.roms.trnwebservice.SystemErrorException
     */
    public SystemErrorException getFaultInfo() {
        return faultInfo;
    }

}