//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package org.fsl.webservices.common.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TrnDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrnDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="formattedName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="businessTrn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="individualTrn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="badChequeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="branchRefNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flgStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flgTrnType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="middleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrBranch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrReferenceTrn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrTrn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="registered" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rpdAlert" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="trnEntryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="trnEntryStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="addrCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrMarkText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrParishCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrPoBoxNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrPoLocName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrStreetName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrStreetNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="maritalStat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrCountryDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addrParishDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthMiddleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="genderType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrnDTO", propOrder = {
    "formattedName",
    "businessTrn",
    "individualTrn",
    "address",
    "badChequeId",
    "branchRefNbr",
    "firstName",
    "flgStatus",
    "flgTrnType",
    "lastName",
    "middleName",
    "nbrBranch",
    "nbrReferenceTrn",
    "nbrTrn",
    "registered",
    "rpdAlert",
    "transId",
    "trnEntryDate",
    "trnEntryStatus",
    "lastUpdate",
    "addrCountryCode",
    "addrMarkText",
    "addrParishCode",
    "addrPoBoxNo",
    "addrPoLocName",
    "addrStreetName",
    "addrStreetNo",
    "maritalStat",
    "addrCountryDesc",
    "addrParishDesc",
    "birthFirstName",
    "birthLastName",
    "birthMiddleName",
    "genderType"
})
public class TrnDTO
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected String formattedName;
    protected boolean businessTrn;
    protected boolean individualTrn;
    @XmlElement(required = true, nillable = true)
    protected String address;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer badChequeId;
    @XmlElement(required = true, nillable = true)
    protected String branchRefNbr;
    @XmlElement(required = true, nillable = true)
    protected String firstName;
    @XmlElement(required = true, nillable = true)
    protected String flgStatus;
    @XmlElement(required = true, nillable = true)
    protected String flgTrnType;
    @XmlElement(required = true, nillable = true)
    protected String lastName;
    @XmlElement(required = true, nillable = true)
    protected String middleName;
    @XmlElement(required = true, nillable = true)
    protected String nbrBranch;
    @XmlElement(required = true, nillable = true)
    protected String nbrReferenceTrn;
    @XmlElement(required = true, nillable = true)
    protected String nbrTrn;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer registered;
    @XmlElement(required = true, nillable = true)
    protected String rpdAlert;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer transId;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar trnEntryDate;
    @XmlElement(required = true, nillable = true)
    protected String trnEntryStatus;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdate;
    @XmlElement(required = true, nillable = true)
    protected String addrCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String addrMarkText;
    @XmlElement(required = true, nillable = true)
    protected String addrParishCode;
    @XmlElement(required = true, nillable = true)
    protected String addrPoBoxNo;
    @XmlElement(required = true, nillable = true)
    protected String addrPoLocName;
    @XmlElement(required = true, nillable = true)
    protected String addrStreetName;
    @XmlElement(required = true, nillable = true)
    protected String addrStreetNo;
    @XmlElement(required = true, nillable = true)
    protected String maritalStat;
    @XmlElement(required = true, nillable = true)
    protected String addrCountryDesc;
    @XmlElement(required = true, nillable = true)
    protected String addrParishDesc;
    @XmlElement(required = true, nillable = true)
    protected String birthFirstName;
    @XmlElement(required = true, nillable = true)
    protected String birthLastName;
    @XmlElement(required = true, nillable = true)
    protected String birthMiddleName;
    @XmlElement(required = true, nillable = true)
    protected String genderType;

    /**
     * Gets the value of the formattedName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Sets the value of the formattedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormattedName(String value) {
        this.formattedName = value;
    }

    /**
     * Gets the value of the businessTrn property.
     * 
     */
    public boolean isBusinessTrn() {
        return businessTrn;
    }

    /**
     * Sets the value of the businessTrn property.
     * 
     */
    public void setBusinessTrn(boolean value) {
        this.businessTrn = value;
    }

    /**
     * Gets the value of the individualTrn property.
     * 
     */
    public boolean isIndividualTrn() {
        return individualTrn;
    }

    /**
     * Sets the value of the individualTrn property.
     * 
     */
    public void setIndividualTrn(boolean value) {
        this.individualTrn = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the badChequeId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBadChequeId() {
        return badChequeId;
    }

    /**
     * Sets the value of the badChequeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBadChequeId(Integer value) {
        this.badChequeId = value;
    }

    /**
     * Gets the value of the branchRefNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchRefNbr() {
        return branchRefNbr;
    }

    /**
     * Sets the value of the branchRefNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchRefNbr(String value) {
        this.branchRefNbr = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the flgStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlgStatus() {
        return flgStatus;
    }

    /**
     * Sets the value of the flgStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlgStatus(String value) {
        this.flgStatus = value;
    }

    /**
     * Gets the value of the flgTrnType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlgTrnType() {
        return flgTrnType;
    }

    /**
     * Sets the value of the flgTrnType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlgTrnType(String value) {
        this.flgTrnType = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the nbrBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbrBranch() {
        return nbrBranch;
    }

    /**
     * Sets the value of the nbrBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbrBranch(String value) {
        this.nbrBranch = value;
    }

    /**
     * Gets the value of the nbrReferenceTrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbrReferenceTrn() {
        return nbrReferenceTrn;
    }

    /**
     * Sets the value of the nbrReferenceTrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbrReferenceTrn(String value) {
        this.nbrReferenceTrn = value;
    }

    /**
     * Gets the value of the nbrTrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbrTrn() {
        return nbrTrn;
    }

    /**
     * Sets the value of the nbrTrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbrTrn(String value) {
        this.nbrTrn = value;
    }

    /**
     * Gets the value of the registered property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegistered() {
        return registered;
    }

    /**
     * Sets the value of the registered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegistered(Integer value) {
        this.registered = value;
    }

    /**
     * Gets the value of the rpdAlert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRpdAlert() {
        return rpdAlert;
    }

    /**
     * Sets the value of the rpdAlert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRpdAlert(String value) {
        this.rpdAlert = value;
    }

    /**
     * Gets the value of the transId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransId() {
        return transId;
    }

    /**
     * Sets the value of the transId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransId(Integer value) {
        this.transId = value;
    }

    /**
     * Gets the value of the trnEntryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTrnEntryDate() {
        return trnEntryDate;
    }

    /**
     * Sets the value of the trnEntryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTrnEntryDate(XMLGregorianCalendar value) {
        this.trnEntryDate = value;
    }

    /**
     * Gets the value of the trnEntryStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrnEntryStatus() {
        return trnEntryStatus;
    }

    /**
     * Sets the value of the trnEntryStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrnEntryStatus(String value) {
        this.trnEntryStatus = value;
    }

    /**
     * Gets the value of the lastUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the value of the lastUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdate(XMLGregorianCalendar value) {
        this.lastUpdate = value;
    }

    /**
     * Gets the value of the addrCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrCountryCode() {
        return addrCountryCode;
    }

    /**
     * Sets the value of the addrCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrCountryCode(String value) {
        this.addrCountryCode = value;
    }

    /**
     * Gets the value of the addrMarkText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrMarkText() {
        return addrMarkText;
    }

    /**
     * Sets the value of the addrMarkText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrMarkText(String value) {
        this.addrMarkText = value;
    }

    /**
     * Gets the value of the addrParishCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrParishCode() {
        return addrParishCode;
    }

    /**
     * Sets the value of the addrParishCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrParishCode(String value) {
        this.addrParishCode = value;
    }

    /**
     * Gets the value of the addrPoBoxNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrPoBoxNo() {
        return addrPoBoxNo;
    }

    /**
     * Sets the value of the addrPoBoxNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrPoBoxNo(String value) {
        this.addrPoBoxNo = value;
    }

    /**
     * Gets the value of the addrPoLocName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrPoLocName() {
        return addrPoLocName;
    }

    /**
     * Sets the value of the addrPoLocName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrPoLocName(String value) {
        this.addrPoLocName = value;
    }

    /**
     * Gets the value of the addrStreetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrStreetName() {
        return addrStreetName;
    }

    /**
     * Sets the value of the addrStreetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrStreetName(String value) {
        this.addrStreetName = value;
    }

    /**
     * Gets the value of the addrStreetNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrStreetNo() {
        return addrStreetNo;
    }

    /**
     * Sets the value of the addrStreetNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrStreetNo(String value) {
        this.addrStreetNo = value;
    }

    /**
     * Gets the value of the maritalStat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStat() {
        return maritalStat;
    }

    /**
     * Sets the value of the maritalStat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStat(String value) {
        this.maritalStat = value;
    }

    /**
     * Gets the value of the addrCountryDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrCountryDesc() {
        return addrCountryDesc;
    }

    /**
     * Sets the value of the addrCountryDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrCountryDesc(String value) {
        this.addrCountryDesc = value;
    }

    /**
     * Gets the value of the addrParishDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrParishDesc() {
        return addrParishDesc;
    }

    /**
     * Sets the value of the addrParishDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrParishDesc(String value) {
        this.addrParishDesc = value;
    }

    /**
     * Gets the value of the birthFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthFirstName() {
        return birthFirstName;
    }

    /**
     * Sets the value of the birthFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthFirstName(String value) {
        this.birthFirstName = value;
    }

    /**
     * Gets the value of the birthLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthLastName() {
        return birthLastName;
    }

    /**
     * Sets the value of the birthLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthLastName(String value) {
        this.birthLastName = value;
    }

    /**
     * Gets the value of the birthMiddleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthMiddleName() {
        return birthMiddleName;
    }

    /**
     * Sets the value of the birthMiddleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthMiddleName(String value) {
        this.birthMiddleName = value;
    }

    /**
     * Gets the value of the genderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenderType() {
        return genderType;
    }

    /**
     * Sets the value of the genderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenderType(String value) {
        this.genderType = value;
    }

}
