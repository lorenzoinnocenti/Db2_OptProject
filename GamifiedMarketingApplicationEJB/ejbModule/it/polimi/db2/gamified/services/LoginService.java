package it.polimi.db2.gamified.services;

import java.sql.Timestamp;
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
		//Debug purposes
		System.out.println("costruttore: em is " + em);
	}
	
	public List<Login> findTSByUser (int accountId) throws AccountNotFoundException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("Account not found");
		return account.getLogins();	
	}
	
	public void addTS (int accountId) {
		//Debug purposes
		System.out.println("BBBBBBBBBBBB " + accountId);
		System.out.println(em);
		Account account = em.find(Account.class, accountId);		
		Login login = new Login();
		login.setTimestamp( new Timestamp(System.currentTimeMillis()));
		login.setAccount(account);
		
		//Binding bi-directional 
		account.addLogin(login);
		em.persist(login);
		System.out.println("BBBBBBBBBBBB " + login.getId());
		}
}
