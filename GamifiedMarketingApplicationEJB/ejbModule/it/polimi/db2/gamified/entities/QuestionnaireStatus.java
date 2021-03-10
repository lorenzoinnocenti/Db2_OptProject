package it.polimi.db2.gamified.entities;

public enum QuestionnaireStatus {
	
	CANCELLED(0), FINISHED(1);

	private final int value;

	QuestionnaireStatus(int value) {
		this.value = value;
	}

	public static QuestionnaireStatus getMissionStatusFromInt(int value) {
		switch (value) {
		case 0:
			return QuestionnaireStatus.FINISHED;
		case 1:
			return QuestionnaireStatus.CANCELLED;
		}
		return null;
	}

	public int getValue() {
		return value;
	}
	
}
