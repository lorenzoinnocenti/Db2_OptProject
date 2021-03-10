package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the questionnaire database table.
 * 
 */
@Entity
@Table(name="questionnaire")


@NamedQueries({ @NamedQuery(name="Questionnaire.findAll", query="SELECT q FROM Questionnaire q"),
				@NamedQuery(name = "Questionnaire.findByDate", query = "SELECT q FROM Questionnaire q WHERE q.date = :date"),
    			@NamedQuery(name = "Questionnaire.findPastQuestionnaires", query = "SELECT q FROM Questionnaire q WHERE q.date < :date ORDER BY q.date DESC")})
 

public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="QUESTIONNAIRE_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="QUESTIONNAIRE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date;

	@Column(nullable=false)
	private int maxscore;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="questionnaire",cascade = CascadeType.ALL)
	private List<Question> questions;

	//bi-directional many-to-one association to Product
	@ManyToOne(fetch=FetchType.EAGER) 
	// needed to display the product name with the questionnaire. Actually, EAGER is default for ManyToOne realtion.
	@JoinColumn(name="productid", nullable=false)
	private Product product;

	//bi-directional many-to-one association to Userquestionnaire
	@OneToMany(mappedBy="questionnaire",cascade = CascadeType.REMOVE)
	private List<Userquestionnaire> userquestionnaires;

	public Questionnaire() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getMaxscore() {
		return this.maxscore;
	}

	public void setMaxscore(int maxscore) {
		this.maxscore = maxscore;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setQuestionnaire(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setQuestionnaire(null);

		return question;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Userquestionnaire> getUserquestionnaires() {
		return this.userquestionnaires;
	}

	public void setUserquestionnaires(List<Userquestionnaire> userquestionnaires) {
		this.userquestionnaires = userquestionnaires;
	}

	public Userquestionnaire addUserquestionnaire(Userquestionnaire userquestionnaire) {
		getUserquestionnaires().add(userquestionnaire);
		userquestionnaire.setQuestionnaire(this);

		return userquestionnaire;
	}

	public Userquestionnaire removeUserquestionnaire(Userquestionnaire userquestionnaire) {
		getUserquestionnaires().remove(userquestionnaire);
		userquestionnaire.setQuestionnaire(null);

		return userquestionnaire;
	}

}