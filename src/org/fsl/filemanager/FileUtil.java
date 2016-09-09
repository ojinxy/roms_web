package org.fsl.filemanager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.fsl.filemanager.exceptions.FM_ErrorRetrievingFileException;
import org.fsl.filemanager.exceptions.FM_ErrorSavingFileException;

/**
 * 
 * @author jreid Created Date: Jul 29, 2013
 */
public class FileUtil {

	/**
	 * 
	 * @param rootDirectory
	 * @param currentFolder
	 * @param fileName
	 * @return
	 * @throws FM_ErrorSavingFileException
	 * @throws IOException
	 */
	public String createFile(String rootDirectory, String currentFolder,
			String fileName, byte[] buffer) throws FM_ErrorSavingFileException {

		Path target = Paths.get(SystemUtil.formatPathForOS(rootDirectory,
				currentFolder, fileName));
		try {
			if (!Files.exists(target)) {
				// System.err.println("creating file!");
				Path file;

				file = Files.createFile(target);

				Files.write(file, buffer);
			} else {
				// System.err.println("Creating a new file name instead");
				// String newFileName = RandomStringGenerator.randomString();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorSavingFileException();
		}
		return target.toString();
	}

	/**
	 * 
	 * @param absoluteFileName
	 * @return
	 * @throws IOException
	 * @throws FM_ErrorSavingFileException
	 */
	public static String createFile(String absoluteFileName, byte[] buffer)
			throws FM_ErrorSavingFileException {

		// System.err.println("absoluteFileName" + absoluteFileName);
		Path target = Paths.get(absoluteFileName);

		try {
			if (!Files.exists(target)) {
				// System.err.println("creating  file!" + target);
				Path file = Files.createFile(target);

				Files.write(file, buffer);
			} else {
				// System.err.println("Creating a new file name instead");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorSavingFileException();
		}

		return target.toString();
	}

	/**
	 * 
	 * @param absoluteFileName
	 * @return
	 * @throws IOException
	 * @throws FM_ErrorSavingFileException
	 * @throws FM_ErrorRetrievingFileException
	 */
	public byte[] retrieveFileAsByteArray(String absoluteFileName)
			throws FM_ErrorRetrievingFileException {

		Path target = Paths.get(SystemUtil.formatPathForOS(absoluteFileName));
		byte[] fileArray;

		try {
			if (!Files.exists(target) || Files.isDirectory(target)) {
				// System.err.println("No such file!" + target);
				throw new FileNotFoundException("This file does nto exist "
						+ target + ".");

			} else {

				fileArray = Files.readAllBytes(target);
				// System.err.println(" retrieve file contents");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorRetrievingFileException();
		}

		return fileArray;
	}

	/**
	 * 
	 * @param rootDirectory
	 * @param currentFolder
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws FM_ErrorRetrievingFileException
	 */
	public byte[] retrieveFileAsByteArray(String rootDirectory,
			String currentFolder, String fileName)
			throws FM_ErrorRetrievingFileException {

		try {
			Path target = Paths.get(SystemUtil.formatPathForOS(rootDirectory,
					currentFolder, fileName));
			if (!Files.exists(target) || Files.isDirectory(target)) {
				// System.err.println("No such file!");
				// Path file = Files.createFile(target);
			} else {
				byte[] fileArray;
				fileArray = Files.readAllBytes(target);
				// System.err.println(" retrieve file contents");
				return fileArray;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorRetrievingFileException();
		}

		return null;
	}

	/**
	 * 
	 * @param absoluteFileName
	 * @return
	 * @throws IOException
	 * @throws FM_ErrorRetrievingFileException
	 */
	public byte[] getFileWithExtension(String absoluteFileName)
			throws FM_ErrorRetrievingFileException {

		Path target = Paths.get(SystemUtil.formatPathForOS(absoluteFileName));// absoluteFileName);

		try {
			if (!Files.exists(target) || Files.isDirectory(target)) {
				// System.err.println("No such file!" + target);

			} else {

				String fileType = Files.probeContentType(target);

				// System.err.println(" File type is ::" + fileType);

				FileInputStream fis = new FileInputStream(target.toFile());
				// create FileInputStream which obtains input bytes from a file
				// in a
				// file system
				// FileInputStream is meant for reading streams of raw bytes
				// such as
				// image data. For reading streams of characters, consider using
				// FileReader.

				// InputStream in = resource.openStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				try {
					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						bos.write(buf, 0, readNum);
						// no doubt here is 0

						// System.out.println("read " + readNum + " bytes,");
					}
				} catch (IOException ex) {
					Logger.getLogger(FileUtil.class.getName()).log(Level.FATAL,
							null, ex);
				}
				byte[] bytes = bos.toByteArray();

				return bytes;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorRetrievingFileException();
		}
		return null;

	}

	/**
	 * 
	 * @param absoluteFileName
	 * @return
	 * @throws FM_ErrorRetrievingFileException
	 * @throws IOException
	 */
	public String getFileWithExtension(String rootDirectory,
			String currentFolder, String fileName)
			throws FM_ErrorRetrievingFileException {

		try {
			Path target = Paths.get(SystemUtil.formatPathForOS(rootDirectory,
					currentFolder, fileName));

			if (!Files.exists(target) || Files.isDirectory(target)) {
				// System.err.println("No such file!" + target);

			} else {

				String fileType = Files.probeContentType(target);

				// System.err.println(" File type is ::" + fileType);
				return fileType;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FM_ErrorRetrievingFileException();
		}
		return null;

	}

	/**
	 * 
	 * @param rootDirectory
	 * @return
	 */
	public static boolean fileExists(String filePath) {
		//System.err.println(" file path :" + filePath);
		// obtains the root directory
		Path dir = Paths.get(filePath);

		// the Files class offers methods for validation
		if (!Files.exists(dir) || Files.isDirectory(dir)) {
			// System.err.println("No such file " + dir.toString() );
			return false;
		}
		// System.err.println("file already exists!");

		return true;

	}

}
