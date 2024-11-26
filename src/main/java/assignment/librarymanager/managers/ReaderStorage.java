package assignment.librarymanager.managers;

import assignment.librarymanager.data.Reader;
import assignment.librarymanager.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class ReaderStorage extends StorageAbs<Reader> {

	public ReaderStorage(Database database) {
		super(database);
	}

	private static Reader parseReader(int id, ResultSet resultSet) throws SQLException, NullPointerException {
		String name = Objects.requireNonNull(resultSet.getString("name"));
		String passwordHash = Objects.requireNonNull(resultSet.getString("password_hash"));
		long registrationTime = resultSet.getLong("registration_time");
		long expirationTime = resultSet.getLong("expiration_time");
		String email = resultSet.getString("email");
		String phoneNumber = resultSet.getString("phone_number");
		return new Reader(id, name, passwordHash, registrationTime, expirationTime, email, phoneNumber);
	}

	@Override
	public Reader getEntry(int id) throws SQLException {
		Reader reader = null;
		String sql = "SELECT name, password_hash, registration_time, expiration_time, email, phone_number FROM readers WHERE id = " + id + " LIMIT 1";
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();
		if (resultSet.next()) try {
			reader = parseReader(id, resultSet);
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return reader;
	}

	@Override
	public Reader setEntry(Reader entry) throws SQLException {
		if (entry.getId() == -1) {
			String sql = "INSERT INTO readers (name, password_hash, registration_time, expiration_time, email, phone_number) VALUES (\"%s\", \"%s\", %d, %d, \"%s\", \"%s\")".formatted(
					entry.getName(),
					entry.getPasswordHash(),
					entry.getRegistrationTime(),
					entry.getExpirationTime(),
					entry.getEmail(),
					entry.getPhoneNumber()
			);
			try {
				database.lockWrite();
				Statement statement = database.getConnection().createStatement();
				statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					entry = new Reader(
							resultSet.getInt(1),
							entry.getName(),
							entry.getPasswordHash(),
							entry.getRegistrationTime(),
							entry.getExpirationTime(),
							entry.getEmail(),
							entry.getPhoneNumber()
					);
				}
				statement.close();
			} finally {
				database.unlockWrite();
			}
		} else {
			String sql = "UPDATE readers SET name = \"%s\", password_hash = \"%s\", registration_time = %d, expiration_time = %d, email = \"%s\", phone_number = \"%s\" WHERE id = %d".formatted(
					entry.getName(),
					entry.getPasswordHash(),
					entry.getRegistrationTime(),
					entry.getExpirationTime(),
					entry.getEmail(),
					entry.getPhoneNumber(),
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
		String sql = "DELETE FROM readers WHERE id = " + id;
		try {
			database.lockWrite();
			Statement statement = database.getConnection().createStatement();
			statement.execute(sql);
			statement.close();
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			database.unlockWrite();
		}
	}

	public ArrayList<Reader> getAllReaders(@NotNull String name, @NotNull String email, @NotNull String phoneNumber, boolean notExpiredOnly) throws SQLException {
		ArrayList<Reader> readers = new ArrayList<>();
		String sql = "SELECT id, name, password_hash, registration_time, expiration_time, email, phone_number FROM readers WHERE 1 = 1";
		if (!name.isBlank()) sql += " AND name = \"" + name + "\"";
		if (!email.isBlank()) sql += " AND email = \"" + email + "\"";
		if (!phoneNumber.isBlank()) sql += " AND phone_number = \"" + phoneNumber + "\"";
		if (notExpiredOnly) sql += " AND expiration_time > " + TimeUtils.getTimestamp();
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();
		while (resultSet.next()) try {
			readers.add(parseReader(resultSet.getInt("id"), resultSet));
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return readers;
	}
}
