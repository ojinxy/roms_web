//
// Generated By:JAX-WS RI IBM 2.2.1-07/09/2014 01:53 PM(foreman)- (JAXB RI IBM 2.2.3-07/07/2014 12:56 PM(foreman)-)
//


package fsl.ta.toms.roms.amvswebservice;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VehFitness complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VehFitness">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fitnessNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="examDepot" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="issueDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehFitness", propOrder = {
    "fitnessNo",
    "examDepot",
    "issueDate",
    "expDate"
})
public class VehFitness
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected String fitnessNo;
    @XmlElement(required = true, nillable = true)
    protected String examDepot;
    @XmlElement(required = true, nillable = true)
    protected String issueDate;
    @XmlElement(required = true, nillable = true)
    protected String expDate;

    /**
     * Gets the value of the fitnessNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFitnessNo() {
        return fitnessNo;
    }

    /**
     * Sets the value of the fitnessNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFitnessNo(String value) {
        this.fitnessNo = value;
    }

    /**
     * Gets the value of the examDepot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExamDepot() {
        return examDepot;
    }

    /**
     * Sets the value of the examDepot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExamDepot(String value) {
        this.examDepot = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDate(String value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the expDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpDate() {
        return expDate;
    }

    /**
     * Sets the value of the expDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpDate(String value) {
        this.expDate = value;
    }

}
