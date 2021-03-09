package it.polimi.db2.gamified.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.ArrayList; // import the ArrayList class

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;



@Stateless
public class UserQuestionnaireService {
	
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public UserQuestionnaireService(){		
	}

	
	public List<Account> findUsersByQuestionnaireId(int questionnaireId) throws UserNotFoundException{
		List<Userquestionnaire> questionnaires = em.createNamedQuery("Userquestionnaire.findNonCancelled", Userquestionnaire.class).getResultList();
		if(questionnaires != null) {
			List<Account> users_account = new ArrayList<Account>();
			for (Userquestionnaire u: questionnaires)
				users_account.add(u.getAccount());
			return users_account;
		}
		else
			throw new UserNotFoundException("There is no user which has compiled a such a questionnaire");
	}
	
	public List<Account> FindUsersByQuestionnaireCancelled(int questionnaireId) throws UserNotFoundException{
		List<Userquestionnaire> questionnaires = em.createNamedQuery("Userquestionnaire.findCancelled", Userquestionnaire.class).getResultList();
		if(questionnaires != null) {
			List<Account> users_account = new ArrayList<Account>();
			for (Userquestionnaire u: questionnaires)
				users_account.add(u.getAccount());
			return users_account;
		}
		else
			throw new UserNotFoundException("There is no user which has cancelled a such a questionnaire");	
	}
	
}
