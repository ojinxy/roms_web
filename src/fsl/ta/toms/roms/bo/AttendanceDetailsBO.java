package fsl.ta.toms.roms.bo;

import java.io.Serializable;

public class AttendanceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064834889047958126L;

	private Boolean attended;
	private String comment;
	
	public AttendanceDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AttendanceDetailsBO(Boolean attended, String comment) {
		super();
		this.attended = attended;
		this.comment = comment;
	}

	public Boolean getAttended() {
		return attended;
	}

	public void setAttended(Boolean attended) {
		this.attended = attended;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
