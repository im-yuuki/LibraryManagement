package assignment.librarymanager.data;

import assignment.librarymanager.utils.PasswordHash;
import assignment.librarymanager.utils.TimeUtils;
import org.jetbrains.annotations.Nullable;

public class Reader {

	private final int id;  // -1 for new records
	private final String name;
	private final String passwordHash;
	private final long registrationTime;
	private final long expirationTime;
	private final String email;
	private final String phoneNumber;

	public Reader(String name, String password, long expirationTime, @Nullable String email, @Nullable String phoneNumber) {
		this.id = -1;
		this.name = name;
		this.email = email;
		this.passwordHash = PasswordHash.hash(password);
		this.registrationTime = TimeUtils.getTimestamp();
		this.expirationTime = expirationTime;
		this.phoneNumber = phoneNumber;
	}

	public Reader(int id, String name, String passwordHash, long registrationTime, long expirationTime, String email, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.passwordHash = passwordHash;
		this.registrationTime = registrationTime;
		this.expirationTime = expirationTime;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getRegistrationTime() {
		return registrationTime;
	}

	public String getRegistrationTimeFormatted() {
		return TimeUtils.representTimestamp(registrationTime);
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public String getExpirationTimeFormatted() {
		return TimeUtils.representTimestamp(expirationTime);
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public boolean verifyPassword(String password) {
		return PasswordHash.verify(password, passwordHash);
	}

	public String getPasswordHash() {
		return passwordHash;
	}

}
