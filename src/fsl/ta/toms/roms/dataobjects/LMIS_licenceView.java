/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 * @category This is a hibernate object which refers to the view roms_lmis_licence_view
 */
@Entity
@Table(name="roms_lmis_licence_view")
public class LMIS_licenceView implements Serializable 
{


	
	public LMIS_licenceView() 
	{
		super();
		
	}



	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="licence_no")
	Integer licenceNo;
	
	@Column(name="lic_type")
	String licType;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="expiry_date")
	Date expiryDate;
	
	@Column(name="lic_plate")
	String licPlate;
	
	@Column(name="status_desc")
	String statusDesc;
	
	@Column(name="status_code")
	String statusCode;
	
	@Column(name="route_desc")
	String routeDesc;
	
	@Column(name="veh_make_desc")
	String vehMakeDesc;
	
	@Column(name="model_desc")
	String modelDesc;
	
	@Column(name="model_year")
	Integer modelYear;
	
	@Column(name="route_start")
	String routeStart;
	
	@Column(name="route_end")
	String routeEnd;
	
	@Column(name="seat_capacity")
	Integer seatCapacity;
	
	@Column(name="control_no")
	Integer controlNo;
	
	@OneToMany
	@JoinColumn(name="licence_no")
	List<LMIS_licenceOwnerView> licenceOwners;



	public Integer getLicenceNo() {
		return licenceNo;
	}



	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}



	public String getLicType() {
		return licType;
	}



	public void setLicType(String licType) {
		this.licType = licType;
	}



	public Date getIssueDate() {
		return issueDate;
	}



	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}



	public Date getExpiryDate() {
		return expiryDate;
	}



	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}



	public String getLicPlate() {
		return licPlate;
	}



	public void setLicPlate(String licPlate) {
		this.licPlate = licPlate;
	}



	public String getStatusDesc() {
		return statusDesc;
	}



	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}



	public String getRouteDesc() {
		return routeDesc;
	}



	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}



	public String getVehMakeDesc() {
		return vehMakeDesc;
	}



	public void setVehMakeDesc(String vehMakeDesc) {
		this.vehMakeDesc = vehMakeDesc;
	}



	public String getModelDesc() {
		return modelDesc;
	}



	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}



	public Integer getModelYear() {
		return modelYear;
	}



	public void setModelYear(Integer modelYear) {
		this.modelYear = modelYear;
	}



	public List<LMIS_licenceOwnerView> getLicenceOwners() {
		return licenceOwners;
	}



	public void setLicenceOwners(List<LMIS_licenceOwnerView> licenceOwners) {
		this.licenceOwners = licenceOwners;
	}



	public String getRouteStart() {
		return routeStart;
	}



	public void setRouteStart(String routeStart) {
		this.routeStart = routeStart;
	}



	public String getRouteEnd() {
		return routeEnd;
	}



	public void setRouteEnd(String routeEnd) {
		this.routeEnd = routeEnd;
	}



	public Integer getSeatCapacity() {
		return seatCapacity;
	}



	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}



	public Integer getControlNo() {
		return controlNo;
	}



	public void setControlNo(Integer controlNo) {
		this.controlNo = controlNo;
	}



	public String getStatusCode()
	{
		return statusCode;
	}



	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}
	
	
	

}
