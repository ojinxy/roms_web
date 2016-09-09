
/**
 * FslCodesServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
        package org.fsl.webservice.codes;

        /**
        *  FslCodesServiceMessageReceiverInOut message receiver
        */

        public class FslCodesServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        FslCodesServiceSkeletonInterface skel = (FslCodesServiceSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){


        

            if("getCodeTypes".equals(methodName)){
                
                org.fsl.webservice.codes.GetCodeTypesResponse getCodeTypesResponse9 = null;
	                        org.fsl.webservice.codes.GetCodeTypes wrappedParam =
                                                             (org.fsl.webservice.codes.GetCodeTypes)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.fsl.webservice.codes.GetCodeTypes.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getCodeTypesResponse9 =
                                                   
                                                   
                                                         skel.getCodeTypes(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getCodeTypesResponse9, false, new javax.xml.namespace.QName("http://codes.webservice.fsl.org",
                                                    "getCodeTypes"));
                                    } else 

            if("getCodeDescription".equals(methodName)){
                
                org.fsl.webservice.codes.GetCodeDescriptionResponse getCodeDescriptionResponse11 = null;
	                        org.fsl.webservice.codes.GetCodeDescription wrappedParam =
                                                             (org.fsl.webservice.codes.GetCodeDescription)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.fsl.webservice.codes.GetCodeDescription.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getCodeDescriptionResponse11 =
                                                   
                                                   
                                                         skel.getCodeDescription(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getCodeDescriptionResponse11, false, new javax.xml.namespace.QName("http://codes.webservice.fsl.org",
                                                    "getCodeDescription"));
                                    } else 

            if("getCodes".equals(methodName)){
                
                org.fsl.webservice.codes.GetCodesResponse getCodesResponse13 = null;
	                        org.fsl.webservice.codes.GetCodes wrappedParam =
                                                             (org.fsl.webservice.codes.GetCodes)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.fsl.webservice.codes.GetCodes.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getCodesResponse13 =
                                                   
                                                   
                                                         skel.getCodes(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getCodesResponse13, false, new javax.xml.namespace.QName("http://codes.webservice.fsl.org",
                                                    "getCodes"));
                                    } else 

            if("getUpdatedCodes".equals(methodName)){
                
                org.fsl.webservice.codes.GetUpdatedCodesResponse getUpdatedCodesResponse15 = null;
	                        org.fsl.webservice.codes.GetUpdatedCodes wrappedParam =
                                                             (org.fsl.webservice.codes.GetUpdatedCodes)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.fsl.webservice.codes.GetUpdatedCodes.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUpdatedCodesResponse15 =
                                                   
                                                   
                                                         skel.getUpdatedCodes(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUpdatedCodesResponse15, false, new javax.xml.namespace.QName("http://codes.webservice.fsl.org",
                                                    "getUpdatedCodes"));
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        } catch (FslWebServiceException e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"FslWebServiceException");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
        
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodeTypes param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodeTypes.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodeTypesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodeTypesResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodeDescription param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodeDescription.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodeDescriptionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodeDescriptionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.exceptions.FslWebServiceExceptionE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.exceptions.FslWebServiceExceptionE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodes param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodes.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetCodesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetCodesResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetUpdatedCodes param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetUpdatedCodes.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.fsl.webservice.codes.GetUpdatedCodesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.fsl.webservice.codes.GetUpdatedCodesResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.fsl.webservice.codes.GetCodeTypesResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.fsl.webservice.codes.GetCodeTypesResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.fsl.webservice.codes.GetCodeTypesResponse wrapgetCodeTypes(){
                                org.fsl.webservice.codes.GetCodeTypesResponse wrappedElement = new org.fsl.webservice.codes.GetCodeTypesResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.fsl.webservice.codes.GetCodeDescriptionResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.fsl.webservice.codes.GetCodeDescriptionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.fsl.webservice.codes.GetCodeDescriptionResponse wrapgetCodeDescription(){
                                org.fsl.webservice.codes.GetCodeDescriptionResponse wrappedElement = new org.fsl.webservice.codes.GetCodeDescriptionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.fsl.webservice.codes.GetCodesResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.fsl.webservice.codes.GetCodesResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.fsl.webservice.codes.GetCodesResponse wrapgetCodes(){
                                org.fsl.webservice.codes.GetCodesResponse wrappedElement = new org.fsl.webservice.codes.GetCodesResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.fsl.webservice.codes.GetUpdatedCodesResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.fsl.webservice.codes.GetUpdatedCodesResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.fsl.webservice.codes.GetUpdatedCodesResponse wrapgetUpdatedCodes(){
                                org.fsl.webservice.codes.GetUpdatedCodesResponse wrappedElement = new org.fsl.webservice.codes.GetUpdatedCodesResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (org.fsl.webservice.codes.GetCodeTypes.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodeTypes.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetCodeTypesResponse.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodeTypesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetCodeDescription.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodeDescription.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetCodeDescriptionResponse.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodeDescriptionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.exceptions.FslWebServiceExceptionE.class.equals(type)){
                
                           return org.fsl.webservice.exceptions.FslWebServiceExceptionE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetCodes.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodes.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetCodesResponse.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetCodesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.exceptions.FslWebServiceExceptionE.class.equals(type)){
                
                           return org.fsl.webservice.exceptions.FslWebServiceExceptionE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetUpdatedCodes.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetUpdatedCodes.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.codes.GetUpdatedCodesResponse.class.equals(type)){
                
                           return org.fsl.webservice.codes.GetUpdatedCodesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.fsl.webservice.exceptions.FslWebServiceExceptionE.class.equals(type)){
                
                           return org.fsl.webservice.exceptions.FslWebServiceExceptionE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    