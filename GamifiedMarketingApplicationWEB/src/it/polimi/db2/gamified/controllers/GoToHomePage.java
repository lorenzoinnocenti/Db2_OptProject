package it.polimi.db2.gamified.controllers;

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

@WebServlet("/Home")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gamified.services/ReviewService")
	private ReviewService rService;

	public GoToHomePage() {
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("account") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		
		List<Questionnaire> questionnaires = null;
		Product product = null;
		List<Review> reviews = null;

		try {
			questionnaires = qService.findByDate(new Date(java.lang.System.currentTimeMillis()));
			if (questionnaires.size() != 0) {
				product = questionnaires.get(0).getProduct();
				reviews = rService.findReviewByProduct(product.getId());
				
				if (reviews.size() == 0) {
					reviews = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
			return;
		}

		// Redirect to the Home page and add missions to the parameters
		String path = "/WEB-INF/Home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("product", product);
		ctx.setVariable("reviews", reviews);

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}
