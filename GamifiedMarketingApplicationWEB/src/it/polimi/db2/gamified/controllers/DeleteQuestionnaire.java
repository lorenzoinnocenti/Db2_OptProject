package it.polimi.db2.gamified.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.exceptions.QuestionnaireNotFoundException;

//Remove a questionnaire from the DB (Cascade all questions, answers, userquestionnaires, and removes points via trigger)

@WebServlet("/DeleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;

	public DeleteQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if (session.isNew() || account == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/index.html");
			return;
		}
		if (account.getStatus()==AccountStatus.USER) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		Integer questionnaireId = null;
		try {
			//Getting the Id from the URL
			questionnaireId = Integer.parseInt(request.getParameter("questionnaireid"));
		} catch (NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		try {
			qService.removeQuestionnaire(questionnaireId);
		} catch (QuestionnaireNotFoundException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Wrong questionnaire id");
			return;
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/DeleteQuestionnairePage";
		response.sendRedirect(path);
	}

	public void destroy() {
	}
}
