package org.fsl.roms.view;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.webservices.ROMSUserDetails;

public class OverrideDetailsSecurityView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String userName;
	private String password;//the string PLEASE DO NOT ENCRYPT
	private String confirmPassword;
	
	private String permission;
	
	
	private String comment;

	
	
	public OverrideDetailsSecurityView(String userName, String password,
			String confirmPassword, String passwordHash) {
		super();
		this.userName = userName;
		this.password = password;
		this.confirmPassword = confirmPassword;
		//this.passwordHash = passwordHash;
	}

	public OverrideDetailsSecurityView() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param view
	 * @return
	 */
	public boolean validateUserDetails( RequestContext context, ROMSUserDetails proxy) {
		boolean passed = true;
					
			if(StringUtils.isBlank(this.userName)){
				context.getMessageContext()
				.addMessage(
						(MessageResolver) new MessageBuilder().error().code("RequiredFields").arg("Username"));
				passed = false;
			}
			

			if(StringUtils.isBlank(this.password)){
				context.getMessageContext()
				.addMessage(
						(MessageResolver) new MessageBuilder().error().code("RequiredFields").arg("Password"));
				passed = false;
			}

			if(StringUtils.isBlank(this.comment)){
				context.getMessageContext()
				.addMessage(
						(MessageResolver) new MessageBuilder().error().code("RequiredFields").arg("Comment"));
				passed = false;
			}
			
			/*if(StringUtils.isBlank(this.permission)){
				context.getMessageContext()
				.addMessage(
						(MessageResolver) new MessageBuilder().error().code("RequiredFields").arg("Permission"));
				passed = false;
			}*/
			
			
			
			if(StringUtils.isNotBlank(this.password) && StringUtils.isNotBlank(this.userName)){// && StringUtils.isNotBlank(this.permission)){
			
				
					try {
						passed = proxy.validUser(this.password, this.userName);
					} catch (RequiredFieldMissingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// && proxy.hasPermision(this.userName, this.permission);
			
			
			}
			
			return passed;	
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
