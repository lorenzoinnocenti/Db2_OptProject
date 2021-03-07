package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class QuestionService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public QuestionService() {
	}
	
	public void createQuestion(String questionText, int questionnaireId) throws QuestionnaireNotFoundException {
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		if (questionnaire == null) throw new QuestionnaireNotFoundException("Questionnaire not found.");
		Question question = new Question(questionText);
		
		// Update both sides of the relationships
		questionnaire.addQuestion(question);
		
		// Makes also question object managed via cascading
		em.persist(questionnaire);
	}
	
	public List<Question> findQuestionsByQuestionnaire (int questionnaireId) {
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		List<Question> questions = questionnaire.getQuestions();
		return questions;
	}
	
	public List<Question> findQuestionsByQuestionnaireRefresh (int questionnaireId) {
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		em.refresh(questionnaire);
		List<Question> questions = questionnaire.getQuestions();
		return questions;
	}
	
	public List<Question> findQuestionsByQuestionnaireNoCache (int questionnaireId) {
		List<Question> questions = em
				.createQuery("Select q from Question q where q.questionnaire.id = :questId", Question.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH").setParameter("questId", questionnaireId).getResultList();
		return questions;
	}
	
	public List<Question> findQuestionsByQuestionnaireJPQL (int questionnaireId) {
		List<Question> questions = em
				.createQuery("Select q from Question q where q.questionnaire.id = :questId", Question.class)
				.setParameter("questId", questionnaireId).getResultList();
		return questions;
	}
	
	public void removeQuestion (int questionId) throws QuestionNotFoundException {
		Question question = em.find(Question.class, questionId);
		if (question == null) throw new QuestionNotFoundException("Question not found.");
		Questionnaire questionnaire = question.getQuestionnaire();
		questionnaire.removeQuestion(question);
		em.remove(question);
	}
}
 