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
 *         &lt;element name="getTRNDataReturn" type="{http://dto.common.webservices.fsl.org}TrnExtendedDTO"/>
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
    "getTRNDataReturn"
})
@XmlRootElement(name = "getTRNDataResponse")
public class GetTRNDataResponse
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected TrnExtendedDTO getTRNDataReturn;

    /**
     * Gets the value of the getTRNDataReturn property.
     * 
     * @return
     *     possible object is
     *     {@link TrnExtendedDTO }
     *     
     */
    public TrnExtendedDTO getGetTRNDataReturn() {
        return getTRNDataReturn;
    }

    /**
     * Sets the value of the getTRNDataReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrnExtendedDTO }
     *     
     */
    public void setGetTRNDataReturn(TrnExtendedDTO value) {
        this.getTRNDataReturn = value;
    }

}
