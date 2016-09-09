package org.fsl.converter;

import fsl.ta.toms.roms.bo.PersonBO;


public class PersonBOForRoadCompConverter extends PersonBO
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public PersonBOForRoadCompConverter()
	{
		super();
		
	}

	public PersonBOForRoadCompConverter(PersonBO person)
	{
		super();
		this.setAddressLine1(person.getAddressLine1());
		this.setAddressLine2(person.getAddressLine2());
		this.setAddressLine2NL(person.getAddressLine2NL());
		this.setCurrentUserName(person.getCurrentUserName());
		this.setDlNo(person.getDlNo());
		this.setFirstName(person.getFirstName());
		this.setFullName(person.getFullName());
		this.setLastName(person.getLastName());
		this.setMarkText(person.getMarkText());
		this.setMiddleName(person.getMiddleName());
		this.setOccupation(person.getOccupation());
		this.setParishCode(person.getParishCode());
		this.setParishDesc(person.getParishDesc());
		this.setParishDescription(person.getParishDescription());
		this.setPersonId(person.getPersonId());
		this.setPoBoxNo(person.getPoBoxNo());
		this.setPoLocationName(person.getPoLocationName());
		this.setStreetName(person.getStreetName());
		this.setStreetNo(person.getStreetNo());
		this.setTelephoneCell(person.getTelephoneCell());
		this.setTelephoneHome(person.getTelephoneHome());
		this.setTelephoneWork(person.getTelephoneWork());
		this.setTrnNbr(person.getTrnNbr());
	}


	@Override
	public boolean equals(Object o)
	{
		if(o != null && this != null && ((PersonBO)o).getPersonId() != null && this.getPersonId() != null)
			return ((PersonBO)o).getPersonId().compareTo(this.getPersonId()) == 0;
		else
			return false;
	}
	
	
	
}
