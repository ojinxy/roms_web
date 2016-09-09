package org.fsl.jasper.print;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

public class PDFServlet extends BaseHttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4683519915975921302L;
	public static final String PDF_FILE_NAME_SESSION_ATTRIBUTE = "filename";
	public static final String COMPRESSED_OUTPUT_REQUEST_PARAMETER = "compressed";
	public static final String SAVE_VIEW_DIALOG_REQUEST_PARAMETER = "dialog";
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 	{
				
				
				List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);

				if (jasperPrintList == null)
				{
					throw new ServletException("No JasperPrint documents found on the HTTP session.");
				}
				
				Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
				Boolean isCompressed = Boolean.valueOf(request.getParameter(COMPRESSED_OUTPUT_REQUEST_PARAMETER));
				Boolean useDialog = Boolean.valueOf(request.getParameter(SAVE_VIEW_DIALOG_REQUEST_PARAMETER));
				
				if (isBuffered.booleanValue()){
						
					
					FileBufferedOutputStream fbos = new FileBufferedOutputStream();
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
					
					if(isCompressed)
					{
						exporter.setParameter(JRPdfExporterParameter.IS_COMPRESSED, Boolean.TRUE);
					}
					
					try 
					{
						exporter.exportReport();
						fbos.close();

						if (fbos.size() > 0)
						{
							if(useDialog)
							{
								String fileNameInSession = getFileNameFromSession(request);
								response.addHeader("Content-disposition", "attachment; filename=" + fileNameInSession  );
							}
							
							response.setContentType("application/pdf");
							response.setContentLength(fbos.size());
							ServletOutputStream ouputStream = response.getOutputStream();
			
							try
							{
								fbos.writeData(ouputStream);
								fbos.dispose();
								ouputStream.flush();
							}
							finally
							{
								if (ouputStream != null)
								{
									try
									{
										ouputStream.close();
									}
									catch (IOException ex)
									{
									}
								}
							}
						}
					} 
					catch (JRException e) 
					{
						throw new ServletException(e);
					}
					finally
					{
						fbos.close();
						fbos.dispose();
						
						request.getSession()
						.removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);

					}

				}
				else
				{
					
					if(useDialog)
					{
						String fileNameInSession = getFileNameFromSession(request);
						response.addHeader("Content-disposition", "attachment; filename=" + fileNameInSession  );
					}
					
					response.setContentType("application/pdf");

					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
					
					OutputStream ouputStream = response.getOutputStream();
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
					
					if(isCompressed)
					{
						exporter.setParameter(JRPdfExporterParameter.IS_COMPRESSED, Boolean.TRUE);
					}
					

					try 
					{
						exporter.exportReport();
					} 
					catch (JRException e) 
					{
						throw new ServletException(e);
					}
					finally
					{
						if (ouputStream != null)
						{
							try
							{
								ouputStream.close();
							}
							catch (IOException ex)
							{
							}
							
							request.getSession()
							.removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
			
							
						}
					}
				}
			}
	
	private String getFileNameFromSession(HttpServletRequest request)
	{
		String fileNameInSession = (String)request.getSession().getAttribute(PDF_FILE_NAME_SESSION_ATTRIBUTE);			
		
		fileNameInSession = (fileNameInSession != null ? fileNameInSession : "document.pdf");
		
		return fileNameInSession;
	}



}
