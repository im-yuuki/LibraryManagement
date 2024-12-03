import assignment.librarymanager.data.User;
import assignment.librarymanager.managers.Database;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReaderStorageTest {

	private String generateRandomString() {
		int leftLimit = 97;
		int rightLimit = 122;
		int targetStringLength = 10;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int)
				(random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}

	private final int targetReaderId;
	private final ReaderStorage readerStorage = new ReaderStorage(new Database());

	public ReaderStorageTest() throws SQLException, IOException {
		User reader = new User("Test", "123", 999999999, generateRandomString() , generateRandomString());
		User createdReader = readerStorage.setEntry(reader);
		assertEquals(reader.getName(), createdReader.getName());
		assertEquals(reader.getPasswordHash(), createdReader.getPasswordHash());
		assertEquals(reader.getRegistrationTime(), createdReader.getRegistrationTime());
		assertEquals(reader.getExpirationTime(), createdReader.getExpirationTime());
		assertEquals(reader.getEmail(), createdReader.getEmail());
		assertEquals(reader.getPhoneNumber(), createdReader.getPhoneNumber());
		targetReaderId = createdReader.getId();
	}


	@Test
	public void testLogin() throws SQLException {
		User reader = readerStorage.getEntry(targetReaderId);
		assertNotNull(reader);
		assertEquals("Test", reader.getName());
		assertTrue(reader.verifyPassword("123"));
	}

	@Test
	public void testUpdateReader() throws SQLException {
		User reader = readerStorage.getEntry(targetReaderId);
		assertNotNull(reader);
		User newReader = new User(targetReaderId, "Test2", reader.getPasswordHash(), reader.getRegistrationTime(), reader.getExpirationTime(), reader.getEmail(), reader.getPhoneNumber());
		User updatedReader = readerStorage.setEntry(newReader);
		assertEquals(newReader.getName(), updatedReader.getName());
		assertEquals(newReader.getPasswordHash(), updatedReader.getPasswordHash());
		assertEquals(newReader.getRegistrationTime(), updatedReader.getRegistrationTime());
		assertEquals(newReader.getExpirationTime(), updatedReader.getExpirationTime());
		assertEquals(newReader.getEmail(), updatedReader.getEmail());
		assertEquals(newReader.getPhoneNumber(), updatedReader.getPhoneNumber());
	}

	@Test
	public void testDeleteReader() throws SQLException {
		User reader = readerStorage.getEntry(targetReaderId);
		assertNotNull(reader);
		readerStorage.deleteEntry(targetReaderId);
		assertNull(readerStorage.getEntry(targetReaderId));
	}

}