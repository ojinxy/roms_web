package fsl.ta.toms.roms.util;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class EmailAddressUtil {

	public boolean validateEmailAddress( String userEnteredEmailString ) 
	{
		
		//Regular Expressions for Validating Email Addresses
		String sp = "\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~";
		String atext = "[a-zA-Z0-9" + sp + "]";
		String atom = atext + "+"; //one or more atext chars
		String dotAtom = "\\." + atom;
		String localPart = atom + "(" + dotAtom + ")*"; //one atom followed by 0 or more dotAtoms.
		String letter = "[a-zA-Z]";
		String letDig = "[a-zA-Z0-9]";
		String letDigHyp = "[a-zA-Z0-9-]";
		String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" +	letDig + ")?";
		String domain = rfcLabel + "(\\." + rfcLabel + ")*\\."	+ letter + "{2,6}";
		
		//Combined together, these form the allowed email regexp allowed by RFC 2822:
		String addrSpec = "^" + localPart + "@" + domain +	"$";
		
		Pattern VALID_PATTERN1 = Pattern.compile( addrSpec );
		
		CharBuffer buffer = CharBuffer.wrap(userEnteredEmailString.toCharArray());
		return Pattern.matches(addrSpec,buffer);
	}
		
}
