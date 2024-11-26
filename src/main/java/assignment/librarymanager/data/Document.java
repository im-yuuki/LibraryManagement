package assignment.librarymanager.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Document {

	private final int id;
	private final String name;
	private final int quantity;
	private final String author;
	private final String publisher;
	private final String category;


	public Document(String name, int quantity, String author, String publisher, String category) {
		this.id = -1;
		this.name = name;
		this.quantity = quantity;
		this.author = author;
		this.publisher = publisher;
		this.category = category;
	}

	public Document(int id, String name, int quantity, String author, String publisher, String category) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.author = author;
		this.publisher = publisher;
		this.category = category;
	}

	public int getId() {
		return id;
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

	public String represent() {
		return "[%d]\nName: %s\nAuthor: %s\nPublisher: %s\nCategory: %s\n".formatted(
				id,
				name,
				author,
				publisher,
				category
		);
	}

}
