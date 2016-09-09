package org.fsl.roms.service.action;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

@Service
public class DownloadsMaintenanceAction extends BaseServiceAction {

	public DownloadsMaintenanceAction() {
		super();

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event loadDownloadDetails(RequestContext context) {
		
		context.getFlowScope().put("downloadNow", "false");
		// set the current users details
		HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		HttpSession session = req.getSession();

		// get the actual file contents
		try {
			String appPath = getPrintAppFilePathConfig();
			String appName = getPrintAppNameConfig();

			System.err.println(" path " + appPath + appName);

			if (StringUtils.isBlank(appPath) || StringUtils.isBlank(appName)) {
				addErrorMessageText(context, "File Name and File Path cannot be empty. Configure values in application.");
				return error();
			}

			// app details
			File file = new File(appPath + appName);
			if (!file.exists()) {
				addErrorMessageText(context, "File doesn't exists on server.");
				return error();
			} else {
				session.setAttribute(Constants.PrintApp.FILE, file);
				// set the current users details
				session.setAttribute(Constants.PrintApp.PATH, appPath);
				session.setAttribute(Constants.PrintApp.NAME, appName);

				context.getFlowScope().put("downloadNow", "true");
				addInfoMessageText(context, "Document generated successfully.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context, "There was an error downloading file.");
			return error();
		}

		return success();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getPrintAppFilePathConfig() throws Exception {

		Context ctx;
		String value = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env.lookup(Constants.PrintApp.PATH);
			value = StringUtils.trimToNull(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new Exception("Print App location was not configured in web.xml");
		}

		return value;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws Exception
	 * @throws FM_GeneralException
	 */
	private String getPrintAppNameConfig() throws Exception {

		Context ctx;
		String value = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env.lookup(Constants.PrintApp.NAME);
			value = StringUtils.trimToNull(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new Exception("Print App File Name was not configured in web.xml");
		}

		return value;

	}
	
	public void resetDownload(RequestContext context){
		System.err.println("Reset Download");
		context.getFlowScope().put("downloadNow", "false");
		
	}

}
