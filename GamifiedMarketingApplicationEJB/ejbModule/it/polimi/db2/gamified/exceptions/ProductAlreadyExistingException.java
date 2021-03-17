package it.polimi.db2.gamified.exceptions;

public class ProductAlreadyExistingException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProductAlreadyExistingException(String message) {
		super(message);
	}
}