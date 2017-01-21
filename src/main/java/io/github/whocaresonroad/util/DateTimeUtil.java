package io.github.whocaresonroad.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/*
 * Utility class for time and date related stuff.
 */
public class DateTimeUtil {

	/*
	 * Format the given date with the format string and return it.
	 */
	public static String format(Date dateToFormat, String formatString) {
	SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);

	return dateFormat.format(dateToFormat);
	}

	/*
	 * Advance the given date with the amount, the field tells what to advance.
	 */
	public static Date advance(Date dateToAdvance, int fielToAdvance, int amountToAdvance) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToAdvance);
		calendar.add(fielToAdvance, amountToAdvance);

		return calendar.getTime();
	}
}
