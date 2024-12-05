package assignment.librarymanagement.utils;

import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class DatabaseWrapper {

	private static final String DATABASE_URL = "jdbc:sqlite:library.db";

	private final Connection connection;
	private final ReentrantLock synchronizedLock;

	public DatabaseWrapper() throws SQLException {
		connection = DriverManager.getConnection(DATABASE_URL);
		synchronizedLock = new ReentrantLock();
		try {
			synchronizedLock.lock();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			synchronizedLock.unlock();
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
				System.out.println(statement);
				ResultSet result = statement.executeQuery();
				Platform.runLater(() -> onSuccess.accept(result));
			} catch (SQLException e) {
				Platform.runLater(() -> onFailure.accept(e));
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
				System.out.println(statement);
				int result = statement.executeUpdate();
				Platform.runLater(() -> onSuccess.accept(result));
			} catch (SQLException e) {
				Platform.runLater(() -> onFailure.accept(e));
			} finally {
				synchronizedLock.unlock();
			}
		}).start();
	}

}
