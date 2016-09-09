package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.util.NameUtil;


public class PersonBO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1248516354084281711L;
	Integer personId;
	String trnNbr, firstName , middleName,lastName, fullName;
	String telephoneHome,telephoneCell,telephoneWork;
	String markText, streetNo,streetName,poLocationName,poBoxNo;
	String parishCode, parishDesc, occupation, parishDescription;
	String currentUserName,addressLine1,addressLine2,addressLine2NL,dlNo;
	
	
	
	
	public PersonBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public PersonBO(String trnNbr, String firstName, String middleName,
			String lastName, String telephoneHome,
			String telephoneCell, String telephoneWork, String markText,
			String streetNo, String streetName, String poLocationName,
			String poBoxNo, String parishCode, String currentUserName) {
		super();
		this.trnNbr = trnNbr;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		
		NameUtil util = new NameUtil();
		this.fullName= util.getFullName(firstName, lastName);
		
		this.telephoneHome = telephoneHome;
		this.telephoneCell = telephoneCell;
		this.telephoneWork = telephoneWork;
		this.markText = markText;
		this.streetNo = streetNo;
		this.streetName = streetName;
		this.poLocationName = poLocationName;
		this.poBoxNo = poBoxNo;
		this.parishCode = parishCode;
		this.currentUserName = currentUserName;
	}
	
	@SuppressWarnings("static-access")
	public PersonBO(DriverLicenceDetails dlDetails,String currentUserName) 
	{
		super();
		this.trnNbr = dlDetails.getTrnNo();
		this.firstName = dlDetails.getFirstName();
		this.middleName = dlDetails.getLastName();
		this.lastName = dlDetails.getLastName();
		
		NameUtil util = new NameUtil();
		this.fullName= util.getFullName(dlDetails.getFirstName(), dlDetails.getLastName());
		

		this.markText = dlDetails.getAddress().getMark();
		this.streetNo = dlDetails.getAddress().getStreetNumber();
		this.streetName = dlDetails.getAddress().getStreetName();
		this.poLocationName = dlDetails.getAddress().getPoLocation();
		this.poBoxNo = dlDetails.getAddress().getPoBoxNumber();
		
		this.dlNo = dlDetails.getDriversLicenceNo();
		
		this.currentUserName = currentUserName;
	}


	
	public PersonBO(String trnNbr, String firstName, String middleName,
			String lastName, String telephoneHome,
			String telephoneCell, String telephoneWork, String markText,
			String streetNo, String streetName, String poLocationName,
			String poBoxNo, String parishCode, String currentUserName,String parishDesc) {
		super();
		this.trnNbr = trnNbr;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		
		NameUtil util = new NameUtil();
		this.fullName= util.getFullName(firstName, lastName);
		
		this.telephoneHome = telephoneHome;
		this.telephoneCell = telephoneCell;
		this.telephoneWork = telephoneWork;
		this.markText = markText;
		this.streetNo = streetNo;
		this.streetName = streetName;
		this.poLocationName = poLocationName;
		this.poBoxNo = poBoxNo;
		this.parishCode = parishCode;
		this.currentUserName = currentUserName;
		this.parishDescription = parishDesc;
		
		this.addressLine1 = this.getAddressLine1();
		
		this.addressLine2 = this.getAddressLine2();
		
		this.addressLine2NL = this.getAddressLineWithNewLine();
	}

	public PersonBO(PersonDO personDo) {
	
		if(personDo != null)
		{
			this.setPersonId(personDo.getPersonId());
			this.setTrnNbr(personDo.getTrnNbr());
			this.setFirstName(personDo.getFirstName());
			this.setLastName(personDo.getLastName());
			this.setMiddleName(personDo.getMiddleName());
		
			NameUtil util = new NameUtil();
			this.fullName= util.getFullName(firstName, lastName);
		
			if(personDo.getAddress()!=null){
				this.setMarkText(personDo.getAddress().getMarkText());
				
				if(personDo.getAddress().getParish()!=null){
					this.setParishCode(personDo.getAddress().getParish().getParishCode());
					this.parishDesc = personDo.getAddress().getParish().getDescription();
					this.parishDescription = personDo.getAddress().getParish().getDescription();
					
				}
				
				this.setPoBoxNo(personDo.getAddress().getPoBoxNo());
				this.setPoLocationName(personDo.getAddress().getPoLocationName());
				this.setStreetName(personDo.getAddress().getStreetName());
				this.setStreetNo(personDo.getAddress().getStreetNo());
				
				this.addressLine1 = personDo.getAddress().getAddressLine1();
				this.addressLine2 = personDo.getAddress().getAddressLine2();
				this.addressLine2NL = personDo.getAddress().getAddressLineWithNewLine();
				
			}
		
			this.setTelephoneCell(personDo.getTelephoneCell());
			this.setTelephoneHome(personDo.getTelephoneHome());
			this.setTelephoneWork(personDo.getTelephoneWork());
			this.setOccupation(personDo.getOccupation());
			this.setDlNo(personDo.getDlNo());
		}
		
	}
	
	public PersonDO generatePersonDO(ParishDO parishDO)
	{
		PersonDO personDO = new PersonDO();
//			parishDO.setParishCode(personBO.getParishCode());
			
		personDO.setPersonId(this.getPersonId());
		personDO.setTrnNbr(this.getTrnNbr());
		personDO.setFirstName(this.getFirstName());
		personDO.setLastName(this.getLastName());
		personDO.setMiddleName(this.getMiddleName());
		personDO.getAddress().setMarkText(this.getMarkText());
		personDO.getAddress().setParish(parishDO);
			//this.setParish(parishDO);  //could have used getParishReference from ParishDAOImpl to get DO
		personDO.getAddress().setPoBoxNo(this.getPoBoxNo());
		personDO.getAddress().setPoLocationName(this.getPoLocationName());
		personDO.getAddress().setStreetName(this.getStreetName());
		personDO.getAddress().setStreetNo(this.getStreetNo());
		personDO.setTelephoneCell(this.getTelephoneCell());
		personDO.setTelephoneHome(this.getTelephoneHome());
		personDO.setTelephoneWork(this.getTelephoneWork());
		personDO.setOccupation(this.getOccupation());
		personDO.setDlNo(this.getDlNo());
		return personDO;
	}
	
	
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public String getTrnNbr() {
		return trnNbr;
	}
	public void setTrnNbr(String trnNbr) {
		if(StringUtils.isNotBlank(trnNbr))
			this.trnNbr = trnNbr.trim();
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		if(StringUtils.isNotBlank(firstName))
			this.firstName = firstName.trim();
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		if(StringUtils.isNotBlank(middleName))
			this.middleName = middleName.trim();
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		if(StringUtils.isNotBlank(lastName))
			this.lastName = lastName.trim();
	}
	public String getTelephoneHome() {
		return telephoneHome;
	}
	public void setTelephoneHome(String telephoneHome) {
		if(StringUtils.isNotBlank(telephoneHome))
			this.telephoneHome = telephoneHome.trim();
	}
	public String getTelephoneCell() {
		return telephoneCell;
	}
	public void setTelephoneCell(String telephoneCell) {
		if(StringUtils.isNotBlank(telephoneCell))
			this.telephoneCell = telephoneCell.trim();
	}
	public String getTelephoneWork() {
		return telephoneWork;
	}
	public void setTelephoneWork(String telephoneWork) {
		if(StringUtils.isNotBlank(telephoneWork))
			this.telephoneWork = telephoneWork.trim();
	}
	public String getMarkText() {
		return markText;
	}
	public void setMarkText(String markText) {
		if(StringUtils.isNotBlank(markText))
			this.markText = markText.trim();
	}
	public String getStreetNo() {
		return streetNo;
	}
	public void setStreetNo(String streetNo) {
		if(StringUtils.isNotBlank(streetNo))
			this.streetNo = streetNo.trim();
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		if(StringUtils.isNotBlank(streetName))
			this.streetName = streetName.trim();
	}
	public String getPoLocationName() {
		return poLocationName;
	}
	public void setPoLocationName(String poLocationName) {
		if(StringUtils.isNotBlank(poLocationName))
			this.poLocationName = poLocationName.trim();
	}
	public String getPoBoxNo() {
		return poBoxNo;
	}
	public void setPoBoxNo(String poBoxNo) {
		if(StringUtils.isNotBlank(poBoxNo))
			this.poBoxNo = poBoxNo.trim();
	}
	
	/**
	 * @return the parishDesc
	 */
	public String getParishDesc() {
		return parishDesc;
	}


	/**
	 * @param parishDesc the parishDesc to set
	 */
	public void setParishDesc(String parishDesc) {
		this.parishDesc = parishDesc;
	}


	public String getParishCode() {
		return parishCode;
	}


	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}


	/**
	 * @return the fullName
	 */
	public String getFullName() {
		
		if(fullName == null || fullName.isEmpty()){		
			this.fullName= NameUtil.getFullName(this.firstName, this.lastName);
		}
		return fullName;
	}


	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}


	/**
	 * @param occupation the occupation to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}


	////
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		if(StringUtils.isNotBlank(currentUserName))
			this.currentUserName = currentUserName.trim();
	}
	
	

	public String getParishDescription() {
		return parishDescription;
	}


	public void setParishDescription(String parishDescription) {
		this.parishDescription = parishDescription;
	}


	public String getAddressLine2NL() {
		return addressLine2NL;
	}


	public void setAddressLine2NL(String addressLine2NL) {
		this.addressLine2NL = addressLine2NL;
	}


	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}


	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	
	

	public String getDlNo()
	{
		return dlNo;
	}


	public void setDlNo(String dlNo)
	{
		this.dlNo = dlNo;
	}


	public void trimPersonDetails(){
		
		if(StringUtils.isNotBlank(this.trnNbr)){
			setTrnNbr(this.trnNbr.trim());
		}
		
		if(StringUtils.isNotBlank(this.firstName)){
			setFirstName(this.firstName.trim());
		}
		
		if(StringUtils.isNotBlank(this.middleName)){
			setMiddleName(this.middleName.trim());
		}
		
		if(StringUtils.isNotBlank(this.lastName)){
			setLastName(this.lastName.trim());
		}
		
		if(StringUtils.isNotBlank(this.markText)){
			setMarkText(this.markText.trim());
		}
	
		if(StringUtils.isNotBlank(this.streetNo)){
			setStreetNo(this.streetNo.trim());
		}

		if(StringUtils.isNotBlank(this.streetName)){
			setStreetName(this.streetName.trim());
		}

		if(StringUtils.isNotBlank(this.poLocationName)){
			setPoLocationName(this.poLocationName.trim());
		}
		
		if(StringUtils.isNotBlank(this.poBoxNo)){
			setPoBoxNo(this.poBoxNo.trim());
		}
		
		if(StringUtils.isNotBlank(this.parishCode)){
			setParishCode(this.parishCode.trim());
		}
		
		if(StringUtils.isNotBlank(this.telephoneHome)){
			setTelephoneHome(this.telephoneHome.trim());
		}
		
		if(StringUtils.isNotBlank(this.telephoneCell)){
			setTelephoneCell(this.telephoneCell.trim());
		}
	
		if(StringUtils.isNotBlank(this.telephoneWork)){
			setTelephoneWork(this.telephoneWork.trim());
		}
		
		if(StringUtils.isNotBlank(this.occupation)){
			setOccupation(this.occupation.trim());
		}
	}

public String getAddressLine1() {
		
		String address = "";
		/*if (addrMark != null && !"".equals(addrMark)) {
			address = addrMark.trim();
		}*/
	/*	if (addrStreetNo != null && !"".equals(addrStreetNo.trim())) {
			if ("".equals(address)) {
				address = addrStreetNo.trim();
			} else {
				address = address + ", " + addrStreetNo.trim();
			}
		}*/
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				address = address + " " + streetName.trim() + "";
			}
		}			
		
		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);
	}

	public String getAddressLine2() {
		boolean kingston = false;
		String address = "";
					
		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {				
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNo.trim();
			}else{
				address = address + ", P.O. Box "  + poBoxNo.trim();
			}
		}
			
		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			//if po box is not there then put a comma before the poLocName 
			if(StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address) ){
				address = address + ",";
			}
				
			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocationName.trim() ;
				} else {
					address = address + " " + poLocationName.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocationName.trim() ;
				} else {
					address = address + " " + poLocationName.trim() ;
				}
			}
		}
				
		

		if (parishDescription != null) {
			if (parishDescription != null && !"".equals(parishDescription.trim())) {
				String par = parishDescription.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						//kingston = true;
						if ("".equals(address)) {
							address = parishDescription.trim();
						} else {
							address = address+ ", " +parishDescription.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address =  parishDescription.trim();
					} else {
						address = address + ", " + parishDescription.trim();
					}
				}
			}
		} 
	

		

		
		
		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);
		
	}
	
	
	public String getAddressLineWithNewLine() {
		boolean kingston = false;
		String address = "";

		if (markText != null && !"".equals(markText)) {
			address = markText.trim();
		}
		if (streetNo != null && !"".equals(streetNo.trim())) {
			if ("".equals(address)) {
				address = streetNo.trim();
			} else {
				address = address + '\n' + streetNo.trim();
			}
		}
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				if (StringUtils.isBlank(streetNo)) {
					address = address + '\n' + streetName.trim() + "";
				} else {
					address = address + " " + streetName.trim() + "";
				}
			}
		}

		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {
			if ("".equals(address)) {
				address = "P.O. Box " + poBoxNo.trim();
			} else {
				address = address + '\n' + "P.O. Box " + poBoxNo.trim();
			}
		}

		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address)) {
				address = address + '\n';
			} else

			{
				// set the new line
				if (StringUtils.isBlank(poBoxNo)) {
					if (po.contains("KINGSTON")) {
						kingston = true;
						// if kingston is in the name then put it
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address + '\n' + poLocationName.trim();
						}
					} else {
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address + '\n' + poLocationName.trim();
						}
					}
				} else {
					if (po.contains("KINGSTON")) {
						kingston = true;
						// if kingston is in the name then put it
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address + " " + poLocationName.trim();
						}
					} else {
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address + " " + poLocationName.trim();
						}
					}
				}
				// end set new line

			}
		}

		if (parishDesc != null) {
			if (parishDesc != null && !"".equals(parishDesc.trim())) {
				String par = parishDesc.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parishDesc.trim();
						} else {
							address = address + '\n' + parishDesc.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parishDesc.trim();
					} else {
						address = address + '\n' + parishDesc.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}

	public String getAddressLineWithCommas() {
		boolean kingston = false;
		String address = "";

		if (markText != null && !"".equals(markText)) {
			address = markText.trim();
		}
		if (streetNo != null && !"".equals(streetNo.trim())) {
			if ("".equals(address)) {
				address = streetNo.trim();
			} else {
				address = address + ", " + streetNo.trim();
			}
		}
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				address = address + " " + streetName.trim() + "";
			}
		}

		if (poBoxNo != null && !"".equals(poBoxNo.trim())) {
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNo.trim();
			} else {
				address = address + ", P.O. Box " + poBoxNo.trim();
			}
		}

		if (poLocationName != null && !"".equals(poLocationName.trim())) {
			String po = poLocationName.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocationName) && StringUtils.isNotBlank(address)) {
				address = address + ',';
			}

			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocationName.trim();
				} else {
					address = address + " " + poLocationName.trim();
				}
			}
		}

		if (parishDesc != null) {
			if (parishDesc != null && !"".equals(parishDesc.trim())) {
				String par = parishDesc.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parishDesc.trim();
						} else {
							address = address + ',' + parishDesc.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parishDesc.trim();
					} else {
						address = address + ',' + parishDesc.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}
}
