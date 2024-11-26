package assignment.librarymanager.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Database {

	private static final String DATABASE_URL = "jdbc:sqlite:library.db";

	private final Connection connection;
	private final ReentrantLock writeLock;

	public Database() throws SQLException, IOException {
		connection = DriverManager.getConnection(DATABASE_URL);
		writeLock = new ReentrantLock();
		try {
			writeLock.lock();
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
		} finally {
			writeLock.unlock();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void close() throws SQLException {
		connection.close();
	}

	public void lockWrite() {
		writeLock.lock();
	}

	public void unlockWrite() {
		writeLock.unlock();
	}

}
