package fsl.ta.toms.roms.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class NameValuePairVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6263050056881819153L;
	
private String name;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NameValuePairVO(String name, String value) {
		super();
		if(name!=null){
			this.name = name.trim();	
		}
		if(value!=null){
			this.value = value.trim();	
		}
	}
	
	
	public NameValuePairVO(String str1, String str2, String value){
		StringBuffer strRtn = new StringBuffer();
		
		if(StringUtils.trimToNull(str1)!=null){
			strRtn.append(str1.trim());
			if(StringUtils.trimToNull(str2)!=null){
				strRtn.append(" ");
				strRtn.append(str2.trim());
			}
		}else{
		     strRtn.append(StringUtils.trimToEmpty(str2));
		}
		
		this.name = strRtn.toString();
		this.value = value.trim();
	}
	/*public NameValuePairVO(Date startDate,Date endDate,String endorsement){
		super();
		StringBuffer buffer = new StringBuffer("");
		if(startDate!=null){
			buffer.append( DateFormatter.format(startDate));
		}
		if(endDate!=null){
			buffer.append(" to ");
			buffer.append(DateFormatter.format(endDate));
		}
		this.name = buffer.toString();
		this.value = endorsement;
	}*/
	
   public NameValuePairVO(String name, Integer value){
	   this.name = name;
	   this.value = value.toString();
   }
	
	
	
	public NameValuePairVO() {
		super();
		
	}
	
	public String getNameValueCombinedDisplay(){
		return name+"-"+value;
	}

}
