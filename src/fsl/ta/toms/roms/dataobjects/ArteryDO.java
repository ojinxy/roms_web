package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.dao.DAOFactory;

@Entity
@Table(name="ROMS_artery")
public class ArteryDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -775246815222845093L;

	
	public ArteryDO() {
		super();
		// TODO Auto-generated constructor stub
	}

		
	@Id
	@Column(name="artery_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer arteryId;
	
	@Column(name="description")
	String description;
	
	@Column(name="short_desc")
	String shortDescription;
	
	@ManyToOne	
	@JoinColumn(name="location_id")
	LocationDO location;
	
	@Column(name="points")
	String points;
	
	@Column(name="start_longitude")
	Float startlongitude;
	
	@Column(name="start_latitude")
	Float startlatitude;
	
	@Column(name="end_longitude")
	Float endlongitude;
	
	@Column(name="end_latitude")
	Float endlatitude;
	
	@Column(name="distance")
	Float distance;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getArteryId() {
		return arteryId;
	}

	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public LocationDO getLocation() {
		return location;
	}

	public void setLocation(LocationDO location) {
		this.location = location;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
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
	

	public void updateArteryDOFields(ArteryBO arteryBO) {
		this.description = arteryBO.getArteryDescription();
		this.points = arteryBO.getPoints();
		this.startlatitude = arteryBO.getStartlatitude();
		this.startlongitude = arteryBO.getStartlongitude();
		this.endlatitude = arteryBO.getEndlatitude();
		this.endlongitude = arteryBO.getEndlongitude();
		this.distance = arteryBO.getDistance();
		
		this.location = new LocationDO();
		if(arteryBO.getLocationId() != null) {
			this.location.setLocationId(arteryBO.getLocationId()) ;
		}
		
		this.shortDescription = arteryBO.getShortDescription();
		
		this.status = new StatusDO();
		if(arteryBO.getStatusId() != null) {
			this.status.setStatusId(arteryBO.getStatusId());
		}
		
	}

	/**
	 * 
	 * @param arteryBO
	 */
	public ArteryDO(ArteryBO arteryBO) {
		this.description = arteryBO.getArteryDescription();
		this.points = arteryBO.getPoints();
		this.startlatitude = arteryBO.getStartlatitude();
		this.startlongitude = arteryBO.getStartlongitude();
		this.endlatitude = arteryBO.getEndlatitude();
		this.endlongitude = arteryBO.getEndlongitude();
		this.distance = arteryBO.getDistance();
		
		this.location = new LocationDO();
		
		if(arteryBO.getLocationId() != null) {
			this.location.setLocationId(arteryBO.getLocationId()) ;
			
		}
		
		/*if(arteryBO.getParishCode() != null)
		{
			this.location.parish.setParishCode(arteryBO.getParishCode());
		}*/
		
		this.shortDescription = arteryBO.getShortDescription();
		
		this.status = new StatusDO();
		if(arteryBO.getStatusId() != null) {
			this.status.setStatusId(arteryBO.getStatusId());
		}
	}

}
