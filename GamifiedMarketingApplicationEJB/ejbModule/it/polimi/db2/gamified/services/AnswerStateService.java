package it.polimi.db2.gamified.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class AnswerStateService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB", type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private List<String> answers;
	
	public AnswerStateService() {
	}
	
	public void addAnswers(String ans) {
		answers.add(ans);
	}
	
	public List<String> getAnswerList() {
		return answers;
	}
	
	public void setAnswers(List<String> a) {
		answers = a;
	}
	
	@Remove
	public void remove() {}
}
