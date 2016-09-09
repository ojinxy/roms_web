
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
@Table(name="roms_lmis_licence_ins_view")
@IdClass(LMIS_licenceInsuranceViewKEY.class)
public class LMIS_licenceInsuranceView implements Serializable 
{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LMIS_licenceInsuranceView()
	{
		super();
	}
	
	@Id
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Id
	@Column(name="insurance_comp")
	String insuranceCompany;
	
	@Id
	@Column(name="ins_issue_date")
	Date insIssueDate;
	
	@Id
	@Column(name="ins_exp_date")
	Date insExpDate;
	
	public Integer getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public Date getInsIssueDate() {
		return insIssueDate;
	}

	public void setInsIssueDate(Date insIssueDate) {
		this.insIssueDate = insIssueDate;
	}

	public Date getInsExpDate() {
		return insExpDate;
	}

	public void setInsExpDate(Date insExpDate) {
		this.insExpDate = insExpDate;
	}

	
	

	
	
}
