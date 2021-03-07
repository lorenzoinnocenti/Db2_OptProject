package it.polimi.db2.gamified.exceptions;

public class AccountNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String message) {
		super(message);
	}
}
