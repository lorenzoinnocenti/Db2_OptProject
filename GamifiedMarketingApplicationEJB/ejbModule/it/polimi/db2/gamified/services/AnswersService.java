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
	
	public void AddAllAnswer(int userid, int questionnaireid, List<String>answers_text) throws AccountNotFoundException, QuestionnaireNotFoundException{
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
				AddAnswer(userid, questions.get(i).getId(), answers_text.get(i));
			}
		}			
	}

	
	private void AddAnswer(int userid, int questionid, String answerText) {
		AnswerPK id = new AnswerPK();
		id.setUserid(userid);
		id.setQuestionid(questionid);
		Answer answer = em.find(Answer.class, id );
		id = null;
		answer.setAnswerText(answerText);
	}
	 
}