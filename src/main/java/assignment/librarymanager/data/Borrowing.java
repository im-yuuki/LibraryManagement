package assignment.librarymanager.data;

import assignment.librarymanager.utils.TimeUtils;

public class Borrowing {

	private final int id;  // -1 for new records
	private final int readerId;
	private final int documentId;
	private final long borrowTime;
	private final long dueTime;
	private final long returnTime; // -1 for not returned yet

	public Borrowing(int readerId, int documentId, long dueTime) {
		this.id = -1;
		this.readerId = readerId;
		this.documentId = documentId;
		this.borrowTime = TimeUtils.getTimestamp();
		this.dueTime = dueTime;
		this.returnTime = -1;
	}

	public Borrowing(int id, int readerId, int documentId, long borrowTime, long dueTime, long returnTime) {
		this.id = id;
		this.readerId = readerId;
		this.documentId = documentId;
		this.borrowTime = borrowTime;
		this.dueTime = dueTime;
		this.returnTime = returnTime;
	}

	public int getId() {
		return id;
	}

	public int getReaderId() {
		return readerId;
	}

	public int getDocumentId() {
		return documentId;
	}

	public long getBorrowTime() {
		return borrowTime;
	}

	public String getBorrowTimeFormatted() {
		return TimeUtils.representTimestamp(borrowTime);
	}

	public long getDueTime() {
		return dueTime;
	}

	public String getDueTimeFormatted() {
		return TimeUtils.representTimestamp(dueTime);
	}

	public long getReturnTime() {
		return returnTime;
	}

	public String getReturnTimeFormatted() {
		if (returnTime == -1) return "Not returned";
		return TimeUtils.representTimestamp(returnTime);
	}
}
