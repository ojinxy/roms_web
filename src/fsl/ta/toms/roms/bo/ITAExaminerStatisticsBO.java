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
public class ITAExaminerStatisticsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String fullName;
	
	String attended;
	
	String idNumber;

	
	
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

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

	public ITAExaminerStatisticsBO(String fullName, String attended, String idNumber) {
		super();
		this.fullName = fullName;
		this.attended = attended;
		this.idNumber = idNumber;
	}

	public ITAExaminerStatisticsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
