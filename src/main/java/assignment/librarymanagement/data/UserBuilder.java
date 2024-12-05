package assignment.librarymanagement.data;

import assignment.librarymanagement.data.User.Role;
import assignment.librarymanagement.utils.PasswordHash;
import assignment.librarymanagement.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class UserBuilder {

	private String username;
	private String name;
	private String passwordHash;
	private Role role;
	private LocalDate creation;
	private String email;
	private String notification;

	public UserBuilder(
			@NotNull String username,
			@NotNull String name,
			@NotNull String passwordHash,
			Role role,
			LocalDate creation,
			@NotNull String email
	) {
		this.username = username;
		this.name = name;
		this.passwordHash = passwordHash;
		this.role = Objects.requireNonNullElse(role, Role.USER);
		this.creation = Objects.requireNonNullElse(creation, TimeUtils.now());
		this.email = email;
		this.notification = null;
	}

	public UserBuilder(
			@NotNull String username,
			@NotNull String name,
			Role role,
			LocalDate creation,
			@NotNull String email
	) {
		this.username = username;
		this.name = name;
		this.passwordHash = "";
		this.role = Objects.requireNonNullElse(role, Role.USER);
		this.creation = Objects.requireNonNullElse(creation, TimeUtils.now());
		this.email = email;
		this.notification = null;
	}

	public UserBuilder(@NotNull User user) {
		this.username = user.getUsername();
		this.name = user.getName();
		this.passwordHash = user.getPasswordHash();
		this.role = user.getRole();
		this.creation = user.getCreation();
		this.email = user.getEmail();
		this.notification = user.getNotification();
	}

	public UserBuilder setUsername(@NotNull String username) {
		this.username = username;
		return this;
	}

	public UserBuilder setName(@NotNull String name) {
		this.name = name;
		return this;
	}

	public UserBuilder setPassword(@NotNull String password) {
		this.passwordHash = PasswordHash.hash(password);
		return this;
	}

	public UserBuilder setRole(@NotNull Role role) {
		this.role = role;
		return this;
	}

	public UserBuilder setCreation(@NotNull LocalDate creation) {
		this.creation = creation;
		return this;
	}

	public UserBuilder setCreationNow() {
		this.creation = TimeUtils.now();
		return this;
	}

	public UserBuilder setCreationEpoch(long epoch) {
		this.creation = TimeUtils.fromEpoch(epoch);
		return this;
	}

	public UserBuilder setEmail(@NotNull String email) {
		this.email = email;
		return this;
	}

	public UserBuilder setNotification(String notification) {
		this.notification = notification;
		return this;
	}

	public User build() {
		return new User(username, name, passwordHash, role, creation, email, notification);
	}

}
