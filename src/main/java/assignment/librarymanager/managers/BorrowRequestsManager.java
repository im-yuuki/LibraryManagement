package assignment.librarymanager.managers;

import assignment.librarymanager.data.BorrowRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class BorrowRequestsManager {

	private final Database database;

	public BorrowRequestsManager(Database database) {
		this.database = database;
	}

	private BorrowRequest parseResultSet(ResultSet resultSet) throws SQLException {
		return new BorrowRequest(
				resultSet.getInt("id"),
				resultSet.getString("isbn"),
				resultSet.getString("username"),
				resultSet.getDate("from").toLocalDate(),
				resultSet.getInt("duration")
		);
	}

	public void getAll(@NotNull Consumer<BorrowRequest[]> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "SELECT * FROM borrow_requests";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							ArrayList<BorrowRequest> requests = new ArrayList<>();
							while (resultSet.next()) {
								requests.add(parseResultSet(resultSet));
							}
							onSuccess.accept(requests.toArray(new BorrowRequest[0]));
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

	public void insert(@NotNull BorrowRequest request, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "INSERT INTO borrow_requests (isbn, username, from, duration) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setString(1, request.getIsbn());
			statement.setString(2, request.getUsername());
			statement.setDate(3, java.sql.Date.valueOf(request.getFrom()));
			statement.setInt(4, request.getDuration());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void delete(@NotNull BorrowRequest request, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "DELETE FROM borrow_requests WHERE id = ?";
		try (PreparedStatement statement = database.createPreparedStatement(sql)) {
			statement.setInt(1, request.getId());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

}
