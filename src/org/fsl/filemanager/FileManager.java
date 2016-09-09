/**
 * Created By: jreid
 * Date: Jul 15, 2013
 *
 */
package org.fsl.filemanager;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.fsl.filemanager.exceptions.FM_EmailException;
import org.fsl.filemanager.exceptions.FM_ErrorSavingFileException;
import org.fsl.filemanager.exceptions.FM_GeneralException;
import org.fsl.filemanager.exceptions.FM_RootDirectoryException;
import org.fsl.utility.RandomStringGenerator;

import fsl.ta.toms.roms.constants.Constants;

/**
 * @author jreid Created Date: Jul 15, 2013
 */
public class FileManager {

	static String currentFolder;
	static String rootDirectory;
	static String folderPrefix;
	static Integer maxFileSize;
	static Integer maxFilesInFolder;
	static Integer folderFullPercentage;

	// static String emailList;

	/**
	 * constructor with no inputs
	 */
	public FileManager() {
		super();
		try {
			rootDirectory = getRootDirectoryConfig();
			maxFilesInFolder = getMaxFolderSizeConfig();
			folderPrefix = getFolderPrefixConfig();
			maxFileSize = getMaxFileUploadSizeConfig();
			folderFullPercentage = getRootFullPercentageConfig();
		} catch (FM_GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param rootDirectory
	 * @param currentFolder
	 */
	public FileManager(String rootDirectory, String folderPrefix,
			Integer maxFilesInFolder, Integer maxFileSize,
			Integer folderFullPercentage) {
		super();
		FileManager.rootDirectory = rootDirectory;
		FileManager.maxFilesInFolder = maxFilesInFolder;
		FileManager.maxFileSize = maxFileSize;
		FileManager.folderPrefix = folderPrefix;
		FileManager.folderFullPercentage = folderFullPercentage;
		// FileManager.emailList = emailList;

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String saveFileToFileSystem(byte[] bytes) throws Exception {

		// should be getting directory from database config file
		// RootUtil root = new RootUtil();
		// FolderUtil folder = new FolderUtil();
		String newFileName = RandomStringGenerator.randomString();
		String fullyQualifiedFileName = null;

		try {

			// check if root path exists
			if (RootUtil.rootPathExists(rootDirectory)) {
				int currentFolderNo = FolderUtil.getCurrentFolder(
						rootDirectory, folderPrefix);
				String currentFolder = folderPrefix + currentFolderNo;

				// check that the system is not near capacity
				//TO TEST EMAIL FUNCTION - hard code this value
				long usageRatio =  RootUtil.checkRootDirectoryUsageRatio(rootDirectory);
				
				// the current folder exists
				if (RootUtil.rootPathExists(SystemUtil
						.formatPathForOS(rootDirectory + "\\" + currentFolder))) {

					Integer filescount = FolderUtil.getFilesInFolderCount(
							rootDirectory, currentFolder);
					Integer maxFiles = null;

					// System.err.println(" usage ratio :" + usageRatio);
					// usage of file system is 80% or above
					// EmailUtil emailUtil = new EmailUtil();
					// if above 90% urgent warning
					if (usageRatio >= 90) {
						EmailUtil.send90PercentUsageWarningMail(rootDirectory);
						System.err.println("usage above 90%");
						// return null;

					} else // if above 80% and not above 90 warning
					if (usageRatio >= 80) {
						System.err.println(" usage above 80%");
						EmailUtil.send80PercentUsageWarningMail(rootDirectory);
						// return null;

					} else {
						System.err.println(" File limit is good!");
					}

					// if the ratio is less than 80% used then continue

					if (StringUtils.isNotBlank(maxFilesInFolder + "")) {

						maxFiles = Integer.valueOf(maxFilesInFolder);

						if (filescount < maxFiles) {

							// System.err.println("file count ok: " +
							// filescount);

							// if the file count is ok then save the file
							// FileUtil fileUtils = new FileUtil();

							fullyQualifiedFileName = FileUtil.createFile(
									SystemUtil.formatPathForOS(rootDirectory,
											currentFolder, newFileName), bytes);

						} else {
							String pathToFolder;

							pathToFolder = FolderUtil.createNewFolder(
									rootDirectory, currentFolderNo,
									folderPrefix);
							currentFolder = pathToFolder;
							// System.err.println("File count too high. Need new folder."
							// + pathToFolder);
							// FileUtil fileUtils = new FileUtil();

							fullyQualifiedFileName = FileUtil.createFile(
									SystemUtil.formatPathForOS(rootDirectory,
											currentFolder, newFileName), bytes);

						}
					}

				} else {
					// System.err.println(" The current folder is not yet created");
					// create folder
					currentFolder = FolderUtil.createNewFolder(rootDirectory,
							currentFolderNo, folderPrefix);

					// create file if the file count is ok then save the file
					FileUtil fileUtils = new FileUtil();

					fullyQualifiedFileName = fileUtils.createFile(SystemUtil
							.formatPathForOS(rootDirectory, currentFolder,
									newFileName), bytes);

				}

			} else {

				// System.err.println(rootDirectory +
				// " this drive is not mapped");
				throw new FM_RootDirectoryException(rootDirectory);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new FM_ErrorSavingFileException(e.getMessage());

		}

		return fullyQualifiedFileName;
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileFromFileSystem(String path) throws Exception {

		FileUtil file = new FileUtil();
		byte[] bytes = null;

		// check if root path exists
		if (FileUtil.fileExists(path)) {

			bytes = file.retrieveFileAsByteArray(path);

		} else {
			// System.err.println(" This file was not found " + path);
		}

		return bytes;
	}

	public String getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(String currentFolder) {
		FileManager.currentFolder = currentFolder;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		FileManager.rootDirectory = rootDirectory;
	}

	public static String getFolderPrefix() {
		return folderPrefix;
	}

	public static void setFolderPrefix(String folderPrefix) {
		FileManager.folderPrefix = folderPrefix;
	}

	public static Integer getFolderFullPercentage() {
		return folderFullPercentage;
	}

	public static void setFolderFullPercentage(Integer folderFullPercentage) {
		FileManager.folderFullPercentage = folderFullPercentage;
	}

	public Integer getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Integer maxFileSize) {
		FileManager.maxFileSize = maxFileSize;
	}

	public Integer getMaxFilesInFolder() {
		return maxFilesInFolder;
	}

	public void setMaxFilesInFolder(Integer maxFilesInFolder) {
		FileManager.maxFilesInFolder = maxFilesInFolder;
	}

	// ************************ GET PARAMETERS
	// ***************************************//

	/**
	 * Gets the configuration for the root folder from the database default :
	 * /home/wsdev/ROMS_FILES/
	 * 
	 * @return
	 * @throws FM_GeneralException
	 */
	private String getRootDirectoryConfig() throws FM_GeneralException {

		Context ctx;
		String value = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_ROOT_DIRECTORY);
			value = StringUtils.trimToNull(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_ROOT address was not configured in web.xml");
		}

		return value;

	}

	/**
	 * Gets the configuration for the max folder size from the database
	 * 
	 * @return
	 * @throws FM_GeneralException
	 */
	private Integer getMaxFolderSizeConfig() throws FM_GeneralException {

		Context ctx;
		String value = null;
		Integer numberValue = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_MAX_FOLDER_SIZE);
			value = StringUtils.trimToNull(value);
			numberValue = Integer.valueOf(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_MAX_FLDR address was not configured in web.xml");
		}

		return numberValue;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws FM_GeneralException
	 */
	private Integer getRootFullPercentageConfig() throws FM_GeneralException {

		Context ctx;
		String value = null;
		Integer numberValue = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_ROOT_FULL_PERCENT);
			value = StringUtils.trimToNull(value);
			numberValue = Integer.valueOf(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_FULL_PCNT address was not configured in web.xml");
		}

		return numberValue;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws FM_GeneralException
	 */
	private Integer getRootAlmostFullPercentageConfig()
			throws FM_GeneralException {

		Context ctx;
		String value = null;
		Integer numberValue = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_ROOT_NOTIFICATION_PERCENT);
			value = StringUtils.trimToNull(value);
			numberValue = Integer.valueOf(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_NOTI_PCNT address was not configured in web.xml");
		}

		return numberValue;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws FM_EmailException
	 */
	private String getRootSpaceNotificationEmailConfig()
			throws FM_EmailException {

		Context ctx;
		String value = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_NOTIFICATION_EMAIL);
			value = StringUtils.trimToNull(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_EmailException(
					"FM_EMAIL address was not configured in web.xml");
		}

		return value;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws FM_EmailException
	 */
	private Integer getMaxFileUploadSizeConfig() throws FM_GeneralException {

		Context ctx;
		String value = null;
		Integer numberValue = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_MAX_FILE_SIZE);
			value = StringUtils.trimToNull(value);
			numberValue = Integer.valueOf(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_MAX_FILE address was not configured in web.xml");
		}

		return numberValue;

	}

	/**
	 * Gets the configuration for the min space available
	 * 
	 * @return
	 * @throws FM_GeneralException
	 */
	private String getFolderPrefixConfig() throws FM_GeneralException {

		Context ctx;
		String value = null;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			value = (String) env
					.lookup(Constants.FileManagerConfig.FILE_MANAGER_FOLDER_PREFIX);
			value = StringUtils.trimToNull(value);

		} catch (Exception exc) {
			exc.printStackTrace();
			throw new FM_GeneralException(
					"FM_FLDR_PRE address was not configured in web.xml");
		}

		return value;

	}

}
