package fsl.ta.toms.roms.trnwebservice;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.datatype.XMLGregorianCalendar;

public class TrnValidatorProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private fsl.ta.toms.roms.trnwebservice.TrnValidatorService _service = null;
        private fsl.ta.toms.roms.trnwebservice.TrnValidatorSEI _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new fsl.ta.toms.roms.trnwebservice.TrnValidatorService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (fsl.ta.toms.roms.trnwebservice.TrnValidatorService)ctx.lookup("java:comp/env/service/TrnValidatorService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new fsl.ta.toms.roms.trnwebservice.TrnValidatorService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getTrnValidator();
        }

        public fsl.ta.toms.roms.trnwebservice.TrnValidatorSEI getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://services.TRNValidator.webservices.fsl.org", "TrnValidator");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }

        public void setMTOMEnabled(boolean enable) {
            SOAPBinding binding = (SOAPBinding) ((BindingProvider) _proxy).getBinding();
            binding.setMTOMEnabled(enable);
        }
    }

    public TrnValidatorProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public TrnValidatorProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public BusinessDTO getBusiness(Integer trnNbr, Short branchNbr) throws InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, RetiredTRNException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getBusiness(trnNbr,branchNbr);
    }

    public String getFSLService(String value) throws IncompleteTRNException_Exception, InvalidTRNException_Exception, InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, RetiredTRNException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getFSLService(value);
    }

    public BusinessDetailsDTO getBusinessDetails(Integer trnNbr) throws IncompleteTRNException_Exception, InvalidTRNException_Exception, InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, RetiredTRNException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getBusinessDetails(trnNbr);
    }

    public IndividualDetailsDTO getInvidualDetails(Integer trnNbr) throws IncompleteTRNException_Exception, InvalidTRNException_Exception, InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getInvidualDetails(trnNbr);
    }

    public IndividualDTO getIndividual(Integer trnNbr) throws IncompleteTRNException_Exception, InvalidTRNException_Exception, NotValidTrnTypeException_Exception, RetiredTRNException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getIndividual(trnNbr);
    }

    public IndividualExtendedDTO getIndividualExtended(String trnNbr) throws InvalidInputException_Exception, InvalidTRNException_Exception, NotValidTrnTypeException_Exception, RequiredInputMissingException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getIndividualExtended(trnNbr);
    }

    public TrnDTO validateTRN(Integer trnNbr, Short branchNbr) throws InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().validateTRN(trnNbr,branchNbr);
    }

    public TrnExtendedDTO getTRNData(Integer trnNbr, Short branchNbr) throws IncompleteTRNException_Exception, InvalidTRNException_Exception, InvalidTrnBranchException_Exception, NotValidTrnTypeException_Exception, RetiredTRNException_Exception, SystemErrorException_Exception {
        return _getDescriptor().getProxy().getTRNData(trnNbr,branchNbr);
    }

    public TrnExtendedDTO validateProvisionalTrn(Integer trnNbr, Short branchNbr) throws InvalidTRNException_Exception {
        return _getDescriptor().getProxy().validateProvisionalTrn(trnNbr,branchNbr);
    }

    public ArrayOf5444426031440052060NillableString updatedTRNList(XMLGregorianCalendar startPeriod, XMLGregorianCalendar endPeriod) throws SystemErrorException_Exception {
        return _getDescriptor().getProxy().updatedTRNList(startPeriod,endPeriod);
    }

}