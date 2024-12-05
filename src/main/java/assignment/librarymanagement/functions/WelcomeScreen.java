package assignment.librarymanagement.functions;

import assignment.librarymanagement.data.User;
import assignment.librarymanagement.data.UserBuilder;
import assignment.librarymanagement.utils.DatabaseWrapper;
import assignment.librarymanagement.managers.UsersManager;
import assignment.librarymanagement.utils.EmailValidator;
import assignment.librarymanagement.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class WelcomeScreen {

	private final UsersManager usersManager;
	private User loggedInUser = null;

	public WelcomeScreen(DatabaseWrapper database) {
		this.usersManager = new UsersManager(database);
	}

	public void login(
			@NotNull String username,
			@NotNull String password,
			Runnable callback,
			Consumer<String> errorCallback
	) {
		if (username.length() < 4) {
			errorCallback.accept("Username must be at least 4 characters long");
		} else if (password.isBlank()) {
			errorCallback.accept("Password must contains at least one character");
		} else usersManager.select(username, user -> {
			if (user == null) {
				errorCallback.accept("User not found");
			} else if (!user.verifyPassword(password)) {
				errorCallback.accept("Incorrect password");
			} else {
				loggedInUser = user;
				callback.run();
			}
		}, e -> errorCallback.accept(e.toString()));
	}

	public void register(
			@NotNull String username,
			@NotNull String name,
			@NotNull String email,
			@NotNull String password,
			Runnable callback,
			Consumer<String> errorCallback
	) {
		if (username.length() < 4) {
			errorCallback.accept("Username must be at least 4 characters long");
		} else if (name.isBlank()) {
			errorCallback.accept("You must provide a name");
		} else if (!EmailValidator.check(email)) {
			errorCallback.accept("Invalid email");
		} else if (password.isBlank()) {
			errorCallback.accept("Password must contains at least one character");
		} else {
			UserBuilder builder = new UserBuilder(username, name, User.Role.USER, TimeUtils.now(), email);
			builder.setPassword(password);
			usersManager.insert(
					builder.build(),
					_ -> callback.run(),
					e -> errorCallback.accept(e.toString())
			);
		}
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

}
