package it.polimi.db2.gamified.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
//import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
//import java.util.List;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gamified.utils.ImageUtils;
import it.polimi.db2.gamified.entities.Account;
import it.polimi.db2.gamified.entities.AccountStatus;
import it.polimi.db2.gamified.exceptions.ProductAlreadyExistingException;
import it.polimi.db2.gamified.exceptions.ProductNotFoundException;
import it.polimi.db2.gamified.exceptions.QuestionnaireAlreadyPresentException;
import it.polimi.db2.gamified.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.gamified.services.ProductService;
import it.polimi.db2.gamified.services.QuestionnaireService;

@WebServlet("/SendQuestionnaireNewProduct")
@MultipartConfig
public class SendQuestionnaireNewProduct extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	//private TemplateEngine templateEngine;
	
	
	@EJB(name="it.polimi.db2.gamified.services/ProductService")
	private ProductService pService;

	public SendQuestionnaireNewProduct() {
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
		Account account = (Account) session.getAttribute("account");
		if (session.isNew() || account == null) {
			response.sendRedirect(getServletContext().getContextPath() + "/index.html");
			return;
		}
		if (account.getStatus() == AccountStatus.USER) {
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
			return;
		}
		
		String ProductName = request.getParameter("ProductName");
		System.out.println(ProductName);
		String ProductDescription = (String) request.getParameter("ProductDescription");
		BigDecimal ProductPrice = new BigDecimal(request.getParameter("ProductPrice"));
		Part imgFile = request.getPart("picture");
		InputStream imgContent = imgFile.getInputStream();
		byte[] imgByteArray = ImageUtils.readImage(imgContent);
		try {
			pService.createProduct(ProductName, ProductPrice, ProductDescription, imgByteArray);
		} catch (ProductAlreadyExistingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/AdminPage";
		response.sendRedirect(path);
	}

	public void destroy() {
	}
}