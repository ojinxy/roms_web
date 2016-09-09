package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ArteryDO;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class ArteryBO implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = -2397055159340244785L;
	private String arteryDescription;
	private Integer arteryId;
	private String currentUsername;
	private String locationDescription;
	private Integer locationId;
	private String shortDescription;
	private String statusDescription;
	private  String statusId;
	
	private String createusername;
	private Date createdtime;
	
	private String points;
	
	private Float startlongitude;
	
	private Float startlatitude;
	
	private Float endlongitude;
	
	private Float endlatitude;
	
	private Float distance;
	
	private String parishCode;
	private String parishDescription;
	
	private AuditEntryBO auditEntryBO;
	
	boolean selected;
	
	public ArteryBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param arteryDO
	 */
	public ArteryBO(ArteryDO arteryDO) {
		this.arteryId = arteryDO.getArteryId();
		this.arteryDescription = arteryDO.getDescription();
		this.createusername = arteryDO.getAuditEntry().getCreateUsername();
		this.createdtime = arteryDO.getAuditEntry().getCreateDTime();
		
		this.points = arteryDO.getPoints();
		this.startlatitude = arteryDO.getStartlatitude();
		this.startlongitude = arteryDO.getStartlongitude();
		this.endlatitude = arteryDO.getEndlatitude();
		this.endlongitude = arteryDO.getEndlongitude();
		this.distance = arteryDO.getDistance();
		
		
		if(arteryDO.getLocation() != null) {
			this.locationDescription = arteryDO.getLocation().getShortDesc();
			this.locationId = arteryDO.getLocation().getLocationId();
			
		}
		
		if(arteryDO.getLocation().getParish() != null)
		{
			this.parishCode = arteryDO.getLocation().getParish().getParishCode();
			this.parishDescription = arteryDO.getLocation().getParish().getDescription();
		}
		this.shortDescription = arteryDO.getShortDescription();
		
		if(arteryDO.getStatus() != null) {
			this.statusId = arteryDO.getStatus().getStatusId();
			this.statusDescription = arteryDO.getStatus().getDescription();
		}
		
		if(arteryDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(arteryDO.getAuditEntry());
		}
	}

	public String getArteryDescription() {
		return arteryDescription;
	}

	public Integer getArteryId() {
		return arteryId;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

		
	public String getCreateusername() {
		return createusername;
	}

	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}

	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Float getStartlongitude() {
		return startlongitude;
	}

	public void setStartlongitude(Float startlongitude) {
		this.startlongitude = startlongitude;
	}

	public Float getStartlatitude() {
		return startlatitude;
	}

	public void setStartlatitude(Float startlatitude) {
		this.startlatitude = startlatitude;
	}

	public Float getEndlongitude() {
		return endlongitude;
	}

	public void setEndlongitude(Float endlongitude) {
		this.endlongitude = endlongitude;
	}

	public Float getEndlatitude() {
		return endlatitude;
	}

	public void setEndlatitude(Float endlatitude) {
		this.endlatitude = endlatitude;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public void setArteryDescription(String arteryDescription) {
		if(StringUtils.isNotBlank(arteryDescription))
			this.arteryDescription = arteryDescription.trim();
	}

	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public void setLocationDescription(String locationDescription) {
		if(StringUtils.isNotBlank(locationDescription))
			this.locationDescription = locationDescription.trim();
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public void setShortDescription(String shortDescription) {
		if(StringUtils.isNotBlank(shortDescription))
			this.shortDescription = shortDescription.trim();
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	/**
	 * @return the auditEntryBO
	 */
	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}

	/**
	 * @param auditEntryBO the auditEntryBO to set
	 */
	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	public String getParishDescription() {
		return parishDescription;
	}

	public void setParishDescription(String parishDescription) {
		this.parishDescription = parishDescription;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	

}
