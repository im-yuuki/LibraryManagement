package assignment.librarymanager.managers;

import assignment.librarymanager.data.Borrow;
import assignment.librarymanager.data.BorrowBuilder;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.function.Consumer;

public class BorrowsManager {

	private final Database database;

	public BorrowsManager(Database database) {
		this.database = database;
	}

	private Borrow parseResultSet(ResultSet resultSet) throws SQLException {
		BorrowBuilder builder = new BorrowBuilder(
				resultSet.getInt("id"),
				resultSet.getString("isbn"),
				resultSet.getString("username"),
				resultSet.getDate("from").toLocalDate(),
				resultSet.getDate("due").toLocalDate()
		);
		Date returnDate = resultSet.getDate("return_date");
		builder.setReturnDate(returnDate != null ? returnDate.toLocalDate() : null);
		builder.setComment(resultSet.getString("comment"));
		builder.setRating(resultSet.getInt("rating"));
		return builder.build();
	}

	public void select(int id, @NotNull Consumer<Borrow> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "SELECT * FROM borrows WHERE id = ? LIMIT 1";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setInt(1, id);
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							if (resultSet.next()) {
								onSuccess.accept(parseResultSet(resultSet));
							} else {
								onSuccess.accept(null);
							}
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

	public void insert(@NotNull Borrow borrow, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "INSERT INTO borrows (isbn, username, from, due, return_date, comment, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setString(1, borrow.getIsbn());
			statement.setString(2, borrow.getUsername());
			statement.setDate(3, java.sql.Date.valueOf(borrow.getFrom()));
			statement.setDate(4, java.sql.Date.valueOf(borrow.getDue()));
			statement.setDate(5, borrow.getReturnDate() != null ? java.sql.Date.valueOf(borrow.getReturnDate()) : null);
			statement.setString(6, borrow.getComment());
			statement.setInt(7, borrow.getRating());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void update(@NotNull Borrow borrow, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "UPDATE borrows SET isbn = ?, username = ?, from = ?, due = ?, return_date = ?, comment = ?, rating = ? WHERE id = ?";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setString(1, borrow.getIsbn());
			statement.setString(2, borrow.getUsername());
			statement.setDate(3, java.sql.Date.valueOf(borrow.getFrom()));
			statement.setDate(4, java.sql.Date.valueOf(borrow.getDue()));
			statement.setDate(5, borrow.getReturnDate() != null ? java.sql.Date.valueOf(borrow.getReturnDate()) : null);
			statement.setString(6, borrow.getComment());
			statement.setInt(7, borrow.getRating());
			statement.setInt(8, borrow.getId());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void delete(@NotNull Borrow borrow, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		onFailure.accept(new SQLException("This method is not supported"));
//		String sql = "DELETE FROM borrows WHERE id = ?";
//		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
//			statement.setInt(1, borrow.getId());
//			database.executeUpdateInThread(statement, onSuccess, onFailure);
//		} catch (SQLException e) {
//			onFailure.accept(e);
//		}
	}

	public void search(
			@NotNull String isbn,
			@NotNull String username,
			boolean notReturnedBorrowsOnly,
			@NotNull Consumer<Borrow[]> onSuccess,
			@NotNull Consumer<SQLException> onFailure
	) {
		String sql = "SELECT * FROM borrows WHERE isbn LIKE ? AND username LIKE ?";
		if (notReturnedBorrowsOnly) {
			sql += " AND return_date IS NULL";
		}
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setString(1, "%" + isbn + "%");
			statement.setString(2, "%" + username + "%");
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							ArrayList<Borrow> borrows = new ArrayList<>();
							while (resultSet.next()) {
								borrows.add(parseResultSet(resultSet));
							}
							onSuccess.accept(borrows.toArray(new Borrow[0]));
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
