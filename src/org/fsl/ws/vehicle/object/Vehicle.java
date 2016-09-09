//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-07/05/2013 05:25 AM(foreman)-)
//


package org.fsl.ws.vehicle.object;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Vehicle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Vehicle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vehInfo" type="{http://object.vehicle.ws.fsl.org}VehInfo"/>
 *         &lt;element name="vehFitness" type="{http://object.vehicle.ws.fsl.org}VehFitness"/>
 *         &lt;element name="vehInsurance" type="{http://object.vehicle.ws.fsl.org}VehInsurance"/>
 *         &lt;element name="vehOwners" type="{http://object.vehicle.ws.fsl.org}ArrayOfVehOwner"/>
 *         &lt;element name="vehMvrc" type="{http://object.vehicle.ws.fsl.org}VehMvrc"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vehicle", propOrder = {
    "vehInfo",
    "vehFitness",
    "vehInsurance",
    "vehOwners",
    "vehMvrc"
})
public class Vehicle
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected VehInfo vehInfo;
    @XmlElement(required = true, nillable = true)
    protected VehFitness vehFitness;
    @XmlElement(required = true, nillable = true)
    protected VehInsurance vehInsurance;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfVehOwner vehOwners;
    @XmlElement(required = true, nillable = true)
    protected VehMvrc vehMvrc;

    /**
     * Gets the value of the vehInfo property.
     * 
     * @return
     *     possible object is
     *     {@link VehInfo }
     *     
     */
    public VehInfo getVehInfo() {
        return vehInfo;
    }

    /**
     * Sets the value of the vehInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehInfo }
     *     
     */
    public void setVehInfo(VehInfo value) {
        this.vehInfo = value;
    }

    /**
     * Gets the value of the vehFitness property.
     * 
     * @return
     *     possible object is
     *     {@link VehFitness }
     *     
     */
    public VehFitness getVehFitness() {
        return vehFitness;
    }

    /**
     * Sets the value of the vehFitness property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehFitness }
     *     
     */
    public void setVehFitness(VehFitness value) {
        this.vehFitness = value;
    }

    /**
     * Gets the value of the vehInsurance property.
     * 
     * @return
     *     possible object is
     *     {@link VehInsurance }
     *     
     */
    public VehInsurance getVehInsurance() {
        return vehInsurance;
    }

    /**
     * Sets the value of the vehInsurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehInsurance }
     *     
     */
    public void setVehInsurance(VehInsurance value) {
        this.vehInsurance = value;
    }

    /**
     * Gets the value of the vehOwners property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfVehOwner }
     *     
     */
    public ArrayOfVehOwner getVehOwners() {
        return vehOwners;
    }

    /**
     * Sets the value of the vehOwners property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfVehOwner }
     *     
     */
    public void setVehOwners(ArrayOfVehOwner value) {
        this.vehOwners = value;
    }

    /**
     * Gets the value of the vehMvrc property.
     * 
     * @return
     *     possible object is
     *     {@link VehMvrc }
     *     
     */
    public VehMvrc getVehMvrc() {
        return vehMvrc;
    }

    /**
     * Sets the value of the vehMvrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehMvrc }
     *     
     */
    public void setVehMvrc(VehMvrc value) {
        this.vehMvrc = value;
    }

}
