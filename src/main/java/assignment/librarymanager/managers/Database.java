package assignment.librarymanager.managers;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Database {

	private static final String DATABASE_URL = "jdbc:sqlite:library.db";

	private final Connection connection;
	private final ReentrantLock synchronizedLock;

	public Database() throws SQLException, IOException {
		connection = DriverManager.getConnection(DATABASE_URL);
		synchronizedLock = new ReentrantLock();
		Statement statement = connection.createStatement();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/init.sql")))
		);
		StringBuilder sql = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sql.append(line);
			if (line.trim().endsWith(";")) {
				statement.execute(sql.toString());
				sql.setLength(0);
			}
		}
	}

	public PreparedStatement createPreparedStatement(@NotNull String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

	public void close() throws SQLException {
		connection.close();
	}

	public void executeQueryInThread(
			@NotNull PreparedStatement statement,
			@NotNull Consumer<ResultSet> onSuccess,
			@NotNull Consumer<SQLException> onFailure
	) {
		new Thread(() -> {
			try {
				synchronizedLock.lock();
				onSuccess.accept(statement.executeQuery());
				statement.close();
			} catch (SQLException e) {
				onFailure.accept(e);
			} finally {
				synchronizedLock.unlock();
			}
		}).start();
	}

	public void executeUpdateInThread(
			@NotNull PreparedStatement statement,
			@NotNull Consumer<Integer> onSuccess,
			@NotNull Consumer<SQLException> onFailure
	) {
		new Thread(() -> {
			try {
				synchronizedLock.lock();
				onSuccess.accept(statement.executeUpdate());
				statement.close();
			} catch (SQLException e) {
				onFailure.accept(e);
			} finally {
				synchronizedLock.unlock();
			}
		}).start();
	}

}
