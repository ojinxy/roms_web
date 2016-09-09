package org.fsl.toms.scanneddoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

public class FolderUtil {

	public Integer getFolderCount() {
		return null;
	}

	public boolean setFolderPermissions() {
		return false;
	}

	public String getFolderPermissions() {
		return null;
	}

	public boolean createFolder() {
		return false;

	}

	public Integer getFilesInFolderCount() {
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean createNewFolder() {
		return false;
		
	}

	static void shellToFolderInfo(String directory) throws IOException,
			ParseException {
		Runtime systemShell = Runtime.getRuntime();
		Process output = systemShell.exec(String.format("cmd /c dir ",
				directory));
		System.err.println(" Directory ::" + directory);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				output.getInputStream()));
		String outputLine = null;
		while ((outputLine = reader.readLine()) != null) {
			if (outputLine.contains("Dir(s)")) {
				System.err.println(" The number fo folders::" + outputLine);//.substring(12, 17));

			}
		}
	}

}
