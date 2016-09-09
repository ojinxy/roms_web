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
 * <p>Java class for IndividualExtendedDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndividualExtendedDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nameAtBirth" type="{http://dto.common.webservices.fsl.org}TaxpayerNameDTO"/>
 *         &lt;element name="nameChangeCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nameChangeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currentName" type="{http://dto.common.webservices.fsl.org}TaxpayerNameDTO"/>
 *         &lt;element name="trnNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthCountryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="employerInfo" type="{http://dto.common.webservices.fsl.org}EmployerDTO"/>
 *         &lt;element name="lastUpdateDtime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="mailingAddress" type="{http://dto.common.webservices.fsl.org}AddressDTO"/>
 *         &lt;element name="maritalStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="maritalStatusDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nationalityCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nationalityCountryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="natureOfBusinessCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="natureOfBusinessDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="occupationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="occupationDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="placeOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referencedTRN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="residentAddress" type="{http://dto.common.webservices.fsl.org}AddressDTO"/>
 *         &lt;element name="soleProprietorFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="trnAssignedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="trnStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividualExtendedDTO", namespace = "http://dto.common.webservices.fsl.org", propOrder = {
    "gender",
    "nameAtBirth",
    "nameChangeCode",
    "nameChangeDesc",
    "currentName",
    "trnNbr",
    "birthCountryCode",
    "birthCountryName",
    "birthDate",
    "employerInfo",
    "lastUpdateDtime",
    "mailingAddress",
    "maritalStatus",
    "maritalStatusDesc",
    "nationalityCountryCode",
    "nationalityCountryName",
    "natureOfBusinessCode",
    "natureOfBusinessDesc",
    "occupationCode",
    "occupationDesc",
    "placeOfBirth",
    "referencedTRN",
    "residentAddress",
    "soleProprietorFlag",
    "trnAssignedDate",
    "trnStatus"
})
public class IndividualExtendedDTO
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected String gender;
    @XmlElement(required = true, nillable = true)
    protected TaxpayerNameDTO nameAtBirth;
    @XmlElement(required = true, nillable = true)
    protected String nameChangeCode;
    @XmlElement(required = true, nillable = true)
    protected String nameChangeDesc;
    @XmlElement(required = true, nillable = true)
    protected TaxpayerNameDTO currentName;
    @XmlElement(required = true, nillable = true)
    protected String trnNbr;
    @XmlElement(required = true, nillable = true)
    protected String birthCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String birthCountryName;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar birthDate;
    @XmlElement(required = true, nillable = true)
    protected EmployerDTO employerInfo;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdateDtime;
    @XmlElement(required = true, nillable = true)
    protected AddressDTO mailingAddress;
    @XmlElement(required = true, nillable = true)
    protected String maritalStatus;
    @XmlElement(required = true, nillable = true)
    protected String maritalStatusDesc;
    @XmlElement(required = true, nillable = true)
    protected String nationalityCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String nationalityCountryName;
    @XmlElement(required = true, nillable = true)
    protected String natureOfBusinessCode;
    @XmlElement(required = true, nillable = true)
    protected String natureOfBusinessDesc;
    @XmlElement(required = true, nillable = true)
    protected String occupationCode;
    @XmlElement(required = true, nillable = true)
    protected String occupationDesc;
    @XmlElement(required = true, nillable = true)
    protected String placeOfBirth;
    @XmlElement(required = true, nillable = true)
    protected String referencedTRN;
    @XmlElement(required = true, nillable = true)
    protected AddressDTO residentAddress;
    @XmlElement(required = true, nillable = true)
    protected String soleProprietorFlag;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar trnAssignedDate;
    @XmlElement(required = true, nillable = true)
    protected String trnStatus;

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the nameAtBirth property.
     * 
     * @return
     *     possible object is
     *     {@link TaxpayerNameDTO }
     *     
     */
    public TaxpayerNameDTO getNameAtBirth() {
        return nameAtBirth;
    }

    /**
     * Sets the value of the nameAtBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxpayerNameDTO }
     *     
     */
    public void setNameAtBirth(TaxpayerNameDTO value) {
        this.nameAtBirth = value;
    }

    /**
     * Gets the value of the nameChangeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameChangeCode() {
        return nameChangeCode;
    }

    /**
     * Sets the value of the nameChangeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameChangeCode(String value) {
        this.nameChangeCode = value;
    }

    /**
     * Gets the value of the nameChangeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameChangeDesc() {
        return nameChangeDesc;
    }

    /**
     * Sets the value of the nameChangeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameChangeDesc(String value) {
        this.nameChangeDesc = value;
    }

    /**
     * Gets the value of the currentName property.
     * 
     * @return
     *     possible object is
     *     {@link TaxpayerNameDTO }
     *     
     */
    public TaxpayerNameDTO getCurrentName() {
        return currentName;
    }

    /**
     * Sets the value of the currentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxpayerNameDTO }
     *     
     */
    public void setCurrentName(TaxpayerNameDTO value) {
        this.currentName = value;
    }

    /**
     * Gets the value of the trnNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrnNbr() {
        return trnNbr;
    }

    /**
     * Sets the value of the trnNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrnNbr(String value) {
        this.trnNbr = value;
    }

    /**
     * Gets the value of the birthCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCountryCode() {
        return birthCountryCode;
    }

    /**
     * Sets the value of the birthCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCountryCode(String value) {
        this.birthCountryCode = value;
    }

    /**
     * Gets the value of the birthCountryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthCountryName() {
        return birthCountryName;
    }

    /**
     * Sets the value of the birthCountryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthCountryName(String value) {
        this.birthCountryName = value;
    }

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
     * Gets the value of the employerInfo property.
     * 
     * @return
     *     possible object is
     *     {@link EmployerDTO }
     *     
     */
    public EmployerDTO getEmployerInfo() {
        return employerInfo;
    }

    /**
     * Sets the value of the employerInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployerDTO }
     *     
     */
    public void setEmployerInfo(EmployerDTO value) {
        this.employerInfo = value;
    }

    /**
     * Gets the value of the lastUpdateDtime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdateDtime() {
        return lastUpdateDtime;
    }

    /**
     * Sets the value of the lastUpdateDtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdateDtime(XMLGregorianCalendar value) {
        this.lastUpdateDtime = value;
    }

    /**
     * Gets the value of the mailingAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDTO }
     *     
     */
    public AddressDTO getMailingAddress() {
        return mailingAddress;
    }

    /**
     * Sets the value of the mailingAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDTO }
     *     
     */
    public void setMailingAddress(AddressDTO value) {
        this.mailingAddress = value;
    }

    /**
     * Gets the value of the maritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the maritalStatusDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatusDesc() {
        return maritalStatusDesc;
    }

    /**
     * Sets the value of the maritalStatusDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatusDesc(String value) {
        this.maritalStatusDesc = value;
    }

    /**
     * Gets the value of the nationalityCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalityCountryCode() {
        return nationalityCountryCode;
    }

    /**
     * Sets the value of the nationalityCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalityCountryCode(String value) {
        this.nationalityCountryCode = value;
    }

    /**
     * Gets the value of the nationalityCountryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalityCountryName() {
        return nationalityCountryName;
    }

    /**
     * Sets the value of the nationalityCountryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalityCountryName(String value) {
        this.nationalityCountryName = value;
    }

    /**
     * Gets the value of the natureOfBusinessCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfBusinessCode() {
        return natureOfBusinessCode;
    }

    /**
     * Sets the value of the natureOfBusinessCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfBusinessCode(String value) {
        this.natureOfBusinessCode = value;
    }

    /**
     * Gets the value of the natureOfBusinessDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfBusinessDesc() {
        return natureOfBusinessDesc;
    }

    /**
     * Sets the value of the natureOfBusinessDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfBusinessDesc(String value) {
        this.natureOfBusinessDesc = value;
    }

    /**
     * Gets the value of the occupationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationCode() {
        return occupationCode;
    }

    /**
     * Sets the value of the occupationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationCode(String value) {
        this.occupationCode = value;
    }

    /**
     * Gets the value of the occupationDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationDesc() {
        return occupationDesc;
    }

    /**
     * Sets the value of the occupationDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationDesc(String value) {
        this.occupationDesc = value;
    }

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfBirth(String value) {
        this.placeOfBirth = value;
    }

    /**
     * Gets the value of the referencedTRN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencedTRN() {
        return referencedTRN;
    }

    /**
     * Sets the value of the referencedTRN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedTRN(String value) {
        this.referencedTRN = value;
    }

    /**
     * Gets the value of the residentAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDTO }
     *     
     */
    public AddressDTO getResidentAddress() {
        return residentAddress;
    }

    /**
     * Sets the value of the residentAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDTO }
     *     
     */
    public void setResidentAddress(AddressDTO value) {
        this.residentAddress = value;
    }

    /**
     * Gets the value of the soleProprietorFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoleProprietorFlag() {
        return soleProprietorFlag;
    }

    /**
     * Sets the value of the soleProprietorFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoleProprietorFlag(String value) {
        this.soleProprietorFlag = value;
    }

    /**
     * Gets the value of the trnAssignedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTrnAssignedDate() {
        return trnAssignedDate;
    }

    /**
     * Sets the value of the trnAssignedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTrnAssignedDate(XMLGregorianCalendar value) {
        this.trnAssignedDate = value;
    }

    /**
     * Gets the value of the trnStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrnStatus() {
        return trnStatus;
    }

    /**
     * Sets the value of the trnStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrnStatus(String value) {
        this.trnStatus = value;
    }

}