package assignment.librarymanager.data;

import org.jetbrains.annotations.NotNull;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookDetailsBuilder {

	private String isbn;
	private String name;
	private int qty;
	private String desctiption;
	private String author;
	private String category;
	private ArrayList<Byte> thumbnail;
	private int borrowedCopies;
	private int ratedValue;
	private int ratedTime;

	public BookDetailsBuilder(@NotNull String isbn, @NotNull String name, int qty) {
		this.isbn = isbn;
		this.name = name;
		this.qty = qty;
		this.desctiption = null;
		this.author = null;
		this.category = null;
		this.thumbnail = null;
		this.borrowedCopies = 0;
		this.ratedValue = 0;
		this.ratedTime = 0;
	}

	public BookDetailsBuilder(@NotNull BookDetails book) {
		this.isbn = book.getIsbn();
		this.name = book.getName();
		this.qty = book.getQty();
		this.desctiption = book.getDesctiption();
		this.author = book.getAuthor();
		this.category = book.getCategory();
		this.thumbnail = book.getThumbnail();
		this.borrowedCopies = book.getBorrowedCopies();
		this.ratedValue = book.getRatedValue();
		this.ratedTime = book.getRatedTime();
	}

	public BookDetailsBuilder setIsbn(@NotNull String isbn) {
		this.isbn = isbn;
		return this;
	}

	public BookDetailsBuilder setName(@NotNull String name) {
		this.name = name;
		return this;
	}

	public BookDetailsBuilder setQty(int qty) throws IllegalArgumentException {
		if (qty < 1) throw new IllegalArgumentException("Quantity must be greater than 0");
		this.qty = qty;
		return this;
	}

	public BookDetailsBuilder setDescription(String description) {
		this.desctiption = description;
		return this;
	}

	public BookDetailsBuilder setAuthor(String author) {
		this.author = author;
		return this;
	}

	public BookDetailsBuilder setCategory(String category) {
		this.category = category;
		return this;
	}

	public BookDetailsBuilder setThumbnail(ArrayList<Byte> thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public BookDetailsBuilder setThumbnail(Blob thumbnail) throws SQLException {
		this.thumbnail = new ArrayList<>();
		for (byte b : thumbnail.getBytes(1, (int) thumbnail.length())) {
			this.thumbnail.add(b);
		}
		return this;
	}

	public BookDetailsBuilder setBorrowedCopies(int borrowedCopies) throws IllegalArgumentException {
		if (borrowedCopies < 0) throw new IllegalArgumentException("Borrowed copies must be greater than or equal to 0");
		this.borrowedCopies = borrowedCopies;
		return this;
	}

	public BookDetailsBuilder setRating(int ratedValue, int ratedTime) {
		this.ratedValue = ratedValue;
		this.ratedTime = ratedTime;
		return this;
	}

	public BookDetails build() {
		return new BookDetails(isbn, name, qty, desctiption, author, category, thumbnail, borrowedCopies, ratedValue, ratedTime);
	}

}
