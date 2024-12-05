package assignment.librarymanagement.managers;

import assignment.librarymanagement.data.User;
import assignment.librarymanagement.data.UserBuilder;
import assignment.librarymanagement.utils.DatabaseWrapper;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class UsersManager {

	private final DatabaseWrapper database;

	public UsersManager(DatabaseWrapper database) {
		this.database = database;
	}

	private User parseResultSet(ResultSet resultSet) throws SQLException {
		User.Role role;
		try {
			role = User.Role.valueOf(resultSet.getString("role"));
		} catch (IllegalArgumentException e) {
			role = User.Role.USER;
		}
		UserBuilder builder = new UserBuilder(
			resultSet.getString("username"),
			resultSet.getString("name"),
			resultSet.getString("pwd_hash"),
			role,
			resultSet.getDate("creation").toLocalDate(),
			resultSet.getString("email")
		);
		builder.setNotification(resultSet.getString("notification"));
		return builder.build();

	}

	public void select(@NotNull String username, @NotNull Consumer<User> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "SELECT * FROM users WHERE username = ? LIMIT 1";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, username);
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

	public void insert(@NotNull User user, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "INSERT INTO users (username, name, pwd_hash, role, creation, email, notification) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getName());
			statement.setString(3, user.getPasswordHash());
			statement.setString(4, user.getRole().name());
			statement.setDate(5, Date.valueOf(user.getCreation()));
			statement.setString(6, user.getEmail());
			statement.setString(7, user.getNotification());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void update(@NotNull User user, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "UPDATE users SET name = ?, pwd_hash = ?, role = ?, email = ?, notification = ? WHERE username = ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, user.getName());
			statement.setString(2, user.getPasswordHash());
			statement.setString(3, user.getRole().name());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getNotification());
			statement.setString(6, user.getUsername());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void delete(@NotNull User user, @NotNull Consumer<Integer> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "DELETE FROM users WHERE username = ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, user.getUsername());
			database.executeUpdateInThread(statement, onSuccess, onFailure);
		} catch (SQLException e) {
			onFailure.accept(e);
		}
	}

	public void globalSearch(@NotNull String match, @NotNull Consumer<User[]> onSuccess, @NotNull Consumer<SQLException> onFailure) {
		String sql = "SELECT * FROM users WHERE username LIKE ? OR name LIKE ? OR email LIKE ?";
		try {
			PreparedStatement statement = database.createPreparedStatement(sql);
			statement.setString(1, "%" + match + "%");
			statement.setString(2, "%" + match + "%");
			statement.setString(3, "%" + match + "%");
			database.executeQueryInThread(
					statement,
					resultSet -> {
						try {
							ArrayList<User> users = new java.util.ArrayList<>();
							while (resultSet.next()) {
								users.add(parseResultSet(resultSet));
							}
							onSuccess.accept(users.toArray(new User[0]));
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
