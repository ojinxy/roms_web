package org.fsl.roms.spring.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author othomas
 * @date May 6, 2011
 **/

public class ROMSContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//set this variable on the server at application run time to ensure that NULL faces text box values arent defaulted to a value
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
		
		//initApplicationTempDirectory();
	}

	/*private void initApplicationTempDirectory() {

		try {
			File file = new File(FileUtils.TMP_DIR);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			System.out
					.println("There was a problem creating temporary directory ["
							+ FileUtils.TMP_DIR + "]: " + e.getMessage());
		}

	}*/

}
