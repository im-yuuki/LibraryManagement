package assignment.librarymanager.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

	public static LocalDate getCurrentDate() {
		return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDate fromEpoch(long epoch) {
		return new Date(epoch).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
