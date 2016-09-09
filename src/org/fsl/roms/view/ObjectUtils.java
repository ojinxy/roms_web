package org.fsl.roms.view;

/************
 * General Utility functions copied from SLB project
 * @author kpowell
 * 2014-10-29 * 
 * 
 */

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;



import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @Team Member: mmorgan
 * @Project: LMSEjb
 * @Created: Nov 21, 2007
 * 
 * @Purpose:
 */
public class ObjectUtils {
	
	/**
	 * @author hhill <br>
	 * <b>Purpose: </b> TODO purpose of ObjectUtils by hhill
	 */
	static final Logger log = Logger.getLogger("ObjectUtils");

	/**
	 * See ObjectUtils#objectToString
	 */
	public static String objectToString(Object source) {
		return objectToString(source, false, true);
	}

	/**
	 * 
	 * @param source
	 * @param showNull
	 * @return
	 */
	public static String objectToString(Object source, boolean showNull) {
		return objectToString(source, false, showNull);
	}

	/**
	 * @author stephenw
	 * 
	 * Takes an object and returns a String which may be used in a toString()
	 * implementation.
	 * 
	 * @param source -
	 *            Object to execute look-up on useClassName - Determines whether
	 *            the class name is echoed as a property of the class.
	 * @return String
	 */
	public static String objectToString(Object source, boolean useClassName,
			boolean showNull) {
	    
		StringBuffer result = new StringBuffer("");

		String propertyName = null;
		Object propertyValue = null;
		//int fieldCount = 0;

		try {
		    Map getters = BeanUtils.describe(source);
			Iterator keys = new TreeSet(getters.keySet()).iterator();

			while (keys.hasNext()) {
				propertyName = (String) keys.next();

				if (!propertyName.equals("class") || useClassName) {
					propertyValue = getters.get(propertyName);

					//int length = 0;
					//if (null != propertyValue)
					//	length = propertyValue.toString().length();

					if (showNull || null != propertyValue) {/*
						result.append(propertyName + ":\t" + propertyValue
								+ " length: " + length + "\n");*/
						result.append("\t" + propertyName + " = " + propertyValue + "\n");
					}
				}
			}
		} catch (Exception e){
		    e.printStackTrace();
		}

		return result.toString();
	}
	
	/**
	 * @author mmorgan
	 * @param propertyName
	 * @param object
	 * @return
	 */
	public static Object getProperty(String propertyName, Object object){
		
		
		String methodName = "get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
		
		//log.info("methodName="+methodName);

		Object propertyValue = null;
		
		try {
			Class clazz = object.getClass();
			propertyValue= clazz.getMethod(methodName, new Class[]{}).invoke(object, null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//log.info("propertyValue="+propertyValue);
		
		return propertyValue;
	}

	/**
	 * 
	 * @author mmorgan
	 * @param object
	 * @return
	 */
	public static String toTrimmedString(Object object){
	    return (object == null) ? "" : object.toString().trim(); 
	}
	
	/**
	 * @author mbwilliams
	 * @param object
	 * @return
	 */
	/*public static String toTrimmedString(Date date){
	    SimpleDateFormat df = new SimpleDateFormat(LMSDAOConstant.Date.FORMAT);
	    return (date == null) ? "" : df.format(date); 
	}*/
	

	/**
	 * See LmisUtils#objectToString
	 */
	public static void nullSafe(Object source) {
		nullSafe(source, false);
	}

	/**
	 * 
	 * Takes an object and returns a EMPTY String if the object property is NULL
	 * 
	 * @param source -
	 *            Object to execute look-up on useClassName - Determines whether
	 *            the class name is echoed as a property of the class.
	 * @return String
	 */
	public static void nullSafe(Object source, boolean useClassName) {

		Map getters = null;
		String propertyName = null;
		Object propertyValue = null;
		//int fieldCount = 0;

		try {
			getters = BeanUtils.describe(source);
			Iterator keys = getters.keySet().iterator();

			while (keys.hasNext()) {
				propertyName = (String) keys.next();

				if (!propertyName.equals("class") || useClassName) {
					propertyValue = getters.get(propertyName);

					if (propertyValue == null)
						propertyValue = StringUtils.EMPTY;
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param strings
	 * @return
	 */
	public static String ArrayToString(String[] strings) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			buffer.append("Pos[" + i + "] value = " + strings[i] + ",  ");
		}

		return buffer.toString();
	}

	/**
	 * @param changedItems
	 * @return
	 */
	public static boolean isEmpty(String[] changedItems) {
		if (changedItems == null)
			return true;
		else if (changedItems.length < 1)
			return true;
		else
			return false;
	}

	/**
	 * @param jutcLicenceTypes
	 * @return
	 */
	public static String convertToSeries(String[] types) {
		if (types == null)
			return null;
		StringBuffer series = new StringBuffer();
		for (int i = 0; i < types.length; i++) {
			series.append(types[i]);
			if (i != types.length - 1)
				series.append(",");
		}
		return series.toString();
	}

	/**
	 * Get the Collection values and append commas at the end
	 * 
	 * @param map
	 * @return
	 */
	public static String toCommaDelimited(Collection collection) {
		if (collection == null)
			throw new IllegalAccessError(
					"A collection is required for this method");

		StringBuffer buffer = new StringBuffer();

		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();

			if (iter.hasNext() == true)
				buffer.append(element + ",");
			else
				buffer.append(element);
		}

		return buffer.toString();
	}

	/**
	 * @param strings
	 * @return
	 */
	public static Collection createCollection(String[] strings) {

		if (strings == null)
			throw new IllegalAccessError("String[] cannot be null");

		Collection aCollection = new ArrayList(strings.length);

		for (int i = 0; i < strings.length; i++) {
			aCollection.add(strings[i]);
		}

		return aCollection;
	}

	/**
	 * Return an obj as a string <b>uses <code>toString()</code> </b> <br>
	 * If null return BLANK string
	 * 
	 * @param obj
	 * @return
	 */
	public static String thisToString(Object obj) {
		return null != obj ? obj.toString() : "";
	}
	
	/**
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @author mmorgan<br>
	 */
	public static void copyProperties(Object src, Object dest) 
			throws IllegalAccessException, InvocationTargetException {
		Date defaultDate = null;
		DateConverter converter = new DateConverter(defaultDate);
		ConvertUtils.register(converter, Date.class);
		BeanUtils.copyProperties(dest, src);
	}
	
	/**
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @author mmorgan<br>
	 */
	public static void copyProperty(Object src, String property, Object dest) 
			throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperty(dest, property, src);
	}

}