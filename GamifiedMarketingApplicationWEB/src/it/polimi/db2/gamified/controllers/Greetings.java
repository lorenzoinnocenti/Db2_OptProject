package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;

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
import it.polimi.db2.gamified.entities.Questionnaire;
import it.polimi.db2.gamified.entities.Userquestionnaire;
import it.polimi.db2.gamified.exceptions.UserQuestionnaireNotFoundException;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

//Load the greeting page

@WebServlet("/Greetings")
public class Greetings extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;

	public Greetings() {
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
		
		int newPoints = 0;
		Questionnaire q = qService.findByDate(new Date(java.lang.System.currentTimeMillis())).get(0);
		Userquestionnaire uq = null;
		try {
			uq = uqService.getUserQuestionnaire(((Account) session.getAttribute("account")).getId(), q.getId());
		} catch (UserQuestionnaireNotFoundException e) {
			//We should never reach this block
			e.printStackTrace();
		}		
		if (uq != null) {
			newPoints = uq.getScore();
		}
		
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("newPoints", newPoints);
		templateEngine.process("/WEB-INF/Greetings.html", ctx, response.getWriter()); 
	}
	
	public void destroy() {
	}
	
}
