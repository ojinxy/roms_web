package fsl.ta.toms.roms.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import fsl.ta.toms.roms.dataobjects.DLCheckResultDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckDO;
import fsl.ta.toms.roms.dlwebservice.Address;


public class DLCheckResultBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4689491960765982150L;
	
	Integer driversLicCheckId;
	String dlNo, firstName, middleName,lastName,dlClass;
	byte[] photograph;
	Date issueDate;
	Date expiryDate;
	String currentUserName;
	RoadCheckBO roadCheckBO;
	Integer complianceID;
	String comment;
	
	//Additional fields needed to be displayed in Road Check
	String nationality;
	String collectorateIssued;
	String dateFirstIssued;
	String dateOfBirth;
	String gender;
	AddressBO address;
	String vehicleLicencedToDrive;
	String trnNo;
	
	
	public DLCheckResultBO(DLCheckResultDO dlCheckResultDO) {
		super();
		
		this.roadCheckBO = new RoadCheckBO(dlCheckResultDO.getRoadCheck());
		this.dlNo = dlCheckResultDO.getDlNo();
		this.firstName = dlCheckResultDO.getFirstName();
		this.middleName = dlCheckResultDO.getMiddleName();
		this.lastName = dlCheckResultDO.getLastName();
		this.dlClass = dlCheckResultDO.getDlClass();
		
		if (null != dlCheckResultDO.getPhotograph())
		{
			try{
			Blob photoBlob = dlCheckResultDO.getPhotograph();
			ByteArrayOutputStream output = new ByteArrayOutputStream();  
            InputStream is = photoBlob.getBinaryStream();  
		      
		    // IOUtils is from Apache Commons IO  
            this.photograph = IOUtils.toByteArray(is);  
			}
			catch(SQLException sqe)
			{
				sqe.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.issueDate = dlCheckResultDO.getIssueDate();
		this.expiryDate = dlCheckResultDO.getExpiryDate();
	
	}
	
	public DLCheckResultBO(RoadCheckDO roadCheck, String dlNo,
			String firstName, String middleName, String lastName,
			String dlClass, Blob photograph, Date issueDate, Date expiryDate, String nationality,
			String collectorateIssued, String dateFirstIssued,String dateOfBirth,
			String gender, AddressBO address, String vehicleLicencedToDrive, String trnNo) {
		super();
		
		this.roadCheckBO = new RoadCheckBO(roadCheck);
		this.dlNo = dlNo;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.dlClass = dlClass;
		
		if (null != photograph)
		{
			try{
			Blob photoBlob = photograph;
			ByteArrayOutputStream output = new ByteArrayOutputStream();  
            InputStream is = photoBlob.getBinaryStream();  
		      
		    // IOUtils is from Apache Commons IO  
            this.photograph = IOUtils.toByteArray(is);  
			}
			catch(SQLException sqe)
			{
				sqe.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		
		this.nationality = nationality;
		this.collectorateIssued = collectorateIssued;
		this.dateFirstIssued = dateFirstIssued;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.address = address;
		this.vehicleLicencedToDrive = vehicleLicencedToDrive;
		this.trnNo = trnNo;
	
	}
	public DLCheckResultBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getDriversLicCheckId() {
		return driversLicCheckId;
	}
	public void setDriversLicCheckId(Integer driversLicCheckId) {
		this.driversLicCheckId = driversLicCheckId;
	}
	
	public RoadCheckBO getRoadCheckBO() {
		return roadCheckBO;
	}
	public void setRoadCheckBO(RoadCheckBO roadCheckBO) {
		this.roadCheckBO = roadCheckBO;
	}
	public String getDlNo() {
		return dlNo;
	}
	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDlClass() {
		return dlClass;
	}
	public void setDlClass(String dlClass) {
		this.dlClass = dlClass;
	}
	public byte[] getPhotograph() {
		return photograph;
	}
	public void setPhotograph(byte[] photograph) {
		this.photograph = photograph;
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
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}
	
	public Integer getComplianceID() {
		return complianceID;
	}
	public void setComplianceID(Integer complianceID) {
		this.complianceID = complianceID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getCollectorateIssued() {
		return collectorateIssued;
	}
	public void setCollectorateIssued(String collectorateIssued) {
		this.collectorateIssued = collectorateIssued;
	}
	public String getDateFirstIssued() {
		return dateFirstIssued;
	}
	public void setDateFirstIssued(String dateFirstIssued) {
		this.dateFirstIssued = dateFirstIssued;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public AddressBO getAddress() {
		return address;
	}
	public void setAddress(AddressBO address) {
		this.address = address;
	}
	public String getVehicleLicencedToDrive() {
		return vehicleLicencedToDrive;
	}
	public void setVehicleLicencedToDrive(String vehicleLicencedToDrive) {
		this.vehicleLicencedToDrive = vehicleLicencedToDrive;
	}

	public String getTrnNo() {
		return trnNo;
	}

	public void setTrnNo(String trnNo) {
		this.trnNo = trnNo;
	}
	
	
	
	
}
