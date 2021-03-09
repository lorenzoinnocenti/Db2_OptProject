package it.polimi.db2.gamified.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
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

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.services.*;

@WebServlet("/DeletePage")
public class GoToDeletePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	
    public GoToDeletePage() {
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
		List<Questionnaire> questionnaires = null;
		try {
			questionnaires = qService.findPastQuestionnaires(new Date(java.lang.System.currentTimeMillis()));
		} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
				return;
		}
		String path = "/WEB-INF/DeletePage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaires", questionnaires);
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
