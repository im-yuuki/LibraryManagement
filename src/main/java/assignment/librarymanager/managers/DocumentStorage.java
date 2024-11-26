package assignment.librarymanager.managers;

import assignment.librarymanager.data.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class DocumentStorage extends StorageAbs<Document> {

	public DocumentStorage(Database database) {
		super(database);
	}

	private static Document parseDocument(int id, ResultSet resultSet) throws SQLException, NullPointerException {
		String name = Objects.requireNonNull(resultSet.getString("name"));
		int quantity = resultSet.getInt("quantity");
		String author = resultSet.getString("author");
		String publisher = resultSet.getString("publisher");
		String category = Objects.requireNonNull(resultSet.getString("category"));
		return new Document(id, name, quantity, author, publisher, category);
	}

	@Override
	public Document getEntry(int id) throws SQLException {
		Document document = null;
		String sql = "SELECT name, quantity, author, publisher, category FROM documents WHERE id = " + id + " LIMIT 1";
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();
		if (resultSet.next()) try {
			document = parseDocument(id, resultSet);
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return document;
	}

	@Override
	public Document setEntry(Document entry) throws SQLException {
		if (entry.getId() == -1) {
			String sql = "INSERT INTO documents (name, quantity, author, publisher, category) VALUES (\"%s\", %d, \"%s\", \"%s\", \"%s\")".formatted(
					entry.getName(),
					entry.getQuantity(),
					entry.getAuthor(),
					entry.getPublisher(),
					entry.getCategory()
			);
			try {
				database.lockWrite();
				Statement statement = database.getConnection().createStatement();
				statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					entry = new Document(
							resultSet.getInt(1),
							entry.getName(),
							entry.getQuantity(),
							entry.getAuthor(),
							entry.getPublisher(),
							entry.getCategory()
					);
				}
				statement.close();
			} finally {
				database.unlockWrite();
			}
		} else {
			String sql = "UPDATE documents SET name = \"%s\", quantity = %d, author = \"%s\", publisher = \"%s\", category = \"%s\" WHERE id = %d".formatted(
					entry.getName(),
					entry.getQuantity(),
					entry.getAuthor(),
					entry.getPublisher(),
					entry.getCategory(),
					entry.getId()
			);
			try {
				database.lockWrite();
				Statement statement = database.getConnection().createStatement();
				statement.executeUpdate(sql);
				statement.close();
			} finally {
				database.unlockWrite();
			}
		}
		return entry;
	}

	@Override
	public boolean deleteEntry(int id) {
		String sql = "DELETE FROM documents WHERE id = " + id;
		int rowsAffected;
		try {
			database.lockWrite();
			Statement statement = database.getConnection().createStatement();
			rowsAffected = statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			return false;
		} finally {
			database.unlockWrite();
		}
		return rowsAffected > 0;
	}

	public ArrayList<Document> queryDocuments(@NotNull String name, @NotNull String author, @NotNull String publisher, @NotNull String category) throws SQLException {
		ArrayList<Document> documents = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT id, name, quantity, author, publisher, category FROM documents WHERE 1 = 1");
		if (!name.isBlank()) {
			sql.append(" AND name LIKE \"%").append(name).append("%\"");
		}
		if (!author.isBlank()) {
			sql.append(" AND author LIKE \"%").append(author).append("%\"");
		}
		if (!publisher.isBlank()) {
			sql.append(" AND publisher LIKE \"%").append(publisher).append("%\"");
		}
		if (!category.isBlank()) {
			sql.append(" AND category = \"").append(category).append("\"");
		}
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql.toString());
		ResultSet resultSet = statement.getResultSet();
		while (resultSet.next()) try {
			documents.add(parseDocument(resultSet.getInt("id"), resultSet));
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return documents;
	}

}
