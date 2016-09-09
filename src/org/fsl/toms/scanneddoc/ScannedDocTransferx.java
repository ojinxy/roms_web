/**
 * Created By: jreid
 * Date: Jul 3, 2013
 *
 */
package org.fsl.toms.scanneddoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.bo.ScannedDocBO;

/**
 * @author jreid Created Date: Jul 3, 2013
 */
public class ScannedDocTransferx {

	public static void main(String[] args) {
		File file = new File("C:\\Directory1");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			System.out.println("Directory already exists!");
		}

		// upload fiile
		// getFileForUpload();
		try {
			if(isLinuxOS()) {
				complexExample();
				System.err.println(" is linux ");
			}
			
			if(isWindowsOS()) {
				complexExample();
				System.err.println(" is windows ");	
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static ScannedDocBO saveToFileSystem(ScannedDocBO scannedDoc) {
		//should be getting directory from database config file		
		String root = "C:\\";
		String directory = "C:\\Directory1";
		File file = new File(directory);
		
		//check if directory exists
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			System.out.println("Directory already exists!");
		}
		Date date = new Date();
		String fileName = date.toString().replaceAll(" ", "").replaceAll(":", "") +".txt";
		System.err.println(" filename sa::" + directory + '\\' + fileName);
		
		String filePath = directory + '\\' + fileName;
		//check database
		File fileToWrite = new File(filePath);
		
		scannedDoc.setRelativePath(filePath);
		try {
			fileToWrite.createNewFile();
			if(isLinuxOS()) {
				
				byte [] myBytes = scannedDoc.getFileContents();
		        FileOutputStream out = new FileOutputStream(fileToWrite);
		        out.write(myBytes);
		        out.close();		         
		         
				setFileDetails(scannedDoc);
				checkNumberOffilesInCurrentDirectory(root);
				System.err.println(" is linux ");
			}
			
			if(isWindowsOS()) {
				
				byte [] myBytes = scannedDoc.getFileContents();
		        FileOutputStream out = new FileOutputStream(fileToWrite);
		        out.write(myBytes);
		        out.close();		       
				
		        setFileDetails(scannedDoc);
		        checkNumberOffilesInCurrentDirectory(root);
				System.err.println(" is windows ");
				
			}
			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scannedDoc;
		
	}
	
	public static void checkNumberOffilesInCurrentDirectory(String directory) {
		FolderUtil fUtil = new FolderUtil();
		try {
			fUtil.shellToFolderInfo(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FileInputStream getFileForUpload() {

		FileUtil fu = new FileUtil();
		File file = new File("C:\\Directory1");
		File fileToSave = fu.getFile("C:\\ticket.jpg");
		// fileToSave.set

		if (fileToSave == null) {
			System.err.println(" file not found");
		} else {

			try {
				fu.copyFileToDirectory(fileToSave, file);
				System.err.println("permissions" + fu.getFilePermissions());
				System.err.println("path" + fu.getUserDirectoryPath());
				fileToSave.setReadOnly();
				// fileToSave.
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println(" error occured");
			}
		}

		return null;

	}

	
	public static void setFileDetails(ScannedDocBO scannedDoc) throws IOException, ParseException {
		FileInfo file = new FileInfo(new File(scannedDoc.getRelativePath()));

		scannedDoc.setDocType("a");
		scannedDoc.setMimeType("a");
		
		System.out.println("Name: " + file.getName());
		
		System.out.println("Absolute path: " + file.getAbsolutePath());
		System.out.println("Size: " + file.getSize());
		System.out.println("Last modified: " + file.getLastModified());
		System.out.println("Owner: " + file.getOwner());
		System.out.println("Created: " + file.getCreated());
		System.out.println("Accessed: " + file.getAccessed());
		System.out.println("Written: " + file.getWritten());
	}

	
	public static void complexExample() throws IOException, ParseException {
		FileInfo file = new FileInfo(new File("C:\\ticket.jpg"));

		System.out.println("Name: " + file.getName());
		System.out.println("Absolute path: " + file.getAbsolutePath());
		System.out.println("Size: " + file.getSize());
		System.out.println("Last modified: " + file.getLastModified());
		System.out.println("Owner: " + file.getOwner());
		System.out.println("Created: " + file.getCreated());
		System.out.println("Accessed: " + file.getAccessed());
		System.out.println("Written: " + file.getWritten());
	}

	public static void getOSInfo() {

		System.err.println("** Operating System Info **");
		System.err.println();
		System.err.println("Name : " + System.getProperty("os.name"));
		System.err.println("Arch. : " + System.getProperty("os.arch"));
		System.err.println("Version : " + System.getProperty("os.version"));
		System.err.println("User Name : " + System.getProperty("user.name"));
		System.err.println("User Dir : " + System.getProperty("user.dir"));

	}

	public static String getSystemUser() {

		return System.getProperty("user.name");

	}

	public static String getUserDirectory() {

		return System.getProperty("user.dir");

	}

	public static String getOSName() {

		return System.getProperty("os.name");

	}

	public static boolean isLinuxOS() {
		if (StringUtils.contains(getOSName(), "Linux"))
			return true;

		return false;

	}

	public static boolean isWindowsOS() {
		if (StringUtils.contains(getOSName(), "Windows"))
			return true;

		return false;

	}

	public Integer saveToFile() {
		return null;

	}

	public Integer saveToDatabase() {
		return null;

	}

	public String parseFileDetails() {
		return null;

	}

	public String getFileDetailsfromDatabase() {
		return null;
	}

}
