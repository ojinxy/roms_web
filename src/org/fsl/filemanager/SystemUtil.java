package org.fsl.filemanager;

import org.apache.commons.lang.StringUtils;

/**
 * Has functionality related to the system
 * @author jreid
 * Created Date: Jul 17, 2013
 */
public class SystemUtil {

	private static String OS_NAME = "os.name";
	private static String OS_ARCHITECTURE = "os.arch";
	private static String OS_VERSION = "os.version";
	private static String OS_USER_NAME = "user.name";
	private static String OS_USER_DIRECTORY = "user.dir";
	private static String LINUX = "linux";
	private static String WINDOWS = "windows";
	
	private static String WINDOWS_SLASH ="\\";
	private static String LINUX_SLASH ="/";
	

	
	public static void printOSInfo() {
		System.err.println("** Operating System Info **");
		System.err.println("Name : " + System.getProperty(OS_NAME));
		System.err.println("Arch. : " + System.getProperty(OS_ARCHITECTURE));
		System.err.println("Version : " + System.getProperty(OS_VERSION));
		System.err.println("User Name : " + System.getProperty(OS_USER_NAME));
		System.err.println("User Dir : "
				+ System.getProperty(OS_USER_DIRECTORY));
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	 public static String formatPathForOS(String path) {
		 String newPath = null;
		 if(isLinuxOS()) {
			 newPath = path.replace(WINDOWS_SLASH, LINUX_SLASH);
			// newPath = path.replace(", LINUX_SLASH);
		 }
		 
		 if(isWindowsOS()) {
			 newPath = path.replace(LINUX_SLASH, WINDOWS_SLASH);
		 }
			 
		 return newPath;
	 }

	 /**
		 * 
		 * @param path
		 * @return
		 */
		 public static String formatPathForOS(String rootDirectory, String currentFolder, String fileName) {
			
			 String newPath = formatPathForOS(rootDirectory);
			 
			 if(isLinuxOS()) {
				 newPath = newPath +  currentFolder +LINUX_SLASH + fileName;
			 }
			 
			 if(isWindowsOS()) {
				 newPath = newPath +  currentFolder +WINDOWS_SLASH + fileName;
			 }
				 
			 return newPath;
		 }
	 
	/**
	 * 
	 * @return
	 */
	public static String getSystemUser() {

		return System.getProperty(OS_USER_NAME);
	}

	/**
	 * 
	 * @return
	 */
	public static String getUserDirectory() {

		return System.getProperty(OS_USER_DIRECTORY);
	}

	/**
	 * 
	 * @return
	 */
	public static String getOSName() {

		return System.getProperty(OS_NAME);
	}

	/**
	 * 
	 * @return
	 */
	public static String getOSArchitecture() {

		return System.getProperty(OS_ARCHITECTURE);
	}

	/**
	 * 
	 * @return
	 */
	public static String getOSVersion() {

		return System.getProperty(OS_VERSION);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isLinuxOS() {
		if (StringUtils.containsIgnoreCase(getOSName(), LINUX))
			return true;

		return false;
	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isWindowsOS() {
		if (StringUtils.containsIgnoreCase(getOSName(), WINDOWS))
			return true;

		return false;
	}

	
}
