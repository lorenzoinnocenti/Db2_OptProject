package it.polimi.db2.gamified.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.services.AccountService;
import it.polimi.db2.gamified.services.AnswerStateService;
import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.exceptions.CredentialsException;
import it.polimi.db2.gamified.exceptions.BannedUserException;
import javax.persistence.NonUniqueResultException;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/AccountService")
	private AccountService accService;

	public CheckLogin() {
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
		// obtain and escape params
		String usrn = null;
		String pwd = null;
		String path;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		Account account;
		try {
			// query db to authenticate for user
			account = accService.checkCredentials(usrn, pwd);
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Non unique result");
			return;		
		} catch (CredentialsException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credentials exception");
			return;
		} catch (BannedUserException e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Your account has been terminated");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		
		if (account == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			request.getSession().setAttribute("account", account);
			if (account.getStatus()==AccountStatus.USER) {
				AnswerStateService asService = null;
				try {
					InitialContext ic = new InitialContext();
					asService = (AnswerStateService) ic.lookup("java:/openejb/local/GamifiedMarketingApplicationWEB/GamifiedMarketingApplicationWEB/AnswerStateServiceLocalBean");
					// asService = (AnswerStateService) ic.lookup("java:/openejb/local/AnswerStateServiceLocalBean");
					request.getSession().setAttribute("AnswerStateService", asService);
				} catch (Exception e) {
					e.printStackTrace();
				}
				path = getServletContext().getContextPath() + "/Home";
				response.sendRedirect(path);
			}
			if (account.getStatus()==AccountStatus.ADMIN) {
				path = getServletContext().getContextPath() + "/AdminPage";
				response.sendRedirect(path);
			}
		}

	}

	public void destroy() {
	}
}