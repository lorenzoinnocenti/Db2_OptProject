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
		//Only used to find already ended questionnaires. No need to refresh
		List<Userquestionnaire> questionnaires = em.createNamedQuery("Userquestionnaire.findNonCancelled", Userquestionnaire.class).setParameter("questID", questionnaireId).getResultList();
		if(questionnaires.size() != 0) {
			List<Account> users_account = new ArrayList<Account>();
			for (Userquestionnaire u: questionnaires)
				users_account.add(u.getAccount());
			return users_account;
		}
		else
			throw new UserNotFoundException("There is no user which has compiled a such a questionnaire");
	}
	
	public List<Account> FindUsersByQuestionnaireCancelled(int questionnaireId) throws UserNotFoundException{
		//Only used to find already ended questionnaires. No need to refresh
		List<Userquestionnaire> questionnaires = em.createNamedQuery("Userquestionnaire.findCancelled", Userquestionnaire.class).setParameter("questID", questionnaireId).getResultList();
		if(questionnaires.size() != 0) {
			List<Account> users_account = new ArrayList<Account>();
			for (Userquestionnaire u: questionnaires)
				users_account.add(u.getAccount());
			return users_account;
		}
		else
			throw new UserNotFoundException("There is no user which has cancelled a such a questionnaire");	
	}
	
	public Userquestionnaire getUserQuestionnaire(int accountId, int questionnaireId) throws UserQuestionnaireNotFoundException{
		Userquestionnaire userquest = em.find(Userquestionnaire.class, new UserquestionnairePK(accountId, questionnaireId));
		if (userquest == null) throw new UserQuestionnaireNotFoundException("Userquestionnaire not found");
		em.refresh(userquest);
		return userquest;
	}
	
	public Userquestionnaire addUserquestionnaire(int accountid, int questionnaireid, QuestionnaireStatus status) throws  AccountNotFoundException, QuestionnaireNotFoundException{
		Account user = em.find(Account.class, accountid);
		if (user == null )
			throw new AccountNotFoundException("Account not found");
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireid);
		if (questionnaire == null)
			throw new QuestionnaireNotFoundException("Questionnaire not found");
		UserquestionnairePK id = new UserquestionnairePK(accountid, questionnaireid);
		Userquestionnaire new_userquestionnaire = new Userquestionnaire(id, user, questionnaire, status);
		em.persist(new_userquestionnaire);
		return new_userquestionnaire;
	}
	
	public void SetStatisticalAttributes(int questionnaireId, int userId, String age_answer, String sex_answer, String expertise_answer) {
		Userquestionnaire userquestionnaire = em.find(Userquestionnaire.class, new UserquestionnairePK(userId, questionnaireId));
		if(!age_answer.equals("null"))
			userquestionnaire.setAnswerAge(age_answer);
		if(!sex_answer.equals("null"))
			userquestionnaire.setAnswerSex(sex_answer);
		if(!expertise_answer.equals("null"))
			userquestionnaire.setAnswerExp(expertise_answer);
	}
	
}
