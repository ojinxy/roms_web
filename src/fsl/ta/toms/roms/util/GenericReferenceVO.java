/**
 * created by kpowell 2014-11-03
 */
package fsl.ta.toms.roms.util;

public class GenericReferenceVO {

	private Integer keyValue;
	private String keyDescription;
	private String type;
	
	
	
	
	public GenericReferenceVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GenericReferenceVO(Integer keyValue, String keyDescription,
			String type) {
		super();
		this.keyValue = keyValue;
		this.keyDescription = keyDescription;
		this.type = type;
	}

	public Integer getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(Integer keyValue) {
		this.keyValue = keyValue;
	}
	public String getKeyDescription() {
		return keyDescription;
	}
	public void setKeyDescription(String keyDescription) {
		this.keyDescription = keyDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((keyDescription == null) ? 0 : keyDescription.hashCode());
		result = prime * result
				+ ((keyValue == null) ? 0 : keyValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		//System.err.println("Generic equals");
		if (this.getKeyValue().equals(((GenericReferenceVO)obj).getKeyValue())){
			//System.out.println("same object");
			return true;
		}
		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericReferenceVO other = (GenericReferenceVO) obj;
		if (keyDescription == null) {
			if (other.keyDescription != null)
				return false;
		} else if (!keyDescription.equals(other.keyDescription))
			return false;
		if (keyValue == null) {
			if (other.keyValue != null)
				return false;
		} else if (!keyValue.equals(other.keyValue))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
