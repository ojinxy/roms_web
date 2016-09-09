/**
 * Created By: oanguin
 * Date: Oct 2, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;

/**
 * @author oanguin Created Date: Oct 2, 2013
 */
@Embeddable
public class AddressDO implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public AddressDO() {
		super();
		//this.parish = new ParishDO();
	}

	public AddressDO(String markText, String streetNo, String streetName, String poLocationName, String poBoxNo, ParishDO parish) {
		super();
		this.markText = markText;
		this.streetNo = streetNo;
		this.streetName = streetName;
		this.poLocationName = poLocationName;
		this.poBoxNo = poBoxNo;
		this.parish = parish;
	}

	@Column(name = "mark_text")
	String		markText;

	@Column(name = "street_no")
	String		streetNo;

	@Column(name = "street_name")
	String		streetName;

	@Column(name = "po_loc_name")
	String		poLocationName;

	@Column(name = "po_box_no")
	String		poBoxNo;

	@ManyToOne
	@JoinColumn(name = "parish_code")
	ParishDO	parish;

	public String getMarkText() {
		return markText;
	}

	public void setMarkText(String markText) {
		this.markText = markText;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPoLocationName() {
		return poLocationName;
	}

	public void setPoLocationName(String poLocationName) {
		this.poLocationName = poLocationName;
	}

	public String getPoBoxNo() {
		return poBoxNo;
	}

	public void setPoBoxNo(String poBoxNo) {
		this.poBoxNo = poBoxNo;
	}

	public ParishDO getParish() {
		/*if(parish == null){
			parish = new ParishDO();
		}*/
		return parish;
	}

	public void setParish(ParishDO parish) {
		this.parish = parish;
	}

	public String getAddressLine1() {

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

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);
	}

	public String getAddressLine2() {
		boolean kingston = false;
		String address = "";

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
				address = address + ",";
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

		if (parish != null) {
			if (parish.getDescription() != null && !"".equals(parish.getDescription().trim())) {
				String par = parish.getDescription().toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.getDescription().trim();
						} else {
							address = address + ", " + parish.getDescription().trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.getDescription().trim();
					} else {
						address = address + ", " + parish.getDescription().trim();
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
				//set the new line 
				if (StringUtils.isBlank(poBoxNo)) {
					if (po.contains("KINGSTON")) {
						kingston = true;
						// if kingston is in the name then put it
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address +'\n' + poLocationName.trim();
						}
					} else {
						if ("".equals(address)) {
							address = poLocationName.trim();
						} else {
							address = address + '\n' + poLocationName.trim();
						}
					}
				}else{
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

		if (parish != null) {
			if (parish.getDescription() != null && !"".equals(parish.getDescription().trim())) {
				String par = parish.getDescription().toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.getDescription().trim();
						} else {
							address = address + '\n' + parish.getDescription().trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.getDescription().trim();
					} else {
						address = address + '\n' + parish.getDescription().trim();
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

		if (parish != null) {
			if (parish.getDescription() != null && !"".equals(parish.getDescription().trim())) {
				String par = parish.getDescription().toUpperCase();
				if (par.contains("KINGSTON")) {
					// if kingston was not put before then put it
					// set variable to true
					if (kingston == false) {
						// kingston = true;
						if ("".equals(address)) {
							address = parish.getDescription().trim();
						} else {
							address = address + ", " + parish.getDescription().trim();
						}
					}

				} else {
					if ("".equals(address)) {
						address = parish.getDescription().trim();
					} else {
						address = address + ", " + parish.getDescription().trim();
					}
				}
			}
		}

		return WordUtils.capitalize(address.toLowerCase(), Constants.STRING_DELIM_ARRAY);

	}

}
