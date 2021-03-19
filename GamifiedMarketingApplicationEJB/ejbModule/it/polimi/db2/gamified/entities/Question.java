package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@Table(name="question")
@NamedQuery(name = "Question.findByQuestionnaire", query = "Select q FROM Question q WHERE q.questionnaire.id = :questId")

public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=200)
	private String questionText;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="question",cascade = CascadeType.REMOVE)
	private List<Answer> answers;

	//bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name="questionnaireid", nullable=false)
	private Questionnaire questionnaire;

	public Question() {
	}

	public Question(String text) {
		this.questionText = text;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestionText() {
		return this.questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setQuestion(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setQuestion(null);

		return answer;
	}

	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

}