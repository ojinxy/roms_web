//
// Generated By:JAX-WS RI IBM 2.2.1-07/09/2014 01:53 PM(foreman)- (JAXB RI IBM 2.2.3-07/07/2014 12:56 PM(foreman)-)
//


package fsl.ta.toms.roms.trnwebservice;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="updatedTRNListReturn" type="{http://services.TRNValidator.webservices.fsl.org}ArrayOf_544442603_1440052060_nillable_string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "updatedTRNListReturn"
})
@XmlRootElement(name = "updatedTRNListResponse")
public class UpdatedTRNListResponse
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected ArrayOf5444426031440052060NillableString updatedTRNListReturn;

    /**
     * Gets the value of the updatedTRNListReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOf5444426031440052060NillableString }
     *     
     */
    public ArrayOf5444426031440052060NillableString getUpdatedTRNListReturn() {
        return updatedTRNListReturn;
    }

    /**
     * Sets the value of the updatedTRNListReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOf5444426031440052060NillableString }
     *     
     */
    public void setUpdatedTRNListReturn(ArrayOf5444426031440052060NillableString value) {
        this.updatedTRNListReturn = value;
    }

}
