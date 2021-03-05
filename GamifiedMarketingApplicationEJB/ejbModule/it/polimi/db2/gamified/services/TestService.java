package it.polimi.db2.gamified.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import it.polimi.db2.gamified.entities.*;

@Stateless
public class TestService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public TestService() {
	}
	
	public List<Product> testMethod() {
		return em.createNamedQuery("Product.findAll", Product.class).getResultList();
	}
}
