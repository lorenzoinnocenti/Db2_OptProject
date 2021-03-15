package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import it.polimi.db2.gamified.entities.AccountStatus;

@WebServlet("/SavequestionnaireParameters")
public class SavequestionnaireParameters extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

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
			Date todayDate = new Date(System.currentTimeMillis());
			int before =questionnaireDate.compareTo(todayDate);
			if(before >0) {
				request.getSession().setAttribute("dateOk", Integer.valueOf(1));
				System.out.println("Dati sessione");
			}
			else
				request.getSession().setAttribute("dateOk", Integer.valueOf(0));
			request.getSession().setAttribute("numberOfQuestions", request.getParameter("numberOfQuestions"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
	public void destroy() {
	}
}


