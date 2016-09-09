package org.fsl.roms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fsl.ta.toms.roms.bo.ScannedDocBO;

/**
 * Servlet implementation class ViewScannedDocServlet
 */
public class ViewScannedDocServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// public static String ROMS_LOGGED_IN_USER = "romsLoggedInUser";
	public static String SCANNED_DOC = "scannedDocToViewNPrint";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewScannedDocServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession();

		ScannedDocBO scannedDocBO = (ScannedDocBO) session.getAttribute(SCANNED_DOC);

		if (scannedDocBO == null) {
			System.err.println(" Scanned Doc details is empty.");
			return;
		}

		try {
			if (scannedDocBO != null) {

				if (null != scannedDocBO.getFileContents()) {

					writeImage(scannedDocBO, request, response);// write
																// repsonse
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// clean up session
			session.setAttribute(SCANNED_DOC, null);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	void writeImage(ScannedDocBO scannedDoc, HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setContentLength(scannedDoc.getFileContents().length);
		resp.setContentType(scannedDoc.getMimeType());
		try {
			resp.getOutputStream().write(scannedDoc.getFileContents());
			// resp.getOutputStream().flush();
			// resp.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				resp.getOutputStream().flush();
				resp.getOutputStream().close();

			} catch (IOException e) {

			}
		}
	}

}
