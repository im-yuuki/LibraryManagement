package assignment.librarymanager.data;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Book {

	private final String isbn;
	private final String name;
	private final int qty;
	private final String desctiption;
	private final String author;
	private final String category;
	private final Image thumbnail;

	protected Book(
			@NotNull String isbn,
			@NotNull String name,
			int qty,
			String description,
			String author,
			String category,
			Image thumbnail
	) {
		this.isbn = isbn;
		this.name = name;
		this.qty = qty;
		this.desctiption = description;
		this.author = author;
		this.category = category;
		this.thumbnail = thumbnail;
	}

	@NotNull
	public String getIsbn() {
		return isbn;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public int getQty() {
		return qty;
	}

	@Nullable
	public String getDesctiption() {
		return desctiption;
	}

	@Nullable
	public String getAuthor() {
		return author;
	}

	@Nullable
	public String getCategory() {
		return category;
	}

	@Nullable
	public Image getThumbnail() {
		return thumbnail;
	}

	public String qrText() {
		return "[%s]\nName: %s\nAuthor: %s\nCategory: %s\n".formatted(
				isbn,
				name,
				author,
				category
		);
	}

}
