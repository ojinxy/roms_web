//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package org.fsl.webservices.common.exceptions;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.fsl.webservices.common.exceptions package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SystemErrorException_QNAME = new QName("http://exceptions.common.webservices.fsl.org", "SystemErrorException");
    private final static QName _NotValidTrnTypeException_QNAME = new QName("http://exceptions.common.webservices.fsl.org", "NotValidTrnTypeException");
    private final static QName _InvalidTrnBranchException_QNAME = new QName("http://exceptions.common.webservices.fsl.org", "InvalidTrnBranchException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.fsl.webservices.common.exceptions
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InvalidTrnBranchException }
     * 
     */
    public InvalidTrnBranchException createInvalidTrnBranchException() {
        return new InvalidTrnBranchException();
    }

    /**
     * Create an instance of {@link SystemErrorException }
     * 
     */
    public SystemErrorException createSystemErrorException() {
        return new SystemErrorException();
    }

    /**
     * Create an instance of {@link NotValidTrnTypeException }
     * 
     */
    public NotValidTrnTypeException createNotValidTrnTypeException() {
        return new NotValidTrnTypeException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SystemErrorException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://exceptions.common.webservices.fsl.org", name = "SystemErrorException")
    public JAXBElement<SystemErrorException> createSystemErrorException(SystemErrorException value) {
        return new JAXBElement<SystemErrorException>(_SystemErrorException_QNAME, SystemErrorException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotValidTrnTypeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://exceptions.common.webservices.fsl.org", name = "NotValidTrnTypeException")
    public JAXBElement<NotValidTrnTypeException> createNotValidTrnTypeException(NotValidTrnTypeException value) {
        return new JAXBElement<NotValidTrnTypeException>(_NotValidTrnTypeException_QNAME, NotValidTrnTypeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidTrnBranchException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://exceptions.common.webservices.fsl.org", name = "InvalidTrnBranchException")
    public JAXBElement<InvalidTrnBranchException> createInvalidTrnBranchException(InvalidTrnBranchException value) {
        return new JAXBElement<InvalidTrnBranchException>(_InvalidTrnBranchException_QNAME, InvalidTrnBranchException.class, null, value);
    }

}
