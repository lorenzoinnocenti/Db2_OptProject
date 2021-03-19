package it.polimi.db2.gamified.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class LoginService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public LoginService() {
	}
	
	public List<Login> findTSByUser (int accountId) throws AccountNotFoundException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("Account not found");
		return account.getLogins();	
	}
	
	public void addTS (int accountId) {
		Account account = em.find(Account.class, accountId);		
		Login login = new Login(new Date(System.currentTimeMillis()), account);
		//Binding bi-directional 
		account.addLogin(login);
		em.persist(login);
		}
	
	public List<Login> findByUserDate (int accountId, Date dateStart){
		Date dateEnd = new Date(dateStart.getTime() + (1000 * 60 * 60 * 24));
		return em.createNamedQuery("Login.findByUserDate", Login.class)
				.setParameter("dateStart", dateStart)
				.setParameter("dateEnd", dateEnd)
				.setParameter("usrId", accountId)
				.getResultList();
	}
}
