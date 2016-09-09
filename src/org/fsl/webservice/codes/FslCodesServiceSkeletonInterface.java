
/**
 * FslCodesServiceSkeletonInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
    package org.fsl.webservice.codes;
    /**
     *  FslCodesServiceSkeletonInterface java skeleton interface for the axisService
     */
    public interface FslCodesServiceSkeletonInterface {
     
         
        /**
         * Auto generated method signature
         * 
                                    * @param getCodeTypes
         */

        
                public org.fsl.webservice.codes.GetCodeTypesResponse getCodeTypes
                (
                  org.fsl.webservice.codes.GetCodeTypes getCodeTypes
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getCodeDescription
             * @throws FslWebServiceException : 
         */

        
                public org.fsl.webservice.codes.GetCodeDescriptionResponse getCodeDescription
                (
                  org.fsl.webservice.codes.GetCodeDescription getCodeDescription
                 )
            throws FslWebServiceException;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getCodes
             * @throws FslWebServiceException : 
         */

        
                public org.fsl.webservice.codes.GetCodesResponse getCodes
                (
                  org.fsl.webservice.codes.GetCodes getCodes
                 )
            throws FslWebServiceException;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getUpdatedCodes
             * @throws FslWebServiceException : 
         */

        
                public org.fsl.webservice.codes.GetUpdatedCodesResponse getUpdatedCodes
                (
                  org.fsl.webservice.codes.GetUpdatedCodes getUpdatedCodes
                 )
            throws FslWebServiceException;
        
         }
    