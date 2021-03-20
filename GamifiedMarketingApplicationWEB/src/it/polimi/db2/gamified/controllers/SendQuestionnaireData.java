package it.polimi.db2.gamified.controllers;

import java.io.IOException;
//import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
//import java.util.List;
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
import it.polimi.db2.gamified.entities.Questionnaire;
import it.polimi.db2.gamified.exceptions.ProductNotFoundException;
import it.polimi.db2.gamified.exceptions.QuestionnaireAlreadyPresentException;
import it.polimi.db2.gamified.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

@WebServlet("/SendQuestionnaireData")
public class SendQuestionnaireData extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	//private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;

	public SendQuestionnaireData() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if (session.isNew() || account == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/index.html");
			return;
		}
		if (account.getStatus() == AccountStatus.USER) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		int productId = Integer.parseInt(request.getParameter("productId"));
		List<String> questions = null;
		questions = Arrays.asList(request.getParameterValues("Questions"));
		Date questionnaireDate = (Date) session.getAttribute("questionnaireDate");
		int questionnaireId = 0;
		try {
			questionnaireId = qService.addQuestionnaireReturnId(questionnaireDate, productId);
			
		} catch (ProductNotFoundException | QuestionnaireAlreadyPresentException e) {
			e.printStackTrace();
		}
		try { 
			for(String q : questions) {				
					qService.addQuestion(questionnaireId, q);
			}
		} catch (QuestionnaireNotFoundException e) {
			e.printStackTrace();
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/QuestionnaireSaved";
		 response.sendRedirect(path);		
	}

	public void destroy() {
	}
}