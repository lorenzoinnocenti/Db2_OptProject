package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.exceptions.UserNotFoundException;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.ReviewService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

@WebServlet("/UsersResults")
public class UsersResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionService")
	private ReviewService questionService;
	@EJB(name = "it.polimi.db2.gamified.services/AnswersService")
	private ReviewService answersService;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;
	
	
    public UsersResults() {
        super();
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			questionnaireId = Integer.parseInt(request.getParameter("questionnaireid"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		List<Account> userCompleted;
		List<Account> userCancelled;
		try {
			userCompleted = uqService.findUsersByQuestionnaireId(questionnaireId);
		} catch (UserNotFoundException e) {
			userCompleted = null;
		}
		try {
			userCancelled = uqService.FindUsersByQuestionnaireCancelled(questionnaireId);
		} catch (UserNotFoundException e) {
			userCancelled = null;
		}
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("usercompleted", userCompleted);
		ctx.setVariable("usercancelled", userCancelled);
		ctx.setVariable("questionnaireid", questionnaireId);
		templateEngine.process("/WEB-INF/UsersResults.html", ctx, response.getWriter()); 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
