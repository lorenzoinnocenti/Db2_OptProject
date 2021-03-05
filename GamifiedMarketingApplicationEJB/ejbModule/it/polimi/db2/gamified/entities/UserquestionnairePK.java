package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the userquestionnaire database table.
 * 
 */
@Embeddable
public class UserquestionnairePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int userid;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int questionnaireid;

	public UserquestionnairePK() {
	}
	public int getUserid() {
		return this.userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getQuestionnaireid() {
		return this.questionnaireid;
	}
	public void setQuestionnaireid(int questionnaireid) {
		this.questionnaireid = questionnaireid;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserquestionnairePK)) {
			return false;
		}
		UserquestionnairePK castOther = (UserquestionnairePK)other;
		return 
			(this.userid == castOther.userid)
			&& (this.questionnaireid == castOther.questionnaireid);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userid;
		hash = hash * prime + this.questionnaireid;
		
		return hash;
	}
}