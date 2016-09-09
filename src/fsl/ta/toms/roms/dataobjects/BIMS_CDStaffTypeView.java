package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_bims_cd_staff_type_view")
public class BIMS_CDStaffTypeView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3243964921137421388L;

	public BIMS_CDStaffTypeView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="staff_type_code")
	String staffTypeCode;
	
	@Column(name="staff_type_desc")
	String staffTypeDesc;

	public String getStaffTypeCode() {
		return staffTypeCode;
	}

	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}

	public String getStaffTypeDesc() {
		return staffTypeDesc;
	}

	public void setStaffTypeDesc(String staffTypeDesc) {
		this.staffTypeDesc = staffTypeDesc;
	}
	
	


}
