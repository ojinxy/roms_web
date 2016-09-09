package org.fsl.roms.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.commons.lang.WordUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.constants.Constants.DocumentType;
import org.fsl.roms.service.action.BaseServiceAction;
import org.fsl.roms.util.StringUtil;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.documents.view.SummonsView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.webservices.DocumentsManager;

/**
 * 
 * @author jreid
 * 
 */
public class GenerateDocsServlet extends BaseHttpServlet {

	private static String SUMMONS_PAGE_1 = "WEB-INF/documentTemplates/summons.jasper";// "WEB-INF/documentTemplates/SummonsPage1.jasper";
	private static String WARNING_NOTICE = "WEB-INF/documentTemplates/WarningNotice.jasper";
	private static String WARNING_NOTICE_NO_PROSECUTION = "WEB-INF/documentTemplates/WarningNoProsecution.jasper";
	public static String ROMS_LOGGED_IN_USER = "romsLoggedInUser";
	private static int NOTICE_COPIES = 6;
	
	private String offenderFirstName;
	private String offenderLastName;

	public GenerateDocsServlet() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3018032490428526377L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		List<DocumentViewBO> documentList = (List<DocumentViewBO>) session.getAttribute(Constants.DocumentPrinting.DOCUMENT_LIST);

		BaseServiceAction baseServiceAction = new BaseServiceAction();

		// get the name of the user whose session is active
		String romsLoggedInUser = (String) session.getAttribute(ROMS_LOGGED_IN_USER);

		if (StringUtils.isBlank(romsLoggedInUser)) {
			System.err.println(" User details required");
			return;
		}

		List<byte[]> allBytes = new ArrayList<byte[]>();

		if (documentList == null || documentList.isEmpty()) {
			return;
		}
		try {
			for (DocumentViewBO document : documentList) {

				if (StringUtils.isNotBlank(document.getDocumentTypeCode()) && document.getAutomaticSerialNo() != null) {
					// will hold the path to the file
					String relativeWebPath = null;
					// the file with the jasper report
					File reportFile = null;
					// will hold the byte stream
					byte[] bytes = null;

					// get the proxy
					DocumentsManager documentsManager = new DocumentsManager();

					//proxy._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim() + "DocumentsManagerService");

					/********************** Summons *************************/
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.SUMMONS)) {
						relativeWebPath = SUMMONS_PAGE_1;
						String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
						reportFile = new File(absoluteDiskPath);

						/**************** GET THE DETAILS *****************/
						SummonsBO summons = documentsManager.getSummonsDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (summons != null) {
							if(!StringUtil.isSet(this.offenderFirstName) ||!StringUtil.isSet(this.offenderLastName))
							{
								this.offenderFirstName = summons.getOffenderFirstName();
								
								this.offenderLastName = summons.getOffenderLastName();
							}
							
							
							
							summons.setCurrentUsername(romsLoggedInUser);

							SummonsView summonsView = documentsManager.generateSummonsForm(summons);

							if (summonsView != null) {
								// list with datasource beans
								List<SummonsView> datasourceList = new ArrayList<SummonsView>();

								datasourceList.add(summonsView);

								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // discharge form null
						} // summons null

					}
					/********************** Warning notice *************************/
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE)) {
						relativeWebPath = WARNING_NOTICE;
						String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
						reportFile = new File(absoluteDiskPath);

						/**************** GET THE DETAILS *****************/
						WarningNoticeBO warningNoticeBO = documentsManager.getWarningNoticeDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (warningNoticeBO != null) {
							if(!StringUtil.isSet(this.offenderFirstName) ||!StringUtil.isSet(this.offenderLastName))
							{
								this.offenderFirstName = warningNoticeBO.getOffenderFirstName();
								
								this.offenderLastName = warningNoticeBO.getOffenderLastName();
							}
							
							warningNoticeBO.setCurrentUsername(romsLoggedInUser);

							NoticeView warningNoticeView = documentsManager.generateWarningNoticeForm(warningNoticeBO);

							if (warningNoticeView != null) {
								warningNoticeView.setTaStaffAssignedLocation(WordUtils.capitalize(baseServiceAction.getOfficeLocationDesc(warningNoticeView.getTaStaffAssignedLocation())));
								// list with datasource beans
								List<NoticeView> datasourceList = new ArrayList<NoticeView>();

								int x = 0;
								while (x < NOTICE_COPIES) {
									datasourceList.add(warningNoticeView);
									x++;
								}

								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // form null
						} // summons null

					}
					/********************** Waring No Prosecution *************************/
					if (document.getDocumentTypeCode().equalsIgnoreCase(DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
						relativeWebPath = WARNING_NOTICE_NO_PROSECUTION;
						// get the complete file name on disk
						String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
						reportFile = new File(absoluteDiskPath);

						/**************** GET THE DETAILS *****************/
						WarningNoProsecutionBO warningNoProsecutionBO = documentsManager.getWarningNoProsecutionDetails(Integer.valueOf(document.getAutomaticSerialNo()));

						if (warningNoProsecutionBO != null) {
							if(!StringUtil.isSet(this.offenderFirstName) ||!StringUtil.isSet(this.offenderLastName))
							{
								this.offenderFirstName = warningNoProsecutionBO.getOffenderFirstName();
								
								this.offenderLastName = warningNoProsecutionBO.getOffenderLastName();
							}
							
							warningNoProsecutionBO.setCurrentUsername(romsLoggedInUser);

							NoticeView warningNoProsecutionView = documentsManager.generateWarningNoProsecutionForm(warningNoProsecutionBO);

							if (warningNoProsecutionView != null) {
								warningNoProsecutionView.setTaStaffAssignedLocation(WordUtils.capitalize(baseServiceAction.getOfficeLocationDesc(warningNoProsecutionView.getTaStaffAssignedLocation())));

								// list with datasource beans
								List<NoticeView> datasourceList = new ArrayList<NoticeView>();

								int x = 0;
								while (x < NOTICE_COPIES) {
									datasourceList.add(warningNoProsecutionView);
									x++;
								}

								bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), new HashMap(), new JRBeanCollectionDataSource(datasourceList));

								if (bytes != null) {
									// writeImage(bytes, request, response);//
									// write
									// response
									allBytes.add(bytes);
								}
							} // discharge form null
						} // summons null

					}
				}
			}

			// byte stream is done so write bytes
			concatPDFs(allBytes, request, response);// write

			// clean up session
			session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, null);
			session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, null);

		} catch (JRException e) {
			// display stack trace in the browser
			/* StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter);
			 * e.printStackTrace(printWriter); response.setContentType("text/plain");
			 * response.getOutputStream().print(stringWriter.toString()); */
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorSavingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				// clean up session
				session.setAttribute(Constants.DocumentPrinting.DOCUMENT_LIST, null);
				session.setAttribute(GenerateDocServlet.ROMS_LOGGED_IN_USER, null);

				response.getOutputStream().flush();
				response.flushBuffer();

			} catch (IOException ioe) {
				ioe.printStackTrace();

			}
		}
	}

	void concatPDFs(List<byte[]> streamOfPDFFiles, HttpServletRequest req, HttpServletResponse resp) {

		// resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// resp.setContentLength(bytes.length);
		// resp.setContentType("application/pdf");
		HttpSession session = req.getSession();
		
		OutputStream outputStream = null;
		try {
			outputStream = resp.getOutputStream();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Document document = new Document(PageSize.LEGAL);

		try {
			List<byte[]> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			// Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			for (byte[] byteL : pdfs) {
				// InputStream pdf = new FileInputStream(file);
				PdfReader pdfReader = new PdfReader(byteL);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			writer.setDuration(1000000000);
			writer.setFullCompression();
			
			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);

					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					boolean paginate = false;
					if (paginate) {

						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}

			/*oa-10April2014
			 * This is to allow naming of documents on the tablet.*/
			if((Boolean)session.getAttribute(Constants.IS_MOBILE))
			{
				resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				resp.setContentType("application/pdf");
				resp.setHeader("Content-Disposition", "attachment; filename=\"ROMS_" +this.offenderLastName.replaceAll("[^a-zA-Z]", "") +
						this.offenderFirstName.replaceAll("[^a-zA-Z]","") + ".pdf\"");
				
				
			}
			else
			{
				resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				resp.setContentType("application/pdf");
			}

			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.offenderFirstName = null;
			this.offenderLastName = null;
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * This is used to add the byte arrays together
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private byte[] addBytes(byte[] a, byte[] b) {
		byte[] destination = null;

		if (a != null && b != null) {
			// create a destination array that is the size of the two arrays
			destination = new byte[a.length + b.length];

			// copy a into start of destination (from pos 0, copy a.length
			// bytes)
			System.arraycopy(a, 0, destination, 0, a.length);

			// copy b into end of destination (from pos a.length, copy b.length
			// bytes)
			System.arraycopy(b, 0, destination, a.length, b.length);
		}

		if (a != null && b == null) {
			destination = a;
		}

		if (b != null && a == null) {
			destination = b;
		}

		return destination;
	}

}
