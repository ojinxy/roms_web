package org.fsl.filemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.fsl.filemanager.exceptions.FM_GeneralException;

/**
 * 
 * @author jreid Created Date: Jul 17, 2013
 */
public class FolderUtil {

	// the number which is the suffix of the current folder
	static int currentFolderNo;
	static String currentFolderName;

	/**
	 * 
	 * @param rootDirectory
	 * @param folderName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Integer getFilesInFolderCount(String rootDirectory,
			String folderName) throws FileNotFoundException {

		if (StringUtils.isBlank(folderName)) {
			// System.err.println(" folder is empty");
			return 0;
		}

		String fullPath = rootDirectory + folderName;

		Path dir = Paths.get(fullPath);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
			throw new FileNotFoundException("File does not exist " + fullPath
					+ ".");
		}

		try (DirectoryStream<Path>

		ds = Files.newDirectoryStream(dir)) {

			// iterate over the content of the directory
			int count = 0;
			for (Path path : ds) {
				// System.err.println(path.getFileName() + "  " + count);
				count++;
			}

			return count;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new FileNotFoundException("File does not exist " + fullPath
					+ ".");
		}
	}

	/**
	 * 
	 * @param fullPath
	 * @return
	 * @throws FileNotFoundException
	 */
	public Integer getFilesInFolderCount(String fullPath)
			throws FileNotFoundException {

		Path dir = Paths.get(fullPath);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
			throw new FileNotFoundException("File does not exist " + fullPath
					+ ".");
		}

		try (DirectoryStream<Path>

		ds = Files.newDirectoryStream(dir)) {
			// iterate over the content of the directory
			int count = 0;
			for (Path path : ds) {
				// System.err.println(path.getFileName() + "  " + count);
				count++;
			}
			return count;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new FileNotFoundException("Error occurred.");
		}
	}

	/**
	 * 
	 * @param fullPathToFolder
	 * @param maxFolderSize
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean isFolderFull(String fullPathToFolder, Integer maxFolderSize)
			throws FileNotFoundException {

		Integer numberOfFilesIncurrentFolder = getFilesInFolderCount(fullPathToFolder);

		if (numberOfFilesIncurrentFolder != 0) {
			if (numberOfFilesIncurrentFolder == maxFolderSize
					|| numberOfFilesIncurrentFolder > maxFolderSize) {
				// System.err.println(fullPathToFolder + " Folder is full");
				return true;
			}
		}
		// System.err.println(fullPathToFolder + " Folder is not full ");
		return false;

	}

	/**
	 * 
	 * @return
	 * @throws FM_GeneralException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String createNewFolder(String rootDirectory,
			int currentFolderNo, String folderPrefix)
			throws FM_GeneralException, FileNotFoundException {

		String tempFolderName, tempCurrentFolderName;

		if (StringUtils.isBlank(folderPrefix)
				|| StringUtils.isBlank(rootDirectory))
			throw new FileNotFoundException(" This directory name is empty.");

		// if this is the first time running program
		// thus current file name is empty
		if (currentFolderNo == 0) {
			tempFolderName = rootDirectory + folderPrefix + "1";
			Path dir1 = Paths.get(tempFolderName);
			if (!Files.exists(dir1) || !Files.isDirectory(dir1)) {
				
				try {
					dir1 = Files.createDirectory(dir1);
				} catch (IOException e) {
					
					e.printStackTrace();
					throw new FM_GeneralException(
							"Directory cannot be created.");
				}
				tempCurrentFolderName = folderPrefix + "1";
				return tempCurrentFolderName;
			}
		}

		// increment the folder number
		currentFolderNo++;

		String newFolderName = folderPrefix + currentFolderNo;

		Path dir2 = Paths.get(rootDirectory + newFolderName);
		if (!Files.exists(dir2) || !Files.isDirectory(dir2)) {

			try {
				dir2 = Files.createDirectory(dir2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new FM_GeneralException("Directory cannot be created.");
			}

		}
		setCurrentFolderNo(currentFolderNo);
		setCurrentFolderName(newFolderName);
		return newFolderName;
	
	}

	/**
	 * 
	 * @param fullPathToFolder
	 * @param maxFolderSize
	 * @return
	 * @throws FileNotFoundException
	 * @throws FM_GeneralException
	 * @throws IOException
	 */
	public String createNewFolderOnFull(String fullPathToFolder,
			Integer maxFolderSize) throws FileNotFoundException,
			FM_GeneralException {

		boolean currentFolderFull = isFolderFull(fullPathToFolder,
				maxFolderSize);
		String currentFolderName = "";

		int currentFolderNo = getCurrentFolder(FileManager.rootDirectory,
				FileManager.folderPrefix);
		// if the current folder is full
		if (currentFolderFull) {
			// create a new folder
			currentFolderName = createNewFolder(FileManager.rootDirectory,
					currentFolderNo, FileManager.folderPrefix);
			setCurrentFolderName(currentFolderName);
		}

		return currentFolderName;
	}

	/**
	 * 
	 * @return
	 */
	public boolean setFolderPermissions() {
		return false;
	}

	public static int getCurrentFolder(String rootDirectory, String folderBase)
			throws FileNotFoundException {

		// obtains the Images directory in the app directory
		Path dir = Paths.get(rootDirectory);
	
		// Try with resources... so nice!
		try (DirectoryStream<Path>

		ds = Files.newDirectoryStream(dir, "*" + folderBase + "*")) {
			// iterate over the content of the directory
			int count = 0;
			
			Iterator<Path> pathIterator = ds.iterator();

			Path lastPath = null;
			int newestFolder = 1;
			while (pathIterator.hasNext()) {
				// Object element = pathIterator.next();
				lastPath = pathIterator.next();
			
				String patehName = lastPath.getFileName().toString();

				String currentNumberOfFolderString = patehName.replace(
						dir.toString(), "").replace(folderBase, "");

				int intCurrentFolder = Integer
						.parseInt(currentNumberOfFolderString);

				if (intCurrentFolder > newestFolder)
					newestFolder = intCurrentFolder;
			}

			return newestFolder;

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new FileNotFoundException("File does not exist " + folderBase
					+ ".");
		}
	}

	public static int getCurrentFolderNo() {
		return currentFolderNo;
	}

	public static void setCurrentFolderNo(int currentFolderNo) {
		FolderUtil.currentFolderNo = currentFolderNo;
	}

	public static String getCurrentFolderName() {
		return currentFolderName;
	}

	public static void setCurrentFolderName(String currentFolderName) {
		FolderUtil.currentFolderName = currentFolderName;
	}

}
