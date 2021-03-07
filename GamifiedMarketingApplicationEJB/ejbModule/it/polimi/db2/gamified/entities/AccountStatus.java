package it.polimi.db2.gamified.entities;

public enum AccountStatus {
	USER(0), BANNED(1), ADMIN(2);

	private final int value;

	AccountStatus(int value) {
		this.value = value;
	}

	public static AccountStatus getMissionStatusFromInt(int value) {
		switch (value) {
		case 0:
			return AccountStatus.USER;
		case 1:
			return AccountStatus.BANNED;
		case 2:
			return AccountStatus.ADMIN;
		}
		return null;
	}

	public int getValue() {
		return value;
	}

}
