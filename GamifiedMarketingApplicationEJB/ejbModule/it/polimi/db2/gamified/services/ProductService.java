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
	
	public Product createProduct(String ProductName,BigDecimal ProductPrice, String ProductDescription, byte[] imgByteArray) throws ProductAlreadyExistingException {
		List<Product> products = em.createNamedQuery("Product.findByName", Product.class).setParameter("prodName", ProductName).getResultList();
		if(!products.isEmpty())
			throw  new ProductAlreadyExistingException("The products with that name already exists");
		Product newProduct = new Product(ProductName, ProductPrice, ProductDescription, imgByteArray);
		em.persist(newProduct);
		return newProduct;
	}
	
}
