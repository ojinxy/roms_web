package org.fsl.roms.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import org.apache.commons.lang.StringUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants.DocumentType;
import org.fsl.roms.service.action.BaseServiceAction;

import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.documents.view.SummonsDischargeAndReleaseFormView;
import fsl.ta.toms.roms.documents.view.SummonsView;
import fsl.ta.toms.roms.webservices.DocumentsManager;

/**
 * 
 * @author jreid
 * 
 */
public class GenerateDocServlet extends BaseHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2326886648711188833L;
	private static String SUMMONS_PAGE_1 = "WEB-INF/documentTemplates/summons.jasper";// "WEB-INF/documentTemplates/SummonsPage1.jasper";
	private static String WARNING_NOTICE = "WEB-INF/documentTemplates/WarningNotice.jasper";
	private static String WARNING_NOTICE_NO_PROSECUTION = "WEB-INF/documentTemplates/WarningNoProsecution.jasper";
	private static String DISCHARGE_FORM = "WEB-INF/documentTemplates/ReleaseNDischargeForm.jasper";
	public static String ROMS_LOGGED_IN_USER = "romsLoggedInUser";
	private static int NOTICE_COPIES = 6;

	public GenerateDocServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession();
		BaseServiceAction baseServiceAction = new BaseServiceAction();

		String documentType = request.getParameter("documentType");
		String documentId = request.getParameter("documentId");

		// get the name of the user whose session is active
		String romsLoggedInUser = (String) session.getAttribute(ROMS_LOGGED_IN_USER);

		if (StringUtils.isBlank(romsLoggedInUser)) {
			System.err.println(" User details required");
			return;
		}

		try {

			if (StringUtils.isNotBlank(documentType) && StringUtils.isNotBlank(documentId)) {
				// will hold the path to the file
				String relativeWebPath = null;
				// the file with the jasper report
				File reportFile = null;
				// will hold the byte stream
				byte[] bytes = null;

				// get the proxy
				DocumentsManager documentsManager = new DocumentsManager();

				//proxy._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "DocumentsManagerService");

				if (documentType.equalsIgnoreCase(DocumentType.DISCHARGE_FORM)) {
					relativeWebPath = DISCHARGE_FORM;
					String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);

					reportFile = new File(absoluteDiskPath);
					// System.err.println(" discharge form");
					/**************** GET THE DETAILS *****************/
					SummonsBO summons = documentsManager.getSummonsDetails(Integer.valueOf(documentId));

					if (summons != null) {
						summons.setCurrentUsername(romsLoggedInUser);

						SummonsDischargeAndReleaseFormView dishcargeView = documentsManager.generateSummonsDischargeAndReleaseForm(summons);

						// System.err.println(" discharge  view " + dishcargeView);
						if (dishcargeView != null) {

							// list with datasource beans
							List<SummonsDischargeAndReleaseFormView> datasourceList = new ArrayList<SummonsDischargeAndReleaseFormView>();

							int x = 0;
							while (x < NOTICE_COPIES) {
								datasourceList.add(dishcargeView);
								x++;
							}

							bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

							if (bytes != null) {
								writeImage(bytes, request, response);// write
																		// response
							}
						} // discharge form null
					} // summons null

				}
				/********************** Summons *************************/
				if (documentType.equalsIgnoreCase(DocumentType.SUMMONS)) {
					relativeWebPath = SUMMONS_PAGE_1;
					String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
					reportFile = new File(absoluteDiskPath);

					/**************** GET THE DETAILS *****************/
					SummonsBO summons = documentsManager.getSummonsDetails(Integer.valueOf(documentId));

					if (summons != null) {
						summons.setCurrentUsername(romsLoggedInUser);

						SummonsView summonsView = documentsManager.generateSummonsForm(summons);

						if (summonsView != null) {
							// list with datasource beans
							List<SummonsView> datasourceList = new ArrayList<SummonsView>();

							datasourceList.add(summonsView);

							bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

							if (bytes != null) {
								writeImage(bytes, request, response);// write
								// System.err.println("bytes " + bytes); // response
							}
						} // discharge form null
					} // summons null

				}
				/********************** Warning notice *************************/
				if (documentType.equalsIgnoreCase(DocumentType.WARNING_NOTICE)) {
					relativeWebPath = WARNING_NOTICE;
					String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
					reportFile = new File(absoluteDiskPath);
					// System.err.println("warn - path " + absoluteDiskPath);

					/**************** GET THE DETAILS *****************/
					Integer num = Integer.valueOf(documentId);
					WarningNoticeBO warningNoticeBO = documentsManager.getWarningNoticeDetails(num);
					// System.err.println(" warning notice " + warningNoticeBO);
					if (warningNoticeBO != null) {
						warningNoticeBO.setCurrentUsername(romsLoggedInUser);

						NoticeView warningNoticeView = documentsManager.generateWarningNoticeForm(warningNoticeBO);

						if (warningNoticeView != null) {
							warningNoticeView.setTaStaffAssignedLocation(baseServiceAction.getOfficeLocationDesc(warningNoticeView.getTaStaffAssignedLocation()));

							if (warningNoticeBO.getRoadLicence() != null && ((warningNoticeBO.getRoadLicence().getLicenceOwners() != null) && !warningNoticeBO.getRoadLicence().getLicenceOwners().isEmpty())) {
								// warningNoticeView.setOwnerAddress(warningNoticeBO.getRoadLicence().getLicenceOwners().get(0).getFirstName());
								warningNoticeView.setOwnerFirstName(warningNoticeBO.getRoadLicence().getLicenceOwners().get(0).getFirstName());
								warningNoticeView.setOwnerLastName(warningNoticeBO.getRoadLicence().getLicenceOwners().get(0).getLastName());
							}
							// list with datasource beans
							List<NoticeView> datasourceList = new ArrayList<NoticeView>();

							int x = 0;
							while (x < NOTICE_COPIES) {
								datasourceList.add(warningNoticeView);
								x++;
							}

							bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

							// System.err.println(" bytes " + bytes);
							if (bytes != null) {
								writeImage(bytes, request, response);// write
																		// response
							}
						} // form null
					} // summons null

				}
				/********************** Waring No Prosecution *************************/
				if (documentType.equalsIgnoreCase(DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					relativeWebPath = WARNING_NOTICE_NO_PROSECUTION;
					// get the complete file name on disk
					String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
					reportFile = new File(absoluteDiskPath);

					// System.err.println(" no prose");

					/**************** GET THE DETAILS *****************/
					WarningNoProsecutionBO warningNoProsecutionBO = documentsManager.getWarningNoProsecutionDetails(Integer.valueOf(documentId));

					if (warningNoProsecutionBO != null) {
						warningNoProsecutionBO.setCurrentUsername(romsLoggedInUser);

						NoticeView warningNoProsecutionView = documentsManager.generateWarningNoProsecutionForm(warningNoProsecutionBO);

						if (warningNoProsecutionView != null) {
							warningNoProsecutionView.setTaStaffAssignedLocation(baseServiceAction.getOfficeLocationDesc(warningNoProsecutionView.getTaStaffAssignedLocation()));

							if (warningNoProsecutionBO.getRoadLicence() != null && ((warningNoProsecutionBO.getRoadLicence().getLicenceOwners() != null) && !warningNoProsecutionBO.getRoadLicence().getLicenceOwners().isEmpty())) {
								// warningNoProsecutionView.setOwnerAddress(warningNoProsecutionBO.getRoadLicence().getLicenceOwners().get(0).getFirstName());
								warningNoProsecutionView.setOwnerFirstName(warningNoProsecutionBO.getRoadLicence().getLicenceOwners().get(0).getFirstName());
								warningNoProsecutionView.setOwnerLastName(warningNoProsecutionBO.getRoadLicence().getLicenceOwners().get(0).getLastName());
							}

							// list with datasource beans
							List<NoticeView> datasourceList = new ArrayList<NoticeView>();

							int x = 0;
							while (x < NOTICE_COPIES) {
								datasourceList.add(warningNoProsecutionView);
								x++;
							}

							bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

							if (bytes != null) {
								writeImage(bytes, request, response);// write
																		// response
							}
						} // discharge form null
					} // summons null

				}

			}

			// clean up session
			session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, null);

		} catch (JRException e) {
			// display stack trace in the browser
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
			response.flushBuffer();
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {

			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
			response.flushBuffer();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}

	void writeImage(byte[] bytes, HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setContentLength(bytes.length);
		resp.setContentType("application/pdf");
		try {
			resp.getOutputStream().write(bytes);
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				resp.getOutputStream().flush();
				resp.getOutputStream().close();

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

}
