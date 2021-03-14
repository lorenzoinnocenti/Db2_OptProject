package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.entities.QuestionnaireStatus;
import it.polimi.db2.gamified.exceptions.BannedUserException;
import it.polimi.db2.gamified.services.AnswersService;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

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
			answers = Arrays.asList(request.getParameterValues("Answer"));
			//Escape answers
			for (String a : answers) {
				a =  StringEscapeUtils.escapeJava(a);
			}
			
		    aService.AddAllAnswer(accountId, questId, answers);
		    //If account is not banned, we can addUserQuestionnaire
		    uqService.addUserquestionnaire(accountId, questId, QuestionnaireStatus.FINISHED);		    
		    // return the user to the right view
		    path = ctxpath + "/Statistical";
		    System.out.println(aService +" "+  uqService+  " " + path);
		} catch (BannedUserException e) {
			//Redirect to logout
			path = ctxpath + "/Banned";	
		} catch (Exception e) {
			System.out.print("ciao");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong query.");
		}
		response.sendRedirect(path);
		return;
	}
	
	public void destroy() {
	}
}

