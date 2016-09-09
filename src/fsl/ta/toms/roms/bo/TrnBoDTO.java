package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

public class TrnBoDTO   implements Serializable
{

    protected String formattedName;
    protected boolean businessTrn;
    protected boolean individualTrn;
    protected String address;
    protected Integer badChequeId;
    protected String branchRefNbr;
    protected String firstName;
    protected String flgStatus;
    protected String flgTrnType;
    protected String lastName;
    protected String middleName;
    protected String nbrBranch;
    protected String nbrReferenceTrn;
    protected String nbrTrn;
    protected Integer registered;
    protected String rpdAlert;
    protected Integer transId;
    protected Date trnEntryDate;
    protected String trnEntryStatus;
    protected Date lastUpdate;
    protected String addrCountryCode;
    protected String addrMarkText;
    protected String addrParishCode;
    protected String addrPoBoxNo;
    protected String addrPoLocName;
    protected String addrStreetName;
    protected String addrStreetNo;
    protected String maritalStat;
    protected String addrCountryDesc;
    protected String addrParishDesc;
    protected String birthFirstName;
    protected String birthLastName;
    protected String birthMiddleName;
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
     *     {@link Date }
     *     
     */
    public Date getTrnEntryDate() {
        return trnEntryDate;
    }

    /**
     * Sets the value of the trnEntryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setTrnEntryDate(Date value) {
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
     *     {@link Date}
     *     
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the value of the lastUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setLastUpdate(Date value) {
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
