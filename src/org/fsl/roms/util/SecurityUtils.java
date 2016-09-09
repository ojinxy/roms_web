package org.fsl.roms.util;

import java.lang.reflect.InvocationTargetException;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class SecurityUtils {

	public static String objectToString(Object source) {
		return objectToString(source, false);
	}

	@SuppressWarnings("unchecked")
	public static String objectToString(Object source, boolean useClassName) {
		StringBuffer result = new StringBuffer("");

		Map getters = null;
		String propertyName = null;
		Object propertyValue = null;
		@SuppressWarnings("unused")
		int fieldCount = 0;

		try {
			getters = BeanUtils.describe(source);
			Iterator keys = getters.keySet().iterator();

			while (keys.hasNext()) {
				propertyName = (String) keys.next();

				if (!propertyName.equals("class") || useClassName) {
					propertyValue = getters.get(propertyName);

					result.append(propertyName + ":\t" + propertyValue + "\n");
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	public static boolean isEmailValid(String email) {

		final String sp = "\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~";
		final String atext = "[a-zA-Z0-9" + sp + "]";
		final String atom = atext + "+";
		final String dotAtom = "\\." + atom;
		final String localPart = atom + "(" + dotAtom + ")*";
		final String letter = "[a-zA-Z]";
		final String letDig = "[a-zA-Z0-9]";
		final String letDigHyp = "[a-zA-Z0-9-]";
		final String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" + letDig
				+ ")?";
		final String domain = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter
				+ "{2,6}";

		final String addrSpec = "^" + localPart + "@" + domain + "$";

		CharBuffer buffer = CharBuffer.wrap(email.toCharArray());

		return Pattern.matches(addrSpec, buffer);

	}

	/**
	 * # Start of group (?=.*\d) # must contains one digit from 0-9 (?=.*[a-z])
	 * # must contains one lowercase characters (?=.*[A-Z]) # must contains one
	 * uppercase characters (?=.*[@#$%]) # must contains one special symbols in
	 * the list "@#$%" . # match anything with previous condition checking
	 * {6,20} # length at least 6 characters and maximum of 20 # End of group
	 ***/

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public static boolean validatePassword(final String password) {
		final Pattern pattern;
		final Matcher matcher;
		final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

		pattern = Pattern.compile(PASSWORD_PATTERN);

		matcher = pattern.matcher(password);
		return matcher.matches();

	}

	/**
	 * # Start of group (?=.*\d) # must contains one digit from 0-9 (?=.*[a-z])
	 * # must contains one lowercase characters (?=.*[A-Z]) # must contains one
	 * uppercase characters (?=.*[@#$%]) # must contains one special symbols in
	 * the list "@#$%" . # match anything with previous condition checking
	 * {6,20} # length at least 6 characters and maximum of 20 # End of group
	 ***/

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public static boolean validateSimplePassword(final String password) {
		// final Pattern pattern;
		// final Matcher matcher;
		// final String PASSWORD_PATTERN = "({5,25})";

		// pattern = Pattern.compile(PASSWORD_PATTERN);
		if (StringUtils.isNotBlank(password) && password.length() > 5)
			return true;

		return false;
	}

}
