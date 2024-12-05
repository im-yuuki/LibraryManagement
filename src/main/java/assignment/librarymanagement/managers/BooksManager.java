package assignment.librarymanagement.managers;

import assignment.librarymanagement.data.Book;
import assignment.librarymanagement.data.BookDetails;
import assignment.librarymanagement.data.BookDetailsBuilder;
import assignment.librarymanagement.utils.DatabaseWrapper;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class BooksManager {

	DatabaseWrapper database;

	public BooksManager(DatabaseWrapper database) {
		this.database = database;
	}

	private Book parseResultSet(ResultSet resultSet) throws SQLException {
		BookDetailsBuilder builder = new BookDetailsBuilder(
				resultSet.getString("isbn"),
				resultSet.getString("name"),
				resultSet.getInt("qty")
		);
		builder.setDescription(resultSet.getString("description"));
		builder.setAuthor(resultSet.getString("author"));
		builder.setCategory(resultSet.getString("category"));
		return builder.build();
	}

	private BookDetails parseResultSetWithDetails(ResultSet resultSet) throws SQLException {
		BookDetailsBuilder builder = new BookDetailsBuilder(
				resultSet.getString("isbn"),
				resultSet.getString("name"),
				resultSet.getInt("qty")
		);
		builder.setDescription(resultSet.getString("description"));
		builder.setAuthor(resultSet.getString("author"));
		builder.setCategory(resultSet.getString("category"));
		builder.setThumbnail(resultSet.getBlob("thumbnail"));
		builder.setBorrowedCopies(resultSet.getInt("borrowed_copies"));
		builder.setRating(resultSet.getInt("rated_value"), resultSet.getInt("rated_time"));
		return builder.build();
	}

	public void select(@NotNull String isbn, @NotNull Consumer<BookDetails> onSuccess, @NotNull Consumer<String> onFailure) {
		String sql = "SELECT " +
				"b.*, " +
				"COUNT(CASE WHEN br.return_date IS NULL THEN 1 ELSE NULL END) AS borrowed_copies, " +
				"SUM(CASE WHEN br.rating > 0 THEN br.rating ELSE 0 END) AS rated_value, " +
				"COUNT(CASE WHEN br.rating > 0 THEN 1 ELSE NULL END) AS rated_time " +
				"FROM books b " +
				"LEFT JOIN borrow br ON b.isbn = br.isbn " +
				"WHERE b.isbn = ? " +
				"GROUP BY b.isbn " +
				"LIMIT 1";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, isbn);
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							if (resultSet.next()) {
								onSuccess.accept(parseResultSetWithDetails(resultSet));
							} else {
								onSuccess.accept(null);
							}
						} catch (SQLException e) {
							onFailure.accept(e.getMessage());
						}
					},
					e -> onFailure.accept(e.getMessage())
			);
		} catch (SQLException e) {
			onFailure.accept(e.getMessage());
		}
	}

	public void insert(@NotNull Book book, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "INSERT INTO books (isbn, name, qty, description, author, category, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, book.getIsbn());
			statement.setString(2, book.getName());
			statement.setInt(3, book.getQty());
			statement.setString(4, book.getDesctiption());
			statement.setString(5, book.getAuthor());
			statement.setString(6, book.getCategory());
			statement.setBlob(7, book.getThumbnailStream());
			database.executeUpdateInThread(
					statement,
					onSuccess,
					onFailure
			);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void update(@NotNull Book book, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "UPDATE books SET name = ?, qty = ?, description = ?, author = ?, category = ?, thumbnail = ? WHERE isbn = ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, book.getName());
			statement.setInt(2, book.getQty());
			statement.setString(3, book.getDesctiption());
			statement.setString(4, book.getAuthor());
			statement.setString(5, book.getCategory());
			statement.setBlob(6, book.getThumbnailStream());
			statement.setString(7, book.getIsbn());
			database.executeUpdateInThread(
					statement,
					onSuccess,
					onFailure
			);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void delete(@NotNull Book book, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "DELETE FROM books WHERE isbn = ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, book.getIsbn());
			database.executeUpdateInThread(
					statement,
					onSuccess,
					onFailure
			);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void globalSearch(@NotNull String match, @NotNull Consumer<Book[]> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "SELECT * FROM books WHERE name LIKE ? OR author LIKE ? OR category LIKE ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, "%" + match + "%");
			statement.setString(2, "%" + match + "%");
			statement.setString(3, "%" + match + "%");
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							ArrayList<Book> books = new ArrayList<>();
							while (resultSet.next()) {
								books.add(parseResultSet(resultSet));
							}
							onSuccess.accept(books.toArray(new Book[0]));
						} catch (SQLException e) {
							onFailure.accept(e);
						}
					},
					onFailure
			);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

}
