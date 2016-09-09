package org.fsl.roms.view;

import java.io.Serializable;

import org.fsl.roms.constants.Constants;

public class OperationStrategyRuleView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7529741773344104708L;
	private int minimumStaffReq;
	private int minimumITAReq;
	private int minimumPoliceReq;
	private int minimumJPReq;
	private int minimumVehReq;
	
	private int maximumStaffReq;
	private int maximumITAReq;
	private int maximumPoliceReq;
	private int maximumJPReq;
	private int maximumVehReq;
	
	public OperationStrategyRuleView() {
		super();
		this.minimumStaffReq = 0;
		this.minimumITAReq = 0;
		this.minimumPoliceReq = 0;
		this.minimumJPReq = 0;
		this.minimumVehReq = 0;
		this.maximumStaffReq = Constants.maxStrategyRuleValue;
		this.maximumITAReq = Constants.maxStrategyRuleValue;
		this.maximumPoliceReq = Constants.maxStrategyRuleValue;
		this.maximumJPReq = Constants.maxStrategyRuleValue;
		this.maximumVehReq = Constants.maxStrategyRuleValue;
	}

	public int getMinimumStaffReq() {
		return minimumStaffReq;
	}

	public void setMinimumStaffReq(int minimumStaffReq) {
		this.minimumStaffReq = minimumStaffReq;
	}

	public int getMinimumITAReq() {
		return minimumITAReq;
	}

	public void setMinimumITAReq(int minimumITAReq) {
		this.minimumITAReq = minimumITAReq;
	}

	public int getMinimumPoliceReq() {
		return minimumPoliceReq;
	}

	public void setMinimumPoliceReq(int minimumPoliceReq) {
		this.minimumPoliceReq = minimumPoliceReq;
	}

	public int getMinimumJPReq() {
		return minimumJPReq;
	}

	public void setMinimumJPReq(int minimumJPReq) {
		this.minimumJPReq = minimumJPReq;
	}

	public int getMinimumVehReq() {
		return minimumVehReq;
	}

	public void setMinimumVehReq(int minimumVehReq) {
		this.minimumVehReq = minimumVehReq;
	}

	public int getMaximumStaffReq() {
		return maximumStaffReq;
	}

	public void setMaximumStaffReq(int maximumStaffReq) {
		this.maximumStaffReq = maximumStaffReq;
	}

	public int getMaximumITAReq() {
		return maximumITAReq;
	}

	public void setMaximumITAReq(int maximumITAReq) {
		this.maximumITAReq = maximumITAReq;
	}

	public int getMaximumPoliceReq() {
		return maximumPoliceReq;
	}

	public void setMaximumPoliceReq(int maximumPoliceReq) {
		this.maximumPoliceReq = maximumPoliceReq;
	}

	public int getMaximumJPReq() {
		return maximumJPReq;
	}

	public void setMaximumJPReq(int maximumJPReq) {
		this.maximumJPReq = maximumJPReq;
	}

	public int getMaximumVehReq() {
		return maximumVehReq;
	}

	public void setMaximumVehReq(int maximumVehReq) {
		this.maximumVehReq = maximumVehReq;
	}
	
	
	

}
