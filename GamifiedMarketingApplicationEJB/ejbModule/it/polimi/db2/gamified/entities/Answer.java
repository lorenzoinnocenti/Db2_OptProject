package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the answers database table.
 * 
 */
@Entity
@Table(name="answers")
@NamedQuery(name="Answer.findByQuestionByUser", query="SELECT a FROM Answer a WHERE a.id.userid = :usID and a.id.questionid = :questID")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AnswerPK id;

	@Column(name="answer_text", nullable=false, length=200)
	private String answerText;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="userid", nullable=false)
	private Account account;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="questionid", nullable=false)
	private Question question;

	public Answer() {
	}
	
	public Answer(AnswerPK id, String answerText, Account account, Question question) {
		this.id = id;
		this.answerText = answerText;
		this.account = account;
		this.question = question;
	}

	public AnswerPK getId() {
		return this.id;
	}

	public void setId(AnswerPK id) {
		this.id = id;
	}

	public String getAnswerText() {
		return this.answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}