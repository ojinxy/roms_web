package org.fsl.converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

/**
 * @author jreid
 */
public class MoneyConverter extends javax.faces.convert.BigDecimalConverter {

	public MoneyConverter() {

		super();
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {

		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			value = StringUtils.remove(value, ',');
			System.err.println("removing commas");
			return super.getAsObject(context, component, value);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (context == null || component == null) {
			throw new NullPointerException();
		}

		// If the specified value is null, return a zero-length String
		if (value == null) {
			return "";
		}
		System.err.println(" printing string");
		// If the incoming value is still a string, play nice
		// and return the value unmodified
		if (value instanceof String) {
			return (String) value;
		}

		try {
			DecimalFormat myFormatter = new DecimalFormat("###,###.##");
			String output = myFormatter.format(value);
			// NumberFormatter nf = new NumberFormatter("###,###.##");
			return output;
		} catch (Exception e) {
			// throw new ConverterException(MessageFactory.getMessage(context,
			// STRING_ID, value, MessageFactory.getLabel(context, component)),
			// e);

		}
		return "";
	}

}
