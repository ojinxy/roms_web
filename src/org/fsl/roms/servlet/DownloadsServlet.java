package org.fsl.roms.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;

/**
 * 
 * @author jreid Servlet implementation class DownloadsServlet
 */
public class DownloadsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7667451982832762665L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession();

		String path = (String) session.getAttribute(Constants.PrintApp.PATH);
		String fileName = (String) session.getAttribute(Constants.PrintApp.NAME);

		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(path)) {
			throw new ServletException("File Name can't be null or empty");
		}
		try {
			/* File file = new File(path + fileName);//
			 * request.getServletContext().getAttribute("FILES_DIR")+File.separator+fileName); if (!file.exists()) {
			 * throw new ServletException("File doesn't exists on server."); } */
			File file = (File) session.getAttribute(Constants.PrintApp.FILE);
			if (file != null) {
				System.out.println("File location on server::" + file.getAbsolutePath());
				ServletContext ctx = getServletContext();
				InputStream fis = new FileInputStream(file);
				String mimeType = ctx.getMimeType(file.getAbsolutePath());
				response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "attachment; filename=\"" + "ROMSPrintApp.apk" + "\"");
				// "ROMSPrintApp.apk"
				ServletOutputStream os = response.getOutputStream();
				byte[] bufferData = new byte[1024];
				int read = 0;
				while ((read = fis.read(bufferData)) != -1) {
					os.write(bufferData, 0, read);
				}
				os.flush();
				os.close();
				fis.close();
			} else {
				System.err.println(" File passed as null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// clean up session
			// session.setAttribute(Constants.PrintApp.PATH, null);
			// session.setAttribute(Constants.PrintApp.NAME, null);
		}

	}

}
