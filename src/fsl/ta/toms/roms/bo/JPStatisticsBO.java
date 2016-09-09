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
public class JPStatisticsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String fullName;
	String regNumber;
	Integer countSummonsSigned;
	String attended;
	
	
	public String getAttended() {
		return attended;
	}
	public void setAttended(String attended) {
		this.attended = attended;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	public Integer getCountSummonsSigned() {
		return countSummonsSigned;
	}
	public void setCountSummonsSigned(Integer countWarningNoticesSigned) {
		this.countSummonsSigned = countWarningNoticesSigned;
	}


	public JPStatisticsBO(String fullName, String regNumber,
			Integer countSummonsSigned, String attended) {
		super();
		this.fullName = fullName;
		this.regNumber = regNumber;
		this.countSummonsSigned = countSummonsSigned;
		this.attended = attended;
	}
	public JPStatisticsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
