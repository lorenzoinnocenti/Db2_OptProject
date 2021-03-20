package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.polimi.db2.gamified.services.QuestionnaireService;
import it.polimi.db2.gamified.entities.Questionnaire;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;

//Set the parameters used by GoToCreateQuestionnaire servlet to render the CreateQuestionnaire.html

@WebServlet("/SavequestionnaireParameters")
public class SavequestionnaireParameters extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/QuestionnaireService")
	private QuestionnaireService qService;

	public SavequestionnaireParameters() {
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
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
		
		String stringDate = request.getParameter("questionnaireDate");
		Date questionnaireDate;
		try {
			questionnaireDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
			Date todayDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(todayDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			todayDate = cal.getTime();
			Boolean isTheDateNotBeforeToday = (questionnaireDate.after(todayDate) || questionnaireDate.equals(todayDate));			
			List<Questionnaire> questionnaires = null;
			questionnaires = qService.findByDate(questionnaireDate);
			Boolean alreadyExistingQuestionnaires = (!questionnaires.isEmpty());
			if(alreadyExistingQuestionnaires)
				session.setAttribute("alreadyExisting", Integer.valueOf(1));
			else
				session.setAttribute("alreadyExisting", Integer.valueOf(0));
			if (isTheDateNotBeforeToday)
				session.setAttribute("dateOk", Integer.valueOf(1));
			else
				session.setAttribute("dateOk", Integer.valueOf(0));
			if (session.getAttribute("numberOfQuestions") == null) {
				Integer numberOfQuestions = Integer.parseInt(request.getParameter("numberOfQuestions"));
				session.setAttribute("numberOfQuestions", numberOfQuestions);
			}
			session.setAttribute("questionnaireDate", questionnaireDate);
			String path = null;
			String ctxpath = getServletContext().getContextPath();				
			path = ctxpath + "/CreateQuestionnaire";
			response.sendRedirect(path);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
	public void destroy() {
	}
	
}



