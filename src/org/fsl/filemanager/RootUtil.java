package org.fsl.filemanager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

import org.fsl.filemanager.exceptions.FM_GeneralException;

/**
 * 
 * @author jreid Created Date: Jul 16, 2013
 */
public class RootUtil {

	public static String rootDirectory;
	boolean over80PercentWarningEmailSent = false;
	boolean over90PercentWarningEmailSent = false;

	public RootUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param rootDirectory
	 */
	public RootUtil(String rootDirectory) {
		super();
		this.rootDirectory = rootDirectory;
	}

	/**
	 * 
	 * @param rootDirectory
	 * @return
	 */
	public static boolean rootPathExists(String rootDirectory) {
		// obtains the root directory
		Path dir = Paths.get(rootDirectory);

		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
			return false;
		}
		// System.err.println("Directory already exists!");

		return true;

	}

	/**
	 * 
	 * @param rootDirectory
	 * @return
	 * @throws FM_GeneralException
	 */
	public static long checkRootDirectorySize(String rootDirectory)
			throws FM_GeneralException {
		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}

		try {
			FileStore fileStore = Files.getFileStore(dir);
			long sizeOfdirectory = fileStore.getTotalSpace();

			// System.err.println("usable:" + fileStore.getUsableSpace());
			// System.err.println("free:" + fileStore.getUnallocatedSpace());
			// System.err.println("total:" + fileStore.getTotalSpace());

			return sizeOfdirectory;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_GeneralException();
		}

	}

	/**
	 * 
	 * @param rootDirectory
	 * @return
	 * @throws FM_GeneralException
	 */
	public static long checkRootDirectoryUsedSpace(String rootDirectory)
			throws FM_GeneralException {
		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}

		FileStore fileStore;
		try {
			fileStore = Files.getFileStore(dir);
			long usedSpace = fileStore.getTotalSpace()
					- fileStore.getUnallocatedSpace();

			return usedSpace;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_GeneralException();
		}

		// return 0;
	}

	/**
	 * @param rootDirectory
	 * @return
	 * @throws FM_GeneralException
	 */
	public static long checkRootDirectoryAvailableSpace(String rootDirectory)
			throws FM_GeneralException {
		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}
		FileStore fileStore;
		try {
			fileStore = Files.getFileStore(dir);
			long availableSpace = fileStore.getUnallocatedSpace();

			return availableSpace;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_GeneralException();
		}

		// return 0;
	}

	/**
	 * Shows how much free space is available on disk
	 * 
	 * @param rootDirectory
	 * @return
	 * @throws FM_GeneralException
	 */
	public static long checkRootDirectoryUsageRatio(String rootDirectory)
			throws FM_GeneralException {
		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}
		FileStore fileStore;
		try {
			fileStore = Files.getFileStore(dir);

			long usageRatio = (fileStore.getTotalSpace() - fileStore
					.getUnallocatedSpace()) / fileStore.getTotalSpace();
			// System.err.println(" used: " + ((fileStore.getTotalSpace() -
			// fileStore.getUnallocatedSpace()) / fileStore.getTotalSpace()));
			return usageRatio;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_GeneralException();
		}
		// return 0;
	}

	/**
	 * 
	 * @param rootDirectory
	 * @return
	 * @throws FM_GeneralException
	 */
	public static int countFilesInRootDirectory(String rootDirectory)
			throws FM_GeneralException {

		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}

		// Try with resources... so nice!
		try (DirectoryStream<Path>

		ds = Files.newDirectoryStream(dir)) {

			// iterate over the content of the directory
			int count = 0;// dir.getFileSystem().;
			for (Path path : ds) {
				// System.err.println(path.getFileName() + "  " + count);
				count++;
			}

			// System.err.printf("%d Files match the pattern", count);
			return count;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new FM_GeneralException();
		}
		// return 0;
	}

	/**
	 * 
	 * @param rootDirectory
	 * @param pattern
	 * @return
	 * @throws FM_GeneralException
	 */
	public static int retrieveFilesWithNamePatternFromRootDirectory(
			String rootDirectory, String[] patterns) throws FM_GeneralException {
		// obtains the Images directory in the app directory
		Path dir = Paths.get(rootDirectory);
		// the Files class offers methods for validation
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			// System.err.println("No such directory!");
		}

		// validate at least the glob pattern

		if (patterns == null || patterns.length < 1) {

			// System.err.println( "Please provide at least the glob pattern.");

			return 0;

		}

		// obtain the objects that implements PathMatcher
		PathMatcher extraFilterOne = null;
		PathMatcher extraFilterTwo = null;
		if (patterns.length > 1 && patterns[1] != null) {
			extraFilterOne = FileSystems.getDefault().getPathMatcher(
					patterns[1]);
		}
		if (patterns.length > 2 && patterns[2] != null) {
			extraFilterTwo = FileSystems.getDefault().getPathMatcher(
					patterns[2]);
		}

		// Try with resources... so nice!
		try (DirectoryStream<Path>

		ds = Files.newDirectoryStream(dir, patterns[0])) {
			// iterate over the content of the directory and apply
			// any other extra pattern
			int count = 0;
			for (Path path : ds) {
				// System.out.println("Evaluating " + path.getFileName());

				if (extraFilterOne != null
						&& extraFilterOne.matches(path.getFileName())) {
					// System.out.println("Match found Do something!");
				}

				if (extraFilterTwo != null
						&& extraFilterTwo.matches(path.getFileName())) {
					// System.out.println("Match found Do something else!");
				}

				count++;
			}
			// System.out.println();
			// System.out.printf("%d Files match the global pattern\n", count);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new FM_GeneralException();
		}
		return 0;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

}
