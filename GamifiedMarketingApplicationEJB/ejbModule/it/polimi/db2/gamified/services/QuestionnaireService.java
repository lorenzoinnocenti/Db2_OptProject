package it.polimi.db2.gamified.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.util.List;

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public QuestionnaireService() {
	}
	
	public List<Questionnaire> findByDate(Date date) { 
		return em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class).setParameter("date", date).getResultList();
	}
	
	public void addQuestionnaire(Date date, int productId) throws ProductNotFoundException{ 
		Product p = em.find(Product.class, productId);
		if (p == null) throw new ProductNotFoundException("product not found");
		Questionnaire q = new Questionnaire();
		q.setDate(date);
		q.setProduct(p);
		em.persist(q);
	}
	
	public void addQuestion(int questionnaireId, String text) throws QuestionnaireNotFoundException{
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q == null) throw new QuestionnaireNotFoundException("questionnaire not found");
		Question question = new Question();
		question.setQuestionText(text);
		q.addQuestion(question);
		em.persist(q); 
	}
	
	public void removeQuestionnaire(int questionnaireId) throws QuestionnaireNotFoundException{
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q == null) throw new QuestionnaireNotFoundException("questionnaire not found");
		Product p = q.getProduct();
		p.getQuestionnaires().remove(q);
		em.remove(q);
	}
	
}