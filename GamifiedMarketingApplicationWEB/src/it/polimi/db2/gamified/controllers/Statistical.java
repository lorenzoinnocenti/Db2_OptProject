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

//Load the Statistical page (static .html)

@WebServlet("/Statistical")
public class Statistical extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	public Statistical() {
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
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process("/WEB-INF/Statistical.html", ctx, response.getWriter()); 
	}
	
	public void destroy() {
	}
	
}
