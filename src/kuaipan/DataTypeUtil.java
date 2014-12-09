package kuaipan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataTypeUtil {

	public static Long convert2Long(Object obj) {
		long ret = 0;
		if (obj != null) {
			if (obj instanceof Number) {
				ret = ((Number) obj).longValue();
			} else if (obj instanceof String) {
				ret = Long.parseLong((String) obj);
			}
		}
		return ret;
	}

	public static Date convert2Date(Object obj) {
		if (obj == null)
			return null;

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;

		try {
			date = format.parse((String) obj);
		} catch (ParseException e) {
		}
		return date;
	}

	public static boolean convert2Boolean(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue();
		return ((String) obj).toLowerCase().equals("true");
	}

	public static int convert2Int(Object obj) {
		int ret = 0;
		if (obj != null) {
			if (obj instanceof Number) {
				ret = ((Number) obj).intValue();
			} else if (obj instanceof String) {
				ret = Integer.parseInt((String) obj);
			}
		}
		return ret;
	}
}
