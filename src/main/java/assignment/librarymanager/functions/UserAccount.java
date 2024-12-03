package assignment.librarymanager.functions;

import assignment.librarymanager.data.User;
import assignment.librarymanager.managers.Database;

public class UserAccount {

	private final Database database;

	public UserAccount(Database database) {
		this.database = database;
	}

	public User login(String username, String password) {
		return null;
	}

	public boolean register(String username, String password, long expirationTime, String email, String phoneNumber) {
		return false;
	}

	public User updateInfo(User user, String username, String password, String email, String phoneNumber) {
		return null;
	}

	public User changePassword(User user, String password) {
		return null;
	}

}
