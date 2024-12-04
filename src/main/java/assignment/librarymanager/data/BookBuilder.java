package assignment.librarymanager.data;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public class BookBuilder {

	private String isbn;
	private String name;
	private int qty;
	private String desctiption;
	private String author;
	private String category;
	private Image thumbnail;

	public BookBuilder(@NotNull String isbn, @NotNull String name, int qty) {
		this.isbn = isbn;
		this.name = name;
		this.qty = qty;
		this.desctiption = null;
		this.author = null;
		this.category = null;
		this.thumbnail = null;
	}

	public BookBuilder(@NotNull Book book) {
		this.isbn = book.getIsbn();
		this.name = book.getName();
		this.qty = book.getQty();
		this.desctiption = book.getDesctiption();
		this.author = book.getAuthor();
		this.category = book.getCategory();
		this.thumbnail = book.getThumbnail();
	}

	public BookBuilder setIsbn(@NotNull String isbn) {
		this.isbn = isbn;
		return this;
	}

	public BookBuilder setName(@NotNull String name) {
		this.name = name;
		return this;
	}

	public BookBuilder setQty(int qty) throws IllegalArgumentException {
		if (qty < 1) throw new IllegalArgumentException("Quantity must be greater than 0");
		this.qty = qty;
		return this;
	}

	public BookBuilder setDescription(String description) {
		this.desctiption = description;
		return this;
	}

	public BookBuilder setAuthor(String author) {
		this.author = author;
		return this;
	}

	public BookBuilder setCategory(String category) {
		this.category = category;
		return this;
	}

	public BookBuilder setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public Book build() {
		return new Book(isbn, name, qty, desctiption, author, category, thumbnail);
	}

}
