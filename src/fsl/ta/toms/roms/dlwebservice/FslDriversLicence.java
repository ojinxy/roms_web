//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package fsl.ta.toms.roms.dlwebservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "FslDriversLicence", targetNamespace = "http://dl.ws.fsl.org")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface FslDriversLicence {


    /**
     * 
     * @param driversLicenceNo
     * @return
     *     returns fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails
     * @throws FslWebServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "getDriversLicenceIReturn", targetNamespace = "")
    @RequestWrapper(localName = "getDriversLicenceI", targetNamespace = "http://dl.ws.fsl.org", className = "fsl.ta.toms.roms.dlwebservice.GetDriversLicenceI")
    @ResponseWrapper(localName = "getDriversLicenceIResponse", targetNamespace = "http://dl.ws.fsl.org", className = "fsl.ta.toms.roms.dlwebservice.GetDriversLicenceIResponse")
    public DriverLicenceDetails getDriversLicenceI(
        @WebParam(name = "driversLicenceNo", targetNamespace = "")
        String driversLicenceNo)
        throws FslWebServiceException_Exception
    ;

}
