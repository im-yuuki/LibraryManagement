package assignment.librarymanager.managers;

import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public abstract class StorageAbs<Type> {

	protected final Database database;

	public StorageAbs(Database database) {
		this.database = database;
	}

	@Nullable
	public abstract Type getEntry(int id) throws SQLException;

	public abstract Type setEntry(Type entry) throws SQLException;

	public abstract boolean deleteEntry(int id) throws SQLException;

}
