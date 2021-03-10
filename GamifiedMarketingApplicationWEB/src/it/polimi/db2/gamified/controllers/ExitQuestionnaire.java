package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.QuestionnaireStatus;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

@WebServlet("/ExitQuestionnaire")
public class ExitQuestionnaire extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;

	public ExitQuestionnaire() {
		super();
	}
	
	public void init() throws ServletException {
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		Account u = (Account) session.getAttribute("account");
		if (session.isNew() || u == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		try {
			uqService.addUserquestionnaire(((Account)session.getAttribute("account")).getId(), qService.findByDate(new Date(java.lang.System.currentTimeMillis())).get(0).getId(), QuestionnaireStatus.CANCELLED);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Questionnaire not found");
		}

		// return the user to the right view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Home";
		response.sendRedirect(path);
	}
	
	public void destroy() {
	}
}
