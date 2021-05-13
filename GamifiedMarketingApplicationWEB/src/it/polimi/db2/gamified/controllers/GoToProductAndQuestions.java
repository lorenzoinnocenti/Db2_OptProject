package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.gamified.services.ProductService;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.Product;
import it.polimi.db2.gamified.entities.AccountStatus;

//Load the page where the admin can choose the product and insert the questions for the new questionnaire

@WebServlet("/GoToProductAndQuestions")
public class GoToProductAndQuestions extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gamified.services/ProductSrvice")
	private ProductService pService;

	public GoToProductAndQuestions() {
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
		if (account.getStatus()==AccountStatus.USER) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		Integer numberOfQuestions = (Integer) session.getAttribute("numberOfQuestions");
		if(numberOfQuestions == null) {
			String path = getServletContext().getContextPath() + "/CreateQuestionnaire";
			response.sendRedirect(path);
			return;
		}
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		List<Integer> numberList = new ArrayList<Integer>();
		for (int i=1; i<numberOfQuestions+1; i=i+1)
			numberList.add(i);
		ctx.setVariable("numbers", numberList);
		List<Product> products = pService.findAllProduct();
		ctx.setVariable("products", products);
		templateEngine.process("/WEB-INF/InsertProductAndQuestions.html", ctx, response.getWriter());		
	}
		
	public void destroy() {
	}
}


