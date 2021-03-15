package it.polimi.db2.gamified.exceptions;

public class UsernameAlreadyUsedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyUsedException(String message) {
		super(message);
	}
}
