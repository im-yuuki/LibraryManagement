package assignment.librarymanagement.data;

import assignment.librarymanagement.utils.PasswordHash;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class User {

	public enum Role {
		ADMINISTRATOR,
		USER
	}

	private final String username;
	private final String name;
	private final String passwordHash;
	private final Role role;
	private final LocalDate creation;
	private final String email;
	private final String notification;

	protected User(
			@NotNull String username,
			@NotNull String name,
			@NotNull String passwordHash,
			@NotNull Role role,
			@NotNull LocalDate creation,
			@NotNull String email,
			String notification
	) {
		this.username = username;
		this.name = name;
		this.passwordHash = passwordHash;
		this.role = role;
		this.creation = creation;
		this.email = email;
		this.notification = notification;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public LocalDate getCreation() {
		return creation;
	}

	public Role getRole() {
		return role;
	}

	public String getCreationFormatted() {
		return creation.toString();
	}

	public String getEmail() {
		return email;
	}

	public boolean verifyPassword(String password) {
		return PasswordHash.verify(password, passwordHash);
	}

	public String getNotification() {
		return notification;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

}
