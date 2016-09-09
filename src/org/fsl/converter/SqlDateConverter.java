package org.fsl.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.util.DateUtils;

/**
 * 
 * @author jreid
 * 
 */
public class SqlDateConverter implements Converter, StateHolder {

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	 public static final String CONVERTER_ID = "date";

	public SqlDateConverter() {
		super();
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		java.util.Date uDate = null;
		java.util.Date sDate = null;

		if (StringUtils.isBlank(value)) {
			return null;
		}

		if (DateUtils.isDateValid(value.trim()) == true) {
			try {
				uDate = sdf.parse(value);
			} catch (ParseException ex) {

				FacesMessage message = new FacesMessage();
				String fieldName = (String) component.getAttributes()
						.get("alt");

				message.setDetail(fieldName + " Date is invalid.");
				message.setSummary(fieldName + " Date is invalid.");

				message.setSeverity(FacesMessage.SEVERITY_ERROR);

				throw new ConverterException(message);
			}

			if (uDate != null) {
				sDate = new java.sql.Date(uDate.getTime());
			}

			return sDate;
		} else {

			FacesMessage message = new FacesMessage();
			String fieldName = (String) component.getAttributes().get("alt");

			message.setDetail(fieldName + " Date is invalid.");
			message.setSummary(fieldName + " Date is invalid.");

			message.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ConverterException(message);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		// TODO Auto-generated method stub

		if (value == null) {
			return "";
		}
		// return value.toString();

		return sdf.format(value);
	}

	@Override
	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void restoreState(FacesContext arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object saveState(FacesContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransient(boolean arg0) {
		// TODO Auto-generated method stub

	}
}
