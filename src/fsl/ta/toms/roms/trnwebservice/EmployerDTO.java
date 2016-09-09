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
 * <p>Java class for EmployerDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EmployerDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="employerAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employerBranchNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employerTRN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmployerDTO", namespace = "http://dto.common.webservices.fsl.org", propOrder = {
    "employerAddress",
    "employerBranchNbr",
    "employerName",
    "employerTRN"
})
public class EmployerDTO
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected String employerAddress;
    @XmlElement(required = true, nillable = true)
    protected String employerBranchNbr;
    @XmlElement(required = true, nillable = true)
    protected String employerName;
    @XmlElement(required = true, nillable = true)
    protected String employerTRN;

    /**
     * Gets the value of the employerAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerAddress() {
        return employerAddress;
    }

    /**
     * Sets the value of the employerAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerAddress(String value) {
        this.employerAddress = value;
    }

    /**
     * Gets the value of the employerBranchNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerBranchNbr() {
        return employerBranchNbr;
    }

    /**
     * Sets the value of the employerBranchNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerBranchNbr(String value) {
        this.employerBranchNbr = value;
    }

    /**
     * Gets the value of the employerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerName() {
        return employerName;
    }

    /**
     * Sets the value of the employerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerName(String value) {
        this.employerName = value;
    }

    /**
     * Gets the value of the employerTRN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerTRN() {
        return employerTRN;
    }

    /**
     * Sets the value of the employerTRN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerTRN(String value) {
        this.employerTRN = value;
    }

}