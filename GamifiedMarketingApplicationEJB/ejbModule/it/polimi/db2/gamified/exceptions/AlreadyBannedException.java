package it.polimi.db2.gamified.exceptions;

public class AlreadyBannedException extends Exception {
	private static final long serialVersionUID = 1L;

	public AlreadyBannedException(String message) {
		super(message);
	}
}
