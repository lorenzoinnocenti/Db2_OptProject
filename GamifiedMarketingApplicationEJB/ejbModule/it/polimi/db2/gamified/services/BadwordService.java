package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.*;

@Stateless
public class BadwordService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public BadwordService() {
	}
	
	//Controlla se la lista di risposte contiene parolacce, e nel caso ritorna null; altrimenti ritorna la lista di risposte
	public List<String> checkBadword(List<String> answers) {
		List<Badword> badwords = em.createNamedQuery("Badword.findAll", Badword.class).getResultList();
		
		for (String answer : answers) {
			for (Badword badword : badwords) {
				if (answer.contains(badword.getWord())) {
					return null;
				}
			}
		}
		return answers;
	}
}
