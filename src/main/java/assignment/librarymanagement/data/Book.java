package assignment.librarymanagement.data;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;

public class Book {

	private final String isbn;
	private final String name;
	private final int qty;
	private final String desctiption;
	private final String author;
	private final String category;
	private final ArrayList<Byte> thumbnail;

	protected Book(
			@NotNull String isbn,
			@NotNull String name,
			int qty,
			String description,
			String author,
			String category,
			ArrayList<Byte> thumbnail
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
	public ArrayList<Byte> getThumbnail() {
		return thumbnail;
	}

	@Nullable
	public Image getThumbnailImage() {
		if (thumbnail == null) return null;
		byte[] bytes = new byte[thumbnail.size()];
		for (int i = 0; i < thumbnail.size(); i++) {
			bytes[i] = thumbnail.get(i);
		}
		return new Image(new InputStream() {
			private int index = 0;
			@Override
			public int read() {
				if (index >= bytes.length) return -1;
				return bytes[index++] & 0xFF;
			}
		});
	}

	@Nullable
	public InputStream getThumbnailStream() {
		if (thumbnail == null) return null;
		byte[] bytes = new byte[thumbnail.size()];
		for (int i = 0; i < thumbnail.size(); i++) {
			bytes[i] = thumbnail.get(i);
		}
		return new InputStream() {
			private int index = 0;
			@Override
			public int read() {
				if (index >= bytes.length) return -1;
				return bytes[index++] & 0xFF;
			}
		};
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
