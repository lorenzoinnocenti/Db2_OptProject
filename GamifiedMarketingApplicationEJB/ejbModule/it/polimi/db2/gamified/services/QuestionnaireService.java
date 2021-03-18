package it.polimi.db2.gamified.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import java.sql.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	@EJB(name="it.polimi.db2.gamified.services/ProductService")
	private ProductService pService;
	
	public QuestionnaireService() {
	}
	
	public List<Questionnaire> findByDate(Date date) { 
		// maybe it needs hint refresh
		return em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class).setParameter("date", date).getResultList();
	}
	
	public List<Questionnaire> findPastQuestionnaires(Date date) { 
		return em.createNamedQuery("Questionnaire.findPastQuestionnaires", Questionnaire.class)
				.setParameter("date", date)
				.setHint("javax.persistence.cache.storeMode", "REFRESH") // refresh in case a new questionnaire is present, to keep them ordered
				.getResultList();
	}
	
	public void addQuestionnaire(Date date, int productId) throws ProductNotFoundException, QuestionnaireAlreadyPresentException { 
		//E' possibile avere massimo un questionario per giorno
		List<Questionnaire> q = findByDate(date);
		if (!q.isEmpty()) throw new QuestionnaireAlreadyPresentException("There is already a questionnaire on this date.");
		Product p = em.find(Product.class, productId);
		if (p == null) throw new ProductNotFoundException("Product not found");
		Questionnaire questionnaire = new Questionnaire(date, p);
		em.persist(questionnaire);
	}
	
	public void addQuestion(int questionnaireId, String text) throws QuestionnaireNotFoundException{
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q == null) throw new QuestionnaireNotFoundException("Questionnaire not found");
		Question question = new Question(text);
		q.addQuestion(question);
		em.persist(q); 
	}
	
	public void removeQuestionnaire(int questionnaireId) throws QuestionnaireNotFoundException{
		Questionnaire q = em.find(Questionnaire.class, questionnaireId);
		if (q == null) throw new QuestionnaireNotFoundException("Questionnaire not found");
		Product p = q.getProduct();
		p.getQuestionnaires().remove(q);
		em.remove(q);
	}
	
	public void addQuestionnaireNewProduct(String ProductName, BigDecimal ProductPrice, 
			String ProductDescription, byte[] imgByteArray, Date date) throws ProductAlreadyExistingException {
		List<Product> products = em.createNamedQuery("Product.findByName", Product.class).setParameter("prodName", ProductName).getResultList();
		if(!products.isEmpty())
			throw  new ProductAlreadyExistingException("The products with that name already exists");
		Product newProduct = new Product(ProductName, ProductPrice, ProductDescription, imgByteArray);
		List<Questionnaire> questionnaires = new ArrayList<Questionnaire>();
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setDate(date);
		questionnaire.setProduct(newProduct);
		questionnaires.add(questionnaire);
		newProduct.setQuestionnaires(questionnaires);
		em.flush();
		em.persist(questionnaire);
	}
	
}