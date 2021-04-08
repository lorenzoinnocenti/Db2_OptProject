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

import it.polimi.db2.gamified.entities.Review;
import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.Product;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.entities.Questionnaire;
import it.polimi.db2.gamified.services.ReviewService;
import it.polimi.db2.gamified.entities.QuestionnaireStatus;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;
import it.polimi.db2.gamified.exceptions.UserQuestionnaireNotFoundException;

//Load the user home page

@WebServlet("/Home")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gamified.services/ReviewService")
	private ReviewService rService;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;

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
		
		List<Questionnaire> questionnaires = null;
		Product product = null;
		List<Review> reviews = null;
		QuestionnaireStatus status = null;
		try {
			// questionnaires = qService.findByDate(new Date(java.lang.System.currentTimeMillis()));
			questionnaires = qService.findToday();
			if (questionnaires.size() != 0) {
				product = questionnaires.get(0).getProduct();
				reviews = rService.findReviewByProduct(product.getId());
				if (reviews.size() == 0) {
					reviews = null;
				}
				
				//Get the status of userquestionnaire
				if (uqService.getUserQuestionnaire(((Account) session.getAttribute("account")).getId(), questionnaires.get(0).getId()) != null) {
					status = uqService.getUserQuestionnaire(((Account) session.getAttribute("account")).getId(), questionnaires.get(0).getId()).getStatus();
				}
			}
		} catch (UserQuestionnaireNotFoundException e) {
			//User never pressed "Answer questionnaire", so there isn't any entity in the DB.
			status = null;
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
		ctx.setVariable("status", status);

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}
