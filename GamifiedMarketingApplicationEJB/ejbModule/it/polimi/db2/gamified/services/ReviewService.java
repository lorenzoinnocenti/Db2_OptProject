package it.polimi.db2.gamified.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.gamified.entities.*;

public class ReviewService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public ReviewService() {
	}
	
	public List<Review> findReviewByProduct(int productId) {
		return em.createNamedQuery("Review.findByProductId", Review.class).setParameter("prdId", productId).getResultList();	
	}
}
