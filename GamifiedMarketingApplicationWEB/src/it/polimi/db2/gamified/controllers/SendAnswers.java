package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.services.AnswersService;
import it.polimi.db2.gamified.services.AnswerStateService;
import it.polimi.db2.gamified.entities.QuestionnaireStatus;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.exceptions.BannedUserException;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

//Load the answers (Marketing + Statistical) into the DB. Create an UserQuestionnaire instance

@WebServlet("/SendAnswers")
public class SendAnswers extends HttpServlet{
	private static final long serialVersionUID = 1L;
	//private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;
	@EJB(name = "it.polimi.db2.gamified.services/AnswersService")
	private AnswersService aService;

	public SendAnswers() {
		super();
	}
	
	public void init() throws ServletException {
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if (session.isNew() || account == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/index.html");
			return;
		}
		if (account.getStatus()==AccountStatus.ADMIN) {
			response.sendRedirect(getServletContext().getContextPath() + "/AdminPage");
			return;
		}
		
		String ctxpath = getServletContext().getContextPath();
		int questId = qService.findByDate(new Date(java.lang.System.currentTimeMillis())).get(0).getId();
		int accountId = ((Account)session.getAttribute("account")).getId();
		List<String> answers = null;
		String path = null;
		try {
			//Get answers
			AnswerStateService asService = null;
			asService = (AnswerStateService) request.getSession().getAttribute("AnswerStateService");
			answers = asService.getAnswerList();
		    aService.AddAllAnswer(accountId, questId, answers);
		    //If account is not banned, we can addUserQuestionnaire
		    uqService.addUserquestionnaire(accountId, questId, QuestionnaireStatus.FINISHED);		    
			//Clearing the list
			asService.setAnswers(null);
			//Adding statistical info
			String age_answer = request.getParameter("age_answer");
			String sex_answer = request.getParameter("sex_answer");
			String expertise_answer = request.getParameter("expertise_answer");
			uqService.SetStatisticalAttributes(questId, account.getId(), age_answer, sex_answer, expertise_answer);
			path = ctxpath + "/Greetings";
		} catch (BannedUserException e) {
			//Redirect to logout
			path = ctxpath + "/Banned";	
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong query.");
		}
		response.sendRedirect(path);
		return;
	}
	
	public void destroy() {
	}
	
}
