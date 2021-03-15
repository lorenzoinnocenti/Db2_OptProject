package it.polimi.db2.gamified.controllers;

import java.io.IOException;

import javax.ejb.EJB;
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
import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.exceptions.CredentialsException;
import it.polimi.db2.gamified.exceptions.EmailAlreadyUsedException;
import it.polimi.db2.gamified.exceptions.UsernameAlreadyUsedException;
import it.polimi.db2.gamified.exceptions.BannedUserException;
import javax.persistence.NonUniqueResultException;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/AccountService")
	private AccountService accService;

	public SignUp() {
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
		String usrn = null;
		String pwd = null;
		String email = null;
		String path;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			if (usrn == null || pwd == null || email == null || usrn.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		try {
			accService.createNewAccount(usrn, pwd, email);
		} catch (UsernameAlreadyUsedException e) {
			ctx.setVariable("errorMsgSignup", "Username already in use");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		} catch (EmailAlreadyUsedException e) {
			ctx.setVariable("errorMsgSignup", "You already registered with this email");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		ctx.setVariable("successMsg", "Registration successful! You can now log in with your new credentials.");
		path = "/index.html";
		templateEngine.process(path, ctx, response.getWriter());
		return;
	}

	public void destroy() {
	}
}