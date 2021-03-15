package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;

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

@WebServlet("/CreateQuestionnaire")
public class CreateQuestionnaire extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	
	public CreateQuestionnaire() {
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
		Account adminAccount = (Account) session.getAttribute("account");
		if (session.isNew() || adminAccount == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		if (adminAccount.getStatus()==AccountStatus.USER) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		Integer questionnaireDateOk = (Integer) session.getAttribute("dateOk");
		String numberOfQuestions =(String) session.getAttribute("numberOfQuestions");
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		if(questionnaireDateOk == 1 && numberOfQuestions!=null) {
			ctx.setVariable("rightParameters", 1);
			System.out.println("the parameters are ok");
		}
		if(questionnaireDateOk == 0) {
			ctx.setVariable("wrongDate", 1);
			System.out.println("the date is wrong");

		}
		templateEngine.process("/WEB-INF/CreateQuestionnaire.html", ctx, response.getWriter()); 
	}
	
	
	public void destroy() {
	}
	
}

