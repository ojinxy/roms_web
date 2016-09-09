package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="department_view")
public class LMISDepartmentViewBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4630886943650648464L;

	public LMISDepartmentViewBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="dept_code")
	String departmentCode;
	
	@Column(name="dept_desc")
	String departmentDesc;
	
	@Column(name="status_code")
	String statusCode;

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentDesc() {
		return departmentDesc;
	}

	public void setDepartmentDesc(String departmentDesc) {
		this.departmentDesc = departmentDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
