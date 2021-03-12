package it.polimi.db2.gamified.controllers;

import java.io.IOException;
//import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
//import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.services.UserQuestionnaireService;

@WebServlet("/SendStatisticalData")
public class SendStatisticalData extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	//private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gamified.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;

	public SendStatisticalData() {
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
			Account u = (Account) session.getAttribute("account");
			String ctxpath = getServletContext().getContextPath();
			if (session.isNew() || u == null) {
				String loginpath = ctxpath + "/index.html";
				response.sendRedirect(loginpath);
				return;
			}			
			String age_answer = request.getParameter("age_answer");
			String sex_answer = request.getParameter("sex_answer");
			String expertise_answer = request.getParameter("expertise_answer");
			int questId = qService.findByDate(new Date(java.lang.System.currentTimeMillis())).get(0).getId();
			uqService.SetStatisticalAttributes(questId, u.getId(), age_answer, sex_answer, expertise_answer);
	}

	public void destroy() {
	}
}