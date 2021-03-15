package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.Product;

@Stateless
public class ProductService {
	
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public ProductService() {
		
	}
	
	public List<Product> findAllProduct(){
		List<Product> products = em.createNamedQuery("Product.findAll", Product.class).getResultList();
		return products;
	}
	
}
