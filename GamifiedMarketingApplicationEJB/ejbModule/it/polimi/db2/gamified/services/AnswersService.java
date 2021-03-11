package it.polimi.db2.gamified.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.ArrayList; // import the ArrayList class

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class AnswersService {
	
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	@EJB(name="it.polimi.db2.gamified.services/BadwordService")
	BadwordService badwords;
	
	@EJB(name="it.polimi.db2.gamified.services/AccountService")
	AccountService accountService;
	
	public AnswersService(){
		}
	
	public List<Answer> FindAnswersByUserByQuestionnaire(int userID, int questionnaireID) throws AnswersNotFoundException{
		List<Question> questions = em.find(Questionnaire.class, questionnaireID).getQuestions();
		List<Answer> answers = new ArrayList<Answer>();
		Answer answer = null;
		for (Question q: questions) {
			answer = em.createNamedQuery("Answer.findByQuestionByUser", Answer.class)
					.setParameter("usID", userID)
					.setParameter("questID", q.getId())
					.getResultList().get(0);
			if (answer != null)
				answers.add(answer);
		}
		if(!answers.isEmpty())
			return answers;
		else
			throw new AnswersNotFoundException("It's impossible to find the answers given the user and the questionnaire");
		}
	
	public void AddAllAnswer(int userid, int questionnaireid, List<String>answers_text) throws BannedUserException, AccountNotFoundException, QuestionnaireNotFoundException, QuestionNotFoundException, AlreadyBannedException, BanAdminException{
		Account user = em.find(Account.class, userid);
		if (user == null) 
			throw new AccountNotFoundException("User not found");
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireid);
		if (questionnaire == null)
			throw new QuestionnaireNotFoundException("questionnaire not found");
		List<Question> questions = questionnaire.getQuestions();
		int len = questions.size();
		assert(len == answers_text.size());
		List<String> checkBadWords = badwords.checkBadword(answers_text);	
		if(checkBadWords != null) {
			for (int i=0; i<len; i++) {
				AddAnswer(userid, questions.get(i).getId(), answers_text.get(i), user);
			}
		}
		else {
			accountService.banUser(userid);
			throw new BannedUserException("The user has been banned");
		}
	}
	
	private void AddAnswer(int userid, int questionid, String answerText, Account user) throws QuestionNotFoundException {
		Question question = em.find(Question.class, questionid);
		if(question == null)
			throw new QuestionNotFoundException("It is impossible to find such a question");
		AnswerPK id = new AnswerPK();
		id.setUserid(userid);
		id.setQuestionid(questionid);
		Answer answer = new Answer();
		answer.setId(id);
		answer.setAnswerText(answerText);
		answer.setAccount(user);
		answer.setQuestion(question);
		em.persist(answer);
	}
	
	private List<Answer> findAnsByUserOrdered(int userID) {
		return em.createNamedQuery("Answer.findAnsByUserOrdered", Answer.class)
				.setParameter("usID", userID)
				.setHint("javax.persistence.cache.storeMode", "REFRESH") 
				.getResultList();
	}
	 
}
