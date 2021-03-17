package it.polimi.db2.gamified.services;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.Product;
import it.polimi.db2.gamified.exceptions.ProductAlreadyExistingException;

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
	
	public int addProduct(String name, BigDecimal price, String description, byte[] image ) throws ProductAlreadyExistingException {
		List<Product> products = em.createNamedQuery("Product.findByName", Product.class).setParameter("prodName", name).getResultList();
		if(!products.isEmpty())
			throw  new ProductAlreadyExistingException("The products with that name already exists");
		Product product = new Product(name, price,description, image);
		em.persist(product);
		em.flush();
		return product.getId();
	}
	
}
