
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;


public class LMIS_licenceInsuranceViewKEY implements Serializable
{

	private static final long serialVersionUID = 1L;

	
	
	
	@Column(name="licence_no")
	Integer licenceNo;
	
	
	@Column(name="insurance_comp")
	String insuranceCompany;
	
	
	@Column(name="ins_issue_date")
	Date insIssueDate;
	
	
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
