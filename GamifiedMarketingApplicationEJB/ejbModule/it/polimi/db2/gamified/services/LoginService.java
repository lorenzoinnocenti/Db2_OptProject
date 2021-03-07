package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.*;

@Stateless
public class LoginService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public List<Login> findTSByUser (int userId) {
		return em.createNamedQuery("Login.findByUserId", Login.class).setParameter("usrId", userId).getResultList();	
	}
}
