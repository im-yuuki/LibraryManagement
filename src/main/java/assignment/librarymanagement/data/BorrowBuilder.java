package assignment.librarymanagement.data;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class BorrowBuilder {

	private int id;
	private String isbn;
	private String username;
	private LocalDate from;
	private LocalDate due;
	private LocalDate returnDate;
	private String comment;
	private int rating; // 0 for not rated

	public BorrowBuilder(
			int id,
			@NotNull String isbn,
			@NotNull String username,
			@NotNull LocalDate from,
			@NotNull LocalDate due
	) {
		this.id = id;
		this.isbn = isbn;
		this.username = username;
		this.from = from;
		this.due = due;
		this.returnDate = null;
		this.comment = null;
		this.rating = 0;
	}

	public BorrowBuilder(@NotNull Borrow borrow) {
		this.id = borrow.getId();
		this.isbn = borrow.getIsbn();
		this.username = borrow.getUsername();
		this.from = borrow.getFrom();
		this.due = borrow.getDue();
		this.returnDate = borrow.getReturnDate();
		this.comment = borrow.getComment();
		this.rating = borrow.getRating();
	}

	public BorrowBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public BorrowBuilder setIsbn(@NotNull String isbn) {
		this.isbn = isbn;
		return this;
	}

	public BorrowBuilder setUsername(@NotNull String username) {
		this.username = username;
		return this;
	}

	public BorrowBuilder setFrom(@NotNull LocalDate from) {
		this.from = from;
		return this;
	}

	public BorrowBuilder setDue(@NotNull LocalDate due) {
		this.due = due;
		return this;
	}

	public BorrowBuilder setReturnDate(LocalDate returnDate) throws IllegalArgumentException {
		if (returnDate != null && returnDate.isBefore(from)) throw new IllegalArgumentException("Return date must be after from date");
		this.returnDate = returnDate;
		return this;
	}

	public BorrowBuilder setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public BorrowBuilder setRating(int rating) throws IllegalArgumentException {
		if (rating < 0 || rating > 5) throw new IllegalArgumentException("Rating must be between 0 and 5");
		this.rating = rating;
		return this;
	}

	public Borrow build() {
		return new Borrow(id, isbn, username, from, due, returnDate, comment, rating);
	}

}
