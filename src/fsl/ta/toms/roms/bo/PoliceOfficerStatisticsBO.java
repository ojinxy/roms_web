/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class PoliceOfficerStatisticsBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String fullName;
	
	String attended;

	Integer policeOfficerCompNo;
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAttended() {
		return attended;
	}

	public void setAttended(String attended) {
		this.attended = attended;
	}

	
	public PoliceOfficerStatisticsBO(String fullName, String attended, Integer policeOfficerCompNo) {
		super();
		this.fullName = fullName;
		this.attended = attended;
		this.policeOfficerCompNo = policeOfficerCompNo;
	}

	public PoliceOfficerStatisticsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
