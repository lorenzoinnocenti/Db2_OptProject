package it.polimi.db2.gamified.exceptions;

public class QuestionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public QuestionNotFoundException(String message) {
		super(message);
	}
}
