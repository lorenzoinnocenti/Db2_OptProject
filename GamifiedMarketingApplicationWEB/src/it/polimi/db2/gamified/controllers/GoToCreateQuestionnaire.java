package it.polimi.db2.gamified.controllers;

import java.io.IOException;

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

//Show the page for creating a questionnaire. The page will show differently based on some parameters.

@WebServlet("/CreateQuestionnaire")
public class GoToCreateQuestionnaire extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	
	public GoToCreateQuestionnaire() {
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
		//Parameters coming from the session.
		Integer questionnaireDateOk = (Integer) session.getAttribute("dateOk");
		Integer numberOfQuestions =(Integer) session.getAttribute("numberOfQuestions");
		Integer alreadyExisting = (Integer) session.getAttribute("alreadyExisting");
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		//If it is the first time that this servlet is called, the parameters are set to null
		if(questionnaireDateOk!=null && numberOfQuestions!=null && alreadyExisting != null) {
			if(questionnaireDateOk == 1) {
				if (alreadyExisting == 0) {
					this.removeSessionAttributes(session);
					String path = getServletContext().getContextPath() + "/GoToProductAndQuestions";
					response.sendRedirect(path);
					return;
				}
				else
					ctx.setVariable("questAlreadyExists", Integer.valueOf(1));
			}
			else{
				ctx.setVariable("wrongDate", Integer.valueOf(1));
			}
			ctx.setVariable("wrongParameters", Integer.valueOf(0));
		}
		templateEngine.process("/WEB-INF/CreateQuestionnaire.html", ctx, response.getWriter()); 
	}
	
	
	private void removeSessionAttributes(HttpSession session) {
		//Clear the parameters
		session.removeAttribute("dateOk");
		session.removeAttribute("alreadyExisting");
	}
	
	public void destroy() {
	}
	
}

