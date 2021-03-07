package it.polimi.db2.gamified.exceptions;

public class BannedUserException extends Exception {
	private static final long serialVersionUID = 1L;

	public BannedUserException(String message) {
		super(message);
	}
}
