package assignment.librarymanagement.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

	public static LocalDate now() {
		return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDate fromEpoch(long epoch) {
		return new Date(epoch).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static long toEpoch(@NotNull LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
	}

}
