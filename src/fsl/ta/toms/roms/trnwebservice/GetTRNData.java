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
 *         &lt;element name="trnNbr" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="branchNbr" type="{http://www.w3.org/2001/XMLSchema}short"/>
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
    "trnNbr",
    "branchNbr"
})
@XmlRootElement(name = "getTRNData")
public class GetTRNData
    implements Serializable
{

    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer trnNbr;
    @XmlElement(required = true, type = Short.class, nillable = true)
    protected Short branchNbr;

    /**
     * Gets the value of the trnNbr property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTrnNbr() {
        return trnNbr;
    }

    /**
     * Sets the value of the trnNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTrnNbr(Integer value) {
        this.trnNbr = value;
    }

    /**
     * Gets the value of the branchNbr property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getBranchNbr() {
        return branchNbr;
    }

    /**
     * Sets the value of the branchNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setBranchNbr(Short value) {
        this.branchNbr = value;
    }

}
