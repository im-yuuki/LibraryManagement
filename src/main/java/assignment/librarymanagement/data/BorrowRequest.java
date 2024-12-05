package assignment.librarymanagement.data;

import assignment.librarymanagement.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class BorrowRequest {

	private final int id;
	private final String isbn;
	private final String username;
	private final LocalDate from;
	private final int duration;

	public BorrowRequest(
			int id,
			@NotNull String isbn,
			@NotNull String username,
			LocalDate from,
			int duration
	) {
		if (duration <= 0)  throw new IllegalArgumentException("Duration must be positive");
		this.id = id;
		this.isbn = isbn;
		this.username = username;
		this.from = Objects.requireNonNullElse(from, TimeUtils.now());
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getUsername() {
		return username;
	}

	public LocalDate getFrom() {
		return from;
	}

	public int getDuration() {
		return duration;
	}

}
