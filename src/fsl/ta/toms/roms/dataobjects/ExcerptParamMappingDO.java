package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_excerpt_param_mapping")
public class ExcerptParamMappingDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5971858363338735777L;
	
	public ExcerptParamMappingDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="param_map_id")
	Integer paramMapId;
	
	@Column(name="param_name")
	String paramName;
	
	@Column(name="param_desc")
	String paramDescription;
	
	@Column(name="param_size")
	Integer paramSize;
	
	@Column(name="map_col_name")
	String mapColName;
	
	@ManyToOne
	@JoinColumn(name="road_check_type_id")
	CDRoadCheckTypeDO roadCheckType;
	
	@Column(name="data_entered")
	String dataEntered;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	

	public Integer getParamMapId() {
		return paramMapId;
	}

	public void setParamMapId(Integer paramMapId) {
		this.paramMapId = paramMapId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamDescription() {
		return paramDescription;
	}

	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}

	public Integer getParamSize() {
		return paramSize;
	}

	public void setParamSize(Integer paramSize) {
		this.paramSize = paramSize;
	}

	public String getMapColName() {
		return mapColName;
	}

	public void setMapColName(String mapColName) {
		this.mapColName = mapColName;
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

	public CDRoadCheckTypeDO getRoadCheckType() {
		return roadCheckType;
	}

	public void setRoadCheckType(CDRoadCheckTypeDO roadCheckType) {
		this.roadCheckType = roadCheckType;
	}

	public String getDataEntered() {
		return dataEntered;
	}

	public void setDataEntered(String dataEntered) {
		this.dataEntered = dataEntered;
	}

	
}
