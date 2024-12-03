package assignment.librarymanager.data;

import assignment.librarymanager.data.User.Role;
import assignment.librarymanager.utils.PasswordHash;

import java.time.LocalDate;

public class UserBuilder {

	private String username;
	private String name;
	private String passwordHash;
	private Role role;
	private LocalDate creation;
	private String email;
	private String notice;

	public UserBuilder() {
		this.username = null;
		this.name = null;
		this.passwordHash = null;
		this.role = Role.USER;
		this.creation = LocalDate.now();
		this.email = null;
		this.notice = null;
	}

	public UserBuilder(User user) {
		this.username = user.getUsername();
		this.name = user.getName();
		this.passwordHash = user.getPasswordHash();
		this.role = user.getRole();
		this.creation = user.getCreation();
		this.email = user.getEmail();
		this.notice = user.getNotice();
	}

	public UserBuilder setUsername(String username) {
		this.username = username;
		return this;
	}

	public UserBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public UserBuilder setPassword(String password) {
		this.passwordHash = PasswordHash.hash(password);
		return this;
	}

	public UserBuilder setRole(Role role) {
		this.role = role;
		return this;
	}

	public UserBuilder setCreation(LocalDate creation) {
		this.creation = creation;
		return this;
	}

	public UserBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public UserBuilder setNotice(String notice) {
		this.notice = notice;
		return this;
	}

	public User build() {
		if (username == null || name == null || passwordHash == null || email == null) {
			throw new IllegalStateException("Missing required fields");
		}
		return new User(username, name, passwordHash, role, creation, email, notice);
	}

}
