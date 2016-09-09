
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 22, 2014
 */
@Entity
@Table(name="roms_lmis_licence_fitness_view")
@IdClass(LMIS_licenceFitnessViewKEY.class)
public class LMIS_licenceFitnessView implements Serializable 
{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LMIS_licenceFitnessView()
	{
		super();
	}
	
	@Id
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Id
	@Column(name="fitness_no")
	String fitnessNo;
	
	
	@Column(name="depot_desc")
	String depotDesc;

	@Column(name="fitness_iss_date")
	Date fitnessIssueDate;
	
	@Column(name="fitness_exp_date")
	Date fitnessExpiryDate;
	
	public Integer getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getFitnessNo() {
		return fitnessNo;
	}

	public void setFitnessNo(String fitnessNo) {
		this.fitnessNo = fitnessNo;
	}

	public String getDepotDesc() {
		return depotDesc;
	}

	public void setDepotDesc(String depotDesc) {
		this.depotDesc = depotDesc;
	}

	public Date getFitnessIssueDate() {
		return fitnessIssueDate;
	}

	public void setFitnessIssueDate(Date fitnessIssueDate) {
		this.fitnessIssueDate = fitnessIssueDate;
	}

	public Date getFitnessExpiryDate() {
		return fitnessExpiryDate;
	}

	public void setFitnessExpiryDate(Date fitnessExpiryDate) {
		this.fitnessExpiryDate = fitnessExpiryDate;
	}

	

	
	
}
