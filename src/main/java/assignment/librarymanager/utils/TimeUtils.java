package assignment.librarymanager.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

	public static long getTimestamp() {
		return fromLocalDate(LocalDate.now());
	}

	public static String representTimestamp(long timestamp) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
	}

	public static long fromLocalDate(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
	}

	public static LocalDate toLocalDate(long timestamp) {
		Date date = new Date(timestamp * 1000);
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
