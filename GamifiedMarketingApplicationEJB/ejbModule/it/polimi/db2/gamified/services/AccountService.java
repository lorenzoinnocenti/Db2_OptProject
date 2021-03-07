package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.gamified.entities.*;
import it.polimi.db2.gamified.exceptions.*;

@Stateless
public class AccountService {
	@PersistenceContext(unitName = "GamifiedMarketingApplicationEJB")
	private EntityManager em;
	
	public AccountService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException, BannedUserException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1) {
			//Se l'account � bannato
			if (uList.get(0).getStatus()==AccountStatus.BANNED) {
				throw new BannedUserException("This account has been terminated.");
			}
			//Altrimenti, ritorna user
			else return uList.get(0);
			}
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
	
	public AccountStatus checkStatus(int accountId) throws AccountNotFoundException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("account not found");
		return account.getStatus();
	}
	
	public void banUser(int accountId) throws AccountNotFoundException, AlreadyBannedException, BanAdminException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("account not found");
		if (account.getStatus()==AccountStatus.BANNED) throw new AlreadyBannedException("this user is already banned");
		if (account.getStatus()==AccountStatus.ADMIN) throw new BanAdminException("can't ban an admin");
		account.setStatus(AccountStatus.BANNED); 
	}
	
	public List<Account> computeLeaderboard(){
		return em.createNamedQuery("Account.computeLeaderboard", Account.class)
				.setMaxResults(10)
				.setHint("javax.persistence.cache.storeMode", "REFRESH") // not sure about that
				.getResultList();
	}
}








