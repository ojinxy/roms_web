
/**
 * FslWebServiceException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package org.fsl.webservice.codes;

public class FslWebServiceException extends java.lang.Exception{

    private static final long serialVersionUID = 1367417142812L;
    
    private org.fsl.webservice.exceptions.FslWebServiceExceptionE faultMessage;

    
        public FslWebServiceException() {
            super("FslWebServiceException");
        }

        public FslWebServiceException(java.lang.String s) {
           super(s);
        }

        public FslWebServiceException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FslWebServiceException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.fsl.webservice.exceptions.FslWebServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public org.fsl.webservice.exceptions.FslWebServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    