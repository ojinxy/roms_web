/**
 * 
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;

/**
 * 
 * @author rbrooks Created Date: Dec 20, 2013
 */
public class AddressBO implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7649161736620398892L;
	String						mark;
	String						streetNumber;
	String						streetName;
	String						poLocation;
	String						poBoxNumber;
	String						parish_code;
	String						parish;
	String						city;
	String						country;

	public AddressBO(AddressDO address) {
		super();
		this.mark = address.getMarkText();
		this.streetNumber = address.getStreetNo();
		this.streetName = address.getStreetName();
		this.poLocation = address.getPoLocationName();
		this.poBoxNumber = address.getPoBoxNo();

		if (address.getParish() != null) {
			this.parish_code = address.getParish().getParishCode();
			this.parish = address.getParish().getDescription();
		}
	}

	public AddressBO() {
		super();
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String markText) {
		this.mark = markText;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNo) {
		this.streetNumber = streetNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPoLocation() {
		return poLocation;
	}

	public void setPoLocation(String poLocationName) {
		this.poLocation = poLocationName;
	}

	public String getPoBoxNumber() {
		return poBoxNumber;
	}

	public void setPoBoxNumber(String poBoxNo) {
		this.poBoxNumber = poBoxNo;
	}

	public String getParish_code() {
		return parish_code;
	}

	public void setParish_code(String parish_code) {
		this.parish_code = parish_code;
	}

	public String getParish() {
		return parish;
	}

	public void setParish(String parishDesc) {
		this.parish = parishDesc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddressLine1() {

		String address = "";
		if (mark != null && !"".equals(mark)) {
			address = mark.trim();
		}
		if (streetNumber != null && !"".equals(streetNumber.trim())) {
			if ("".equals(address)) {
				address = streetNumber.trim();
			} else {
				address = address + ", " + streetNumber.trim();
			}
		}
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

		if (poBoxNumber != null && !"".equals(poBoxNumber.trim())) {
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNumber.trim();
			} else {
				address = address + ", P.O. Box " + poBoxNumber.trim();
			}
		}

		if (poLocation != null && !"".equals(poLocation.trim())) {
			String po = poLocation.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocation) && StringUtils.isNotBlank(address)) {
				address = address + ",";
			}

			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocation.trim();
				} else {
					address = address + " " + poLocation.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocation.trim();
				} else {
					address = address + " " + poLocation.trim();
				}
			}
		}

		if (parish != null) {
			if (parish != null && !"".equals(parish.trim())) {
				String par = parish.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.trim();
						} else {
							address = address + ", " + parish.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.trim();
					} else {
						address = address + ", " + parish.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}

	public String getAddressLineWithNewLine() {
		boolean kingston = false;
		String address = "";

		if (mark != null && !"".equals(mark)) {
			address = mark.trim();
		}
		if (streetNumber != null && !"".equals(streetNumber.trim())) {
			if ("".equals(address)) {
				address = streetNumber.trim();
			} else {
				address = address + '\n' + streetNumber.trim();
			}
		}
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				if (StringUtils.isBlank(streetNumber)) {
					address = address + '\n' + streetName.trim() + "";
				} else {
					address = address + " " + streetName.trim() + "";
				}
			}
		}

		if (poBoxNumber != null && !"".equals(poBoxNumber.trim())) {
			if ("".equals(address)) {
				address = "P.O. Box " + poBoxNumber.trim();
			} else {
				address = address + '\n' + "P.O. Box " + poBoxNumber.trim();
			}
		}

		if (poLocation != null && !"".equals(poLocation.trim())) {
			String po = poLocation.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocation) && StringUtils.isNotBlank(address)) {
				address = address + '\n';
			} else

			{
				// set the new line
				if (StringUtils.isBlank(poBoxNumber)) {
					if (po.contains("KINGSTON")) {
						kingston = true;
						// if kingston is in the name then put it
						if ("".equals(address)) {
							address = poLocation.trim();
						} else {
							address = address + '\n' + poLocation.trim();
						}
					} else {
						if ("".equals(address)) {
							address = poLocation.trim();
						} else {
							address = address + '\n' + poLocation.trim();
						}
					}
				} else {
					if (po.contains("KINGSTON")) {
						kingston = true;
						// if kingston is in the name then put it
						if ("".equals(address)) {
							address = poLocation.trim();
						} else {
							address = address + " " + poLocation.trim();
						}
					} else {
						if ("".equals(address)) {
							address = poLocation.trim();
						} else {
							address = address + " " + poLocation.trim();
						}
					}
				}
				// end set new line

			}
		}

		if (parish != null) {
			if (parish != null && !"".equals(parish.trim())) {
				String par = parish.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.trim();
						} else {
							address = address + '\n' + parish.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.trim();
					} else {
						address = address + '\n' + parish.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}

	public String getAddressLineWithCommas() {
		boolean kingston = false;
		String address = "";

		if (mark != null && !"".equals(mark)) {
			address = mark.trim();
		}
		if (streetNumber != null && !"".equals(streetNumber.trim())) {
			if ("".equals(address)) {
				address = streetNumber.trim();
			} else {
				address = address + ", " + streetNumber.trim();
			}
		}
		if (streetName != null && !"".equals(streetName.trim())) {
			if ("".equals(address)) {
				address = streetName.trim();
			} else {
				address = address + " " + streetName.trim() + "";
			}
		}

		if (poBoxNumber != null && !"".equals(poBoxNumber.trim())) {
			if ("".equals(address)) {
				address = " P.O. Box " + poBoxNumber.trim();
			} else {
				address = address + ", P.O. Box " + poBoxNumber.trim();
			}
		}

		if (poLocation != null && !"".equals(poLocation.trim())) {
			String po = poLocation.toUpperCase();
			// if po box is not there then put a comma before the poLocName
			if (StringUtils.isBlank(poLocation) && StringUtils.isNotBlank(address)) {
				address = address + ',';
			}

			if (po.contains("KINGSTON")) {
				kingston = true;
				// if kingston is in the name then put it
				if ("".equals(address)) {
					address = poLocation.trim();
				} else {
					address = address + " " + poLocation.trim();
				}
			} else {
				if ("".equals(address)) {
					address = poLocation.trim();
				} else {
					address = address + " " + poLocation.trim();
				}
			}
		}

		if (parish != null) {
			if (parish != null && !"".equals(parish.trim())) {
				String par = parish.toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.trim();
						} else {
							address = address + ',' + parish.trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.trim();
					} else {
						address = address + ',' + parish.trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}
}
