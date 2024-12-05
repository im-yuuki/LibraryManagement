package assignment.librarymanager.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BookDetails extends Book {

	private final int borrowedCopies;
	private final int ratedValue;
	private final int ratedTime;

	protected BookDetails(
			@NotNull String isbn,
			@NotNull String name,
			int qty,
			String description,
			String author,
			String category,
			ArrayList<Byte> thumbnail,
			int borrowedCopies,
			int ratedValue,
			int ratedTime
	) {
		super(isbn, name, qty, description, author, category, thumbnail);
		this.borrowedCopies = borrowedCopies;
		this.ratedValue = ratedValue;
		this.ratedTime = ratedTime;
	}

	public int getBorrowedCopies() {
		return borrowedCopies;
	}

	public int getRatedValue() {
		return ratedValue;
	}

	public int getRatedTime() {
		return ratedTime;
	}

	public float getRating() {
		return ratedTime == 0 ? 0 : (float) ratedValue / ratedTime;
	}

}
