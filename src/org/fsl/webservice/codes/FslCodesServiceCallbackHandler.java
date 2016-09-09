
/**
 * FslCodesServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.fsl.webservice.codes;

    /**
     *  FslCodesServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class FslCodesServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public FslCodesServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public FslCodesServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getCodeTypes method
            * override this method for handling normal response from getCodeTypes operation
            */
           public void receiveResultgetCodeTypes(
                    org.fsl.webservice.codes.GetCodeTypesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCodeTypes operation
           */
            public void receiveErrorgetCodeTypes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCodeDescription method
            * override this method for handling normal response from getCodeDescription operation
            */
           public void receiveResultgetCodeDescription(
                    org.fsl.webservice.codes.GetCodeDescriptionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCodeDescription operation
           */
            public void receiveErrorgetCodeDescription(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCodes method
            * override this method for handling normal response from getCodes operation
            */
           public void receiveResultgetCodes(
                    org.fsl.webservice.codes.GetCodesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCodes operation
           */
            public void receiveErrorgetCodes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUpdatedCodes method
            * override this method for handling normal response from getUpdatedCodes operation
            */
           public void receiveResultgetUpdatedCodes(
                    org.fsl.webservice.codes.GetUpdatedCodesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUpdatedCodes operation
           */
            public void receiveErrorgetUpdatedCodes(java.lang.Exception e) {
            }
                


    }
    