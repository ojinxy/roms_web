package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Entity;


public class RoadLicenceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791454610930414679L;

	String info_key;
	
	Integer licence_no;
	String lic_desc;
	Date issue_date;
	Date expiry_date;
	String status_desc;
	String status_code;
	String route_desc;
	String veh_make_desc;
	String model_desc;
	Integer model_year;
	String route_start_txt;
	String route_end_txt;
	Integer pas_seating;
	Integer control_no;
	
	
		
	//owners
	
	String first_name;
	String last_name;
	Integer trn;
	String owner_type;
	Integer cust_id;
	
	String street_no;
	String street_name;
	String mark;
	String po_box;
	String post_office;
	String parish_desc;
	String home_phone_no;
	String work_phone_no;
	String mobile_phone_no;
	
	
	//Driver and Conductor Badge Details

	Integer badge_trn;
	String badge_first_name, badge_last_name, badge_type;
	
	//Fitness Details
	String fitness_no, depot_desc;
	Date fitness_iss_date, fitness_exp_date;
		
	//Insurance Details
	String ins_comp_name;
	Date ins_iss_date, ins_exp_date;
	
	
	Integer appl_no;
	Date appl_date;
	String app_lic_desc, state_desc;
	
	public Integer getLicence_no() {
		return licence_no;
	}
	public void setLicence_no(Integer licence_no) {
		this.licence_no = licence_no;
	}
	public String getLic_desc() {
		return lic_desc;
	}
	public void setLic_desc(String lic_desc) {
		this.lic_desc = lic_desc;
	}
	public Date getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(Date issue_date) {
		this.issue_date = issue_date;
	}
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getRoute_desc() {
		return route_desc;
	}
	public void setRoute_desc(String route_desc) {
		this.route_desc = route_desc;
	}
	public String getVeh_make_desc() {
		return veh_make_desc;
	}
	public void setVeh_make_desc(String veh_make_desc) {
		this.veh_make_desc = veh_make_desc;
	}
	public String getModel_desc() {
		return model_desc;
	}
	public void setModel_desc(String model_desc) {
		this.model_desc = model_desc;
	}
	public Integer getModel_year() {
		return model_year;
	}
	public void setModel_year(Integer model_year) {
		this.model_year = model_year;
	}
	public String getRoute_start_txt() {
		return route_start_txt;
	}
	public void setRoute_start_txt(String route_start_txt) {
		this.route_start_txt = route_start_txt;
	}
	public String getRoute_end_txt() {
		return route_end_txt;
	}
	public void setRoute_end_txt(String route_end_txt) {
		this.route_end_txt = route_end_txt;
	}
	public Integer getPas_seating() {
		return pas_seating;
	}
	public void setPas_seating(Integer pas_seating) {
		this.pas_seating = pas_seating;
	}
	public Integer getControl_no() {
		return control_no;
	}
	public void setControl_no(Integer control_no) {
		this.control_no = control_no;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getOwner_type() {
		return owner_type;
	}
	public void setOwner_type(String owner_type) {
		this.owner_type = owner_type;
	}
	public String getStreet_no() {
		return street_no;
	}
	public void setStreet_no(String street_no) {
		this.street_no = street_no;
	}
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getPo_box() {
		return po_box;
	}
	public void setPo_box(String po_box) {
		this.po_box = po_box;
	}
	public String getPost_office() {
		return post_office;
	}
	public void setPost_office(String post_office) {
		this.post_office = post_office;
	}
	public String getParish_desc() {
		return parish_desc;
	}
	public void setParish_desc(String parish_desc) {
		this.parish_desc = parish_desc;
	}
	public String getHome_phone_no() {
		return home_phone_no;
	}
	public void setHome_phone_no(String home_phone_no) {
		this.home_phone_no = home_phone_no;
	}
	public String getWork_phone_no() {
		return work_phone_no;
	}
	public void setWork_phone_no(String work_phone_no) {
		this.work_phone_no = work_phone_no;
	}
	public String getMobile_phone_no() {
		return mobile_phone_no;
	}
	public void setMobile_phone_no(String mobile_phone_no) {
		this.mobile_phone_no = mobile_phone_no;
	}
	
	public Integer getBadge_trn() {
		return badge_trn;
	}
	public void setBadge_trn(Integer badge_trn) {
		this.badge_trn = badge_trn;
	}
	public String getBadge_first_name() {
		return badge_first_name;
	}
	public void setBadge_first_name(String badge_first_name) {
		this.badge_first_name = badge_first_name;
	}
	public String getBadge_last_name() {
		return badge_last_name;
	}
	public void setBadge_last_name(String badge_last_name) {
		this.badge_last_name = badge_last_name;
	}
	public String getBadge_type() {
		return badge_type;
	}
	public void setBadge_type(String badge_type) {
		this.badge_type = badge_type;
	}
	public String getFitness_no() {
		return fitness_no;
	}
	public void setFitness_no(String fitness_no) {
		this.fitness_no = fitness_no;
	}
	public String getDepot_desc() {
		return depot_desc;
	}
	public void setDepot_desc(String depot_desc) {
		this.depot_desc = depot_desc;
	}
	public Date getFitness_iss_date() {
		return fitness_iss_date;
	}
	public void setFitness_iss_date(Date fitness_iss_date) {
		this.fitness_iss_date = fitness_iss_date;
	}
	public Date getFitness_exp_date() {
		return fitness_exp_date;
	}
	public void setFitness_exp_date(Date fitness_exp_date) {
		this.fitness_exp_date = fitness_exp_date;
	}
	public String getIns_comp_name() {
		return ins_comp_name;
	}
	public void setIns_comp_name(String ins_comp_name) {
		this.ins_comp_name = ins_comp_name;
	}
	public Date getIns_iss_date() {
		return ins_iss_date;
	}
	public void setIns_iss_date(Date ins_iss_date) {
		this.ins_iss_date = ins_iss_date;
	}
	public Date getIns_exp_date() {
		return ins_exp_date;
	}
	public void setIns_exp_date(Date ins_exp_date) {
		this.ins_exp_date = ins_exp_date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getCust_id() {
		return cust_id;
	}
	public void setCust_id(Integer cust_id) {
		this.cust_id = cust_id;
	}
	public Integer getTrn() {
		return trn;
	}
	public void setTrn(Integer trn) {
		this.trn = trn;
	}
	
	public Integer getAppl_no() {
		return appl_no;
	}
	public void setAppl_no(Integer appl_no) {
		this.appl_no = appl_no;
	}
	public Date getAppl_date() {
		return appl_date;
	}
	public void setAppl_date(Date appl_date) {
		this.appl_date = appl_date;
	}
	public String getApp_lic_desc() {
		return app_lic_desc;
	}
	public void setApp_lic_desc(String app_lic_desc) {
		this.app_lic_desc = app_lic_desc;
	}
	public String getState_desc() {
		return state_desc;
	}
	public void setState_desc(String state_desc) {
		this.state_desc = state_desc;
	}
	public String getInfo_key() {
		return info_key;
	}
	public void setInfo_key(String info_key) {
		this.info_key = info_key;
	}
	
	
	
	
	
	
}

