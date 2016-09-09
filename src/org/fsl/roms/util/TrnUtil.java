package org.fsl.roms.util;

/**
 * @author jreid
 * @date 2012-02-1
 **/

public class TrnUtil {

	public static String formatTrn(Integer trn) {

		if (trn == null)
			return "000000000";

		int trnInt = trn.intValue();

		String trnPadded = zeroPad(trnInt, 9);

		return trnPadded;
	}

	public static String formatTrnBranch(Integer trnBranch) {
		if (trnBranch == null)
			return "0000";

		int trnInt = trnBranch.intValue();

		String trnBranchPadded = zeroPad(trnInt, 4);

		return trnBranchPadded;
	}

	/**
	 * Puts zeros at the beginning of a string if string length is less than min
	 * length.
	 */
	public static String zeroPad(int number, int minLength) {
		String s = number + "";

		int numberOfZerosNeeded = minLength - s.length();

		StringBuilder extraZeros = new StringBuilder();

		for (int i = 0; i < numberOfZerosNeeded; i++) {
			extraZeros.append("0");
		}
		return extraZeros + s;
	}

	/*
	 * public static String addStringSeparator(String s, int maxLength, String
	 * separator) { StringBuilder finalTrn = new StringBuilder(); String copy =
	 * s;
	 * 
	 * int startIndex = 0; //int target = startIndex + maxLength;
	 * while(startIndex < (s.length() - 1)) {
	 * finalTrn.append(copy.substring(startIndex, (maxLength + startIndex)) +
	 * separator); startIndex = startIndex + maxLength; copy = s; }
	 * 
	 * System.err.println(" final trn : " + finalTrn.toString()); return
	 * finalTrn.toString(); }
	 */

	public static String addStringSeparator(String s, int maxLength,
			String separator) {
		StringBuilder finalTrn = new StringBuilder();
		String copy = s;

		int startIndex = 0;
		// int target = startIndex + maxLength;
		while (startIndex < (s.length() - 1)) {
			finalTrn.append(copy
					.substring(startIndex, (maxLength + startIndex)));

			startIndex = startIndex + maxLength;

			if (startIndex < (s.length() - 1))
				finalTrn.append(separator);

			copy = s;
		}

		// System.err.println(" final trn : " + finalTrn.toString());
		return finalTrn.toString();
	}

	public static String addHypenSeparator(String string) {

		return addStringSeparator(string, 3, "-");
	}

	public static String addSpaceSeparator(String string) {
		return addStringSeparator(string, 3, " ");
	}

}
