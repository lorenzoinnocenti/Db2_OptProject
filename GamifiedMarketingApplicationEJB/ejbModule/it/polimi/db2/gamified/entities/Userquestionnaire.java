package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the userquestionnaire database table.
 * 
 */
@Entity
@Table(name="userquestionnaire")
@NamedQueries({@NamedQuery(name="Userquestionnaire.findNonCancelled", query="SELECT u FROM Userquestionnaire u WHERE u.status = it.polimi.db2.gamified.entities.QuestionnaireStatus.FINISHED AND u.id.questionnaireid = :questID"),
			   @NamedQuery(name="Userquestionnaire.findCancelled", query="SELECT u FROM Userquestionnaire u WHERE u.status = it.polimi.db2.gamified.entities.QuestionnaireStatus.CANCELLED AND u.id.questionnaireid = :questID")})
public class Userquestionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserquestionnairePK id;

	@Column(name="answer_age", length=200)
	private String answerAge;

	@Column(name="answer_exp", length=200)
	private String answerExp;

	@Column(name="answer_sex", length=200)
	private String answerSex;

	private int score;

	@Column(nullable=false)
	private QuestionnaireStatus status;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="userid", nullable=false)
	private Account account;
	// prima era @JoinColumn(name="userid", nullable=false, insertable=false, updatable=false), sia in questo che in tutti campi delle pk anche di answers, ma dava errore
	
	
	//bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name="questionnaireid", nullable=false)
	private Questionnaire questionnaire;

	public Userquestionnaire() {
	}
	
	public Userquestionnaire(UserquestionnairePK id, Account account, Questionnaire questionnaire, QuestionnaireStatus status) {
		this.id = id;
		this.account = account;
		this.questionnaire = questionnaire;
		this.status = status;
	}

	public UserquestionnairePK getId() {
		return this.id;
	}

	public void setId(UserquestionnairePK id) {
		this.id = id;
	}

	public String getAnswerAge() {
		return this.answerAge;
	}

	public void setAnswerAge(String answerAge) {
		this.answerAge = answerAge;
	}

	public String getAnswerExp() {
		return this.answerExp;
	}

	public void setAnswerExp(String answerExp) {
		this.answerExp = answerExp;
	}

	public String getAnswerSex() {
		return this.answerSex;
	}

	public void setAnswerSex(String answerSex) {
		this.answerSex = answerSex;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public QuestionnaireStatus getStatus() {
		return this.status;
	}

	public void setStatus(QuestionnaireStatus status) {
		this.status = status;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

}