package assignment.librarymanagement.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class Borrow {

	private final int id;
	private final String isbn;
	private final String username;
	private final LocalDate from;
	private final LocalDate due;
	private final LocalDate returnDate;
	private final String comment;
	private final int rating; // 0 for not rated

	protected Borrow(
			int id,
			@NotNull String isbn,
			@NotNull String username,
			@NotNull LocalDate from,
			@NotNull LocalDate due,
			LocalDate returnDate,
			String comment,
			int rating
	) {
		this.id = id;
		this.isbn = isbn;
		this.username = username;
		this.from = from;
		this.due = due;
		this.returnDate = returnDate;
		this.comment = comment;
		this.rating = rating;
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

	public LocalDate getDue() {
		return due;
	}

	@Nullable
	public LocalDate getReturnDate() {
		return returnDate;
	}

	public String getComment() {
		return comment;
	}

	public int getRating() {
		return rating;
	}

}
