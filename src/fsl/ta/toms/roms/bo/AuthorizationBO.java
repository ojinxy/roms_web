package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;



public class AuthorizationBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7520916383409665736L;

	private String username;
	private String password;
	private String personType;
	private String permission;
	

	public AuthorizationBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(StringUtils.isNotBlank(username))
			this.username = username.trim();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(StringUtils.isNotBlank(password))
			this.password = password.trim();
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		if(StringUtils.isNotBlank(personType))
			this.personType = personType.trim();
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		if(StringUtils.isNotBlank(permission))
			this.permission = permission.trim();
	}



	@Override
	public String toString() {
		return "AuthorizationBO [username=" + username + ", password="
				+ password + ", personType=" + personType + ", permission="
				+ permission + "]";
	}
	
	
}
