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

import it.polimi.db2.gamified.entities.*;

//Display the Ban message after a user is banned

@WebServlet("/Banned")
public class Banned extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

    public Banned() {
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
		if (session.isNew() || session.getAttribute("account") == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/index.html");
			return;
		}
		Account account = (Account) session.getAttribute("account");
		if (account.getStatus()==AccountStatus.ADMIN) {
			response.sendRedirect(getServletContext().getContextPath() + "/AdminPage");
			return;
		}
		String path = "/index.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("errorMsg", "You cannot use bad words! Your account has been terminated.");
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
