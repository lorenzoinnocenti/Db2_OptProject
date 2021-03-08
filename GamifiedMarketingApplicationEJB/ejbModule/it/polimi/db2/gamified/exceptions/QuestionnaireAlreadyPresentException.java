package it.polimi.db2.gamified.exceptions;

public class QuestionnaireAlreadyPresentException extends Exception {
	private static final long serialVersionUID = 1L;

	public QuestionnaireAlreadyPresentException(String message) {
		super(message);
	}
}
