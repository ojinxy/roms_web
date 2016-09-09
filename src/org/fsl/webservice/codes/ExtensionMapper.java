
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package org.fsl.webservice.codes;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://codes.webservice.fsl.org".equals(namespaceURI) &&
                  "ArrayOf_tns1_nillable_CodeType".equals(typeName)){
                   
                            return  org.fsl.webservice.codes.ArrayOf_tns1_nillable_CodeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://objects.codes.webservice.fsl.org".equals(namespaceURI) &&
                  "CodeType".equals(typeName)){
                   
                            return  org.fsl.webservice.codes.objects.CodeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://exceptions.webservice.fsl.org".equals(namespaceURI) &&
                  "FslWebServiceException".equals(typeName)){
                   
                            return  org.fsl.webservice.exceptions.FslWebServiceException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://codes.webservice.fsl.org".equals(namespaceURI) &&
                  "ArrayOf_xsd_nillable_string".equals(typeName)){
                   
                            return  org.fsl.webservice.codes.ArrayOf_xsd_nillable_string.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    