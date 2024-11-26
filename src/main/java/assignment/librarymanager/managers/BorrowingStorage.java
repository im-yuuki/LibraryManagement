package assignment.librarymanager.managers;

import assignment.librarymanager.data.Borrowing;
import assignment.librarymanager.utils.TimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BorrowingStorage extends StorageAbs<Borrowing> {

	public BorrowingStorage(Database database) {
		super(database);
	}

	private static Borrowing parseBorrowing(int id, ResultSet resultSet) throws SQLException, NullPointerException {
		int readerId = resultSet.getInt("reader_id");
		int documentId = resultSet.getInt("document_id");
		long borrowTime = resultSet.getLong("borrow_time");
		long dueTime = resultSet.getLong("due_time");
		long returnTime = resultSet.getLong("return_time");
		return new Borrowing(id, readerId, documentId, borrowTime, dueTime, returnTime);
	}

	@Override
	public Borrowing getEntry(int id) throws SQLException {
		Borrowing borrowing = null;
		String sql = "SELECT reader_id, document_id, borrow_time, due_time, return_time FROM borrowings WHERE id = " + id + " LIMIT 1";
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();
		if (resultSet.next()) try {
			borrowing = parseBorrowing(id, resultSet);
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return borrowing;
	}

	@Override
	public Borrowing setEntry(Borrowing entry) throws SQLException {
		if (entry.getId() == -1) {
			String sql = "INSERT INTO borrowings (reader_id, document_id, borrow_time, due_time, return_time) VALUES (%d, %d, %d, %d, %d)".formatted(
					entry.getReaderId(),
					entry.getDocumentId(),
					entry.getBorrowTime(),
					entry.getDueTime(),
					entry.getReturnTime()
			);
			try {
				database.lockWrite();
				Statement statement = database.getConnection().createStatement();
				statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					entry = new Borrowing(
							resultSet.getInt(1),
							entry.getReaderId(),
							entry.getDocumentId(),
							entry.getBorrowTime(),
							entry.getDueTime(),
							entry.getReturnTime()
					);
				}
				statement.close();
			} finally {
				database.unlockWrite();
			}
		} else {
			String sql = "UPDATE borrowings SET reader_id = %d, document_id = %d, borrow_time = %d, due_time = %d, return_time = %d WHERE id = %d".formatted(
					entry.getReaderId(),
					entry.getDocumentId(),
					entry.getBorrowTime(),
					entry.getDueTime(),
					entry.getReturnTime(),
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
		String sql = "DELETE FROM borrowings WHERE id = " + id;
		try {
			database.lockWrite();
			Statement statement = database.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public ArrayList<Borrowing> findBorrowings(int documentId, int readerId, boolean overdueOnly, boolean getHistory) throws SQLException {
		String sql = "SELECT id, reader_id, document_id, borrow_time, due_time, return_time FROM borrowings WHERE 1 = 1";
		if (documentId != -1) sql += " AND document_id = " + documentId;
		if (readerId != -1) sql += " AND reader_id = " + readerId;
		if (overdueOnly && !getHistory) sql += " AND return_time = -1 AND due_time < " + TimeUtils.getTimestamp();
		else if (!getHistory) sql += " AND return_time = -1";
		else if (overdueOnly) sql += " AND return_time != -1 AND due_time < return_time";
		Statement statement = database.getConnection().createStatement();
		statement.execute(sql);
		ResultSet resultSet = statement.getResultSet();
		ArrayList<Borrowing> borrowings = new ArrayList<>();
		while (resultSet.next()) try {
			borrowings.add(parseBorrowing(resultSet.getInt("id"), resultSet));
		} catch (NullPointerException e) {
			throw new SQLException("Invalid data in database");
		}
		statement.close();
		return borrowings;
	}

}
