//
// Generated By:JAX-WS RI IBM 2.2.1-07/09/2014 01:53 PM(foreman)- (JAXB RI IBM 2.2.3-07/07/2014 12:56 PM(foreman)-)
//


package fsl.ta.toms.roms.trnwebservice;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for IndividualDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndividualDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="branchRefNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flgStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="genderType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmMarkText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmParishCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmPoBoxNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmPoLocName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmStreetName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hmStreetNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="middleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlMarkText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlParishCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlPoBoxNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlPoLocName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlStreetName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mlStreetNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrBranch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrReferenceTrn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nbrTrn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="trnEntryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="trnEntryStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividualDTO", namespace = "http://dto.common.webservices.fsl.org", propOrder = {
    "birthDate",
    "branchRefNbr",
    "firstName",
    "flgStatus",
    "genderType",
    "hmCountryCode",
    "hmMarkText",
    "hmParishCode",
    "hmPoBoxNo",
    "hmPoLocName",
    "hmStreetName",
    "hmStreetNo",
    "lastName",
    "lastUpdate",
    "middleName",
    "mlCountryCode",
    "mlMarkText",
    "mlParishCode",
    "mlPoBoxNo",
    "mlPoLocName",
    "mlStreetName",
    "mlStreetNo",
    "nbrBranch",
    "nbrReferenceTrn",
    "nbrTrn",
    "trnEntryDate",
    "trnEntryStatus"
})
public class IndividualDTO
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar birthDate;
    @XmlElement(required = true, nillable = true)
    protected String branchRefNbr;
    @XmlElement(required = true, nillable = true)
    protected String firstName;
    @XmlElement(required = true, nillable = true)
    protected String flgStatus;
    @XmlElement(required = true, nillable = true)
    protected String genderType;
    @XmlElement(required = true, nillable = true)
    protected String hmCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String hmMarkText;
    @XmlElement(required = true, nillable = true)
    protected String hmParishCode;
    @XmlElement(required = true, nillable = true)
    protected String hmPoBoxNo;
    @XmlElement(required = true, nillable = true)
    protected String hmPoLocName;
    @XmlElement(required = true, nillable = true)
    protected String hmStreetName;
    @XmlElement(required = true, nillable = true)
    protected String hmStreetNo;
    @XmlElement(required = true, nillable = true)
    protected String lastName;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdate;
    @XmlElement(required = true, nillable = true)
    protected String middleName;
    @XmlElement(required = true, nillable = true)
    protected String mlCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String mlMarkText;
    @XmlElement(required = true, nillable = true)
    protected String mlParishCode;
    @XmlElement(required = true, nillable = true)
    protected String mlPoBoxNo;
    @XmlElement(required = true, nillable = true)
    protected String mlPoLocName;
    @XmlElement(required = true, nillable = true)
    protected String mlStreetName;
    @XmlElement(required = true, nillable = true)
    protected String mlStreetNo;
    @XmlElement(required = true, nillable = true)
    protected String nbrBranch;
    @XmlElement(required = true, nillable = true)
    protected String nbrReferenceTrn;
    @XmlElement(required = true, nillable = true)
    protected String nbrTrn;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar trnEntryDate;
    @XmlElement(required = true, nillable = true)
    protected String trnEntryStatus;

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
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

    /**
     * Gets the value of the hmCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmCountryCode() {
        return hmCountryCode;
    }

    /**
     * Sets the value of the hmCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmCountryCode(String value) {
        this.hmCountryCode = value;
    }

    /**
     * Gets the value of the hmMarkText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmMarkText() {
        return hmMarkText;
    }

    /**
     * Sets the value of the hmMarkText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmMarkText(String value) {
        this.hmMarkText = value;
    }

    /**
     * Gets the value of the hmParishCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmParishCode() {
        return hmParishCode;
    }

    /**
     * Sets the value of the hmParishCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmParishCode(String value) {
        this.hmParishCode = value;
    }

    /**
     * Gets the value of the hmPoBoxNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmPoBoxNo() {
        return hmPoBoxNo;
    }

    /**
     * Sets the value of the hmPoBoxNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmPoBoxNo(String value) {
        this.hmPoBoxNo = value;
    }

    /**
     * Gets the value of the hmPoLocName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmPoLocName() {
        return hmPoLocName;
    }

    /**
     * Sets the value of the hmPoLocName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmPoLocName(String value) {
        this.hmPoLocName = value;
    }

    /**
     * Gets the value of the hmStreetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmStreetName() {
        return hmStreetName;
    }

    /**
     * Sets the value of the hmStreetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmStreetName(String value) {
        this.hmStreetName = value;
    }

    /**
     * Gets the value of the hmStreetNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHmStreetNo() {
        return hmStreetNo;
    }

    /**
     * Sets the value of the hmStreetNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHmStreetNo(String value) {
        this.hmStreetNo = value;
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
     * Gets the value of the mlCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlCountryCode() {
        return mlCountryCode;
    }

    /**
     * Sets the value of the mlCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlCountryCode(String value) {
        this.mlCountryCode = value;
    }

    /**
     * Gets the value of the mlMarkText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlMarkText() {
        return mlMarkText;
    }

    /**
     * Sets the value of the mlMarkText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlMarkText(String value) {
        this.mlMarkText = value;
    }

    /**
     * Gets the value of the mlParishCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlParishCode() {
        return mlParishCode;
    }

    /**
     * Sets the value of the mlParishCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlParishCode(String value) {
        this.mlParishCode = value;
    }

    /**
     * Gets the value of the mlPoBoxNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlPoBoxNo() {
        return mlPoBoxNo;
    }

    /**
     * Sets the value of the mlPoBoxNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlPoBoxNo(String value) {
        this.mlPoBoxNo = value;
    }

    /**
     * Gets the value of the mlPoLocName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlPoLocName() {
        return mlPoLocName;
    }

    /**
     * Sets the value of the mlPoLocName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlPoLocName(String value) {
        this.mlPoLocName = value;
    }

    /**
     * Gets the value of the mlStreetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlStreetName() {
        return mlStreetName;
    }

    /**
     * Sets the value of the mlStreetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlStreetName(String value) {
        this.mlStreetName = value;
    }

    /**
     * Gets the value of the mlStreetNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlStreetNo() {
        return mlStreetNo;
    }

    /**
     * Sets the value of the mlStreetNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlStreetNo(String value) {
        this.mlStreetNo = value;
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

}
