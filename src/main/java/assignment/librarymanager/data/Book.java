package assignment.librarymanager.data;

public class Book {

	private final String isbn;
	private final String name;
	private final int quantity;
	private final String author;
	private final String publisher;
	private final String category;

	public Book(String isbn, String name, int quantity, String author, String publisher, String category) {
		this.isbn = isbn;
		this.name = name;
		this.quantity = quantity;
		this.author = author;
		this.publisher = publisher;
		this.category = category;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getCategory() {
		return category;
	}

	public String qrText() {
		return "[%s]\nName: %s\nAuthor: %s\nPublisher: %s\nCategory: %s\n".formatted(
				isbn,
				name,
				author,
				publisher,
				category
		);
	}

}
