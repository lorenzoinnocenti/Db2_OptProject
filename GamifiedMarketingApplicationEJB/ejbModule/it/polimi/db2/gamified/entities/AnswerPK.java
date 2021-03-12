package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the answers database table.
 * 
 */
@Embeddable
public class AnswerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int userid;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int questionid;

	public AnswerPK() {
	}
	
	public AnswerPK(int userId, int questionId) {
		this.userid = userId;
		this.questionid = questionId;
	}
	
	public int getUserid() {
		return this.userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getQuestionid() {
		return this.questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnswerPK)) {
			return false;
		}
		AnswerPK castOther = (AnswerPK)other;
		return 
			(this.userid == castOther.userid)
			&& (this.questionid == castOther.questionid);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userid;
		hash = hash * prime + this.questionid;
		
		return hash;
	}
}