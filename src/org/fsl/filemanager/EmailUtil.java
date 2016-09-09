package org.fsl.filemanager;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.fsl.filemanager.exceptions.FM_EmailException;

public class EmailUtil {

	/*public static void main(String[] args) {

		String mailSmtpHost = "webmail1.fsl.org.jm";

		String mailTo = "jreid@fsl.org.jm";
		String mailCc = "ljones@fsl.org.jm";
		String mailFrom = "roms@fsl.org.jm";
		String mailSubject = "Email from ROMS";
		String mailText = "This is an email from ROMS";

		sendEmail(mailTo, mailCc, mailFrom, mailSubject, mailText, mailSmtpHost);
	}*/

	/**
	 * 
	 * @param to
	 * @param cc
	 * @param from
	 * @param subject
	 * @param text
	 * @param smtpHost
	 */
	public static void sendEmail(String to, String cc, String from,
			String subject, String text, String smtpHost) {
		try {

			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpHost);
			Session emailSession = Session.getDefaultInstance(properties);

			Message emailMessage = new MimeMessage(emailSession);
			emailMessage.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
			emailMessage.addRecipient(Message.RecipientType.CC,
					new InternetAddress(cc));
			emailMessage.setFrom(new InternetAddress(from));
			emailMessage.setSubject(subject);
			emailMessage.setText(text);

			emailSession.setDebug(true);

			Transport.send(emailMessage);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws FM_EmailException 
	 */
	public static void send80PercentUsageWarningMail(String filePath) throws FM_EmailException {

		

		try {
			
			// Recipient's email ID needs to be mentioned.
			String to = getToEmailAddress();//"jreid@fsl.org.jm";

			// Sender's email ID needs to be mentioned
			String from = getFromEmailAddress();//"roms@fsl.org.jm";

			// Assuming you are sending email from localhost
			String host = getEmailServerHost();//"webmail1.fsl.org.jm";

			// Get system properties
			Properties properties = System.getProperties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);
			
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Urgent - File Storage Capacity Reached Critical Level");

			// Now set the actual message
			message.setText("The storage space for directory " + filePath + " has reached critical level. It is now over 80% full.");

			// Send message
			Transport.send(message);
			
			//System.out.println("Sent message successfully....");
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
			
			throw new  FM_EmailException("Email was not sent.");
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void send90PercentUsageWarningMail(String filePath) throws FM_EmailException {

	
		try {
			
			// Recipient's email ID needs to be mentioned.
			String to = getToEmailAddress();//"jreid@fsl.org.jm";

			// Sender's email ID needs to be mentioned
			String from = getFromEmailAddress();//"roms@fsl.org.jm";

			// Assuming you are sending email from localhost
			String host = getEmailServerHost();//"webmail1.fsl.org.jm";

			// Get system properties
			Properties properties = System.getProperties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);
			
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Urgent - File Storage Capacity Reached Critical Level.");

			// Now set the actual message
			message.setText("The storage space for directory " + filePath + " has reached critical level. It is now over 90% full.");

			// Send message
			Transport.send(message);
			//System.out.println("Sent message successfully....");
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
			
			throw new  FM_EmailException("Email was not sent.");
		}
	}

	/************************** HELPER FUNCTIONS ************************************/
	
	/**
	 * 
	 * @return
	 * @throws FM_EmailException
	 */
	private static String getEmailServerHost() throws FM_EmailException {
		Context ctx;
		String host = null;
		try{
			ctx = new InitialContext();
			Context env = (Context)ctx.lookup("java:comp/env");
			host = (String)env.lookup("MAIL_SMTP_HOST");
			host = StringUtils.trimToNull(host);
	
		}catch(Exception exc){
			exc.printStackTrace();
			throw new FM_EmailException("MAIL_SMTP_HOST address was not configured in web.xml");
		}
		return host;

	}

	/**
	 * 
	 * @return
	 * @throws FM_EmailException
	 */
	private static String getFromEmailAddress() throws FM_EmailException {
		Context ctx;
		String emailAddress = null;
		try{
			ctx = new InitialContext();
			Context env = (Context)ctx.lookup("java:comp/env");
			emailAddress = (String)env.lookup("MAIL_FROM");
			emailAddress = StringUtils.trimToNull(emailAddress);

		}catch(Exception exc){
			exc.printStackTrace();
			throw new FM_EmailException("MAIL_FROM address was not configured in web.xml");
		}

		return emailAddress;
	}

	/**
	 * 
	 * @return
	 * @throws FM_EmailException
	 */
	private static String getToEmailAddress() throws FM_EmailException {
		Context ctx;
		String emailAddress = null;
		try{
			ctx = new InitialContext();
			Context env = (Context)ctx.lookup("java:comp/env");
			emailAddress = (String)env.lookup("MAIL_TO");
			emailAddress = StringUtils.trimToNull(emailAddress);

		}catch(Exception exc){
			exc.printStackTrace();
			throw new FM_EmailException("MAIL_TO address was not configured in web.xml");
		}
		
		return emailAddress;

	}

	/*********************************** END ***************************************/
}
