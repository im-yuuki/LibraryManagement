package assignment.librarymanager.data;

import assignment.librarymanager.utils.PasswordHash;
import assignment.librarymanager.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class User {

	public enum Role {
		ADMIN,
		USER
	}

	private final String username;
	private final String name;
	private final String passwordHash;
	private final Role role;
	private final LocalDate creation;
	private final String email;
	private final String notice;

	protected User(
			@NotNull String username,
			@NotNull String name,
			@NotNull String passwordHash,
			@NotNull Role role,
			@NotNull LocalDate creation,
			@NotNull String email,
			String notice
	) {
		this.username = username;
		this.name = name;
		this.passwordHash = passwordHash;
		this.role = role;
		this.creation = creation;
		this.email = email;
		this.notice = notice;
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

	public String getNotice() {
		return notice;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

}
