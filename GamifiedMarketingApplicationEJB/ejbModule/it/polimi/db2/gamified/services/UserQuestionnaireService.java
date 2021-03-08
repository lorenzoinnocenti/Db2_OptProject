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
		List<Userquestionnaire> UsersQuestionnairesMapping = em.find(Questionnaire.class, questionnaireId).getUserquestionnaires();		
		List<Account> users = new ArrayList<Account>();
		for (Userquestionnaire mapping : UsersQuestionnairesMapping) {
			if(mapping.getStatus() != 0)
				users.add(mapping.getAccount());
		}
		if (! users.isEmpty())
			return users;
		else
			throw new UserNotFoundException("no user has compiled and submitted that questionnaire");
	}
	
	public List<Account> FindUsersByQuestionnaireCancelled(int questionnaireId) throws UserNotFoundException{
		List<Userquestionnaire> UsersQuestionnairesMapping = em.find(Questionnaire.class, questionnaireId).getUserquestionnaires();
		List<Account> users = new ArrayList<Account>();
		for (Userquestionnaire mapping : UsersQuestionnairesMapping) {
			if(mapping.getStatus() == 0)
				users.add(mapping.getAccount());
		}
		if (! users.isEmpty())
			return users;
		else
			throw new UserNotFoundException("there are no users which have cancelled their questionnaire");			
	}
	
}
