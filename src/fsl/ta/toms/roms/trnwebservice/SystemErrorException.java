//
// Generated By:JAX-WS RI IBM 2.2.1-07/09/2014 01:53 PM(foreman)- (JAXB RI IBM 2.2.3-07/07/2014 12:56 PM(foreman)-)
//


package fsl.ta.toms.roms.trnwebservice;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SystemErrorException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SystemErrorException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strException" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemErrorException", namespace = "http://exceptions.common.webservices.fsl.org", propOrder = {
    "strException"
})
public class SystemErrorException
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected String strException;

    /**
     * Gets the value of the strException property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrException() {
        return strException;
    }

    /**
     * Sets the value of the strException property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrException(String value) {
        this.strException = value;
    }

}
