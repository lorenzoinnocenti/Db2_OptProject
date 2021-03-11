package it.polimi.db2.gamified.services;

import java.util.List;

import javax.ejb.EJB;
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
	@EJB(name = "it.polimi.db2.gamified.services/LoginService")
	private LoginService ls;
	
	public AccountService() {
	}

	public Account checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException, BannedUserException {
		List<Account> aList = null;
		try {
			
			aList = em.createNamedQuery("Account.checkCredentials", Account.class).setParameter("usrn", usrn).setParameter("pwd", pwd)
					.getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new CredentialsException("Could not verify credentals");
		}
		if (aList.isEmpty())
			return null;
		else if (aList.size() == 1) {
			//Se l'account � bannato
			if (aList.get(0).getStatus()==AccountStatus.BANNED) {
				throw new BannedUserException("This account has been terminated.");
			}
			//Altrimenti, ritorna user
			else {
				Account account = aList.get(0);
				ls.addTS(account.getId());
				return account;
			}
		}
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
	
	public AccountStatus checkStatus(int accountId) throws AccountNotFoundException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("Account not found");
		return account.getStatus();
	}
	
	public void banUser(int accountId) throws AccountNotFoundException, AlreadyBannedException, BanAdminException {
		Account account = em.find(Account.class, accountId);
		if (account == null) throw new AccountNotFoundException("Account not found");
		if (account.getStatus() == AccountStatus.BANNED) throw new AlreadyBannedException("This user is already banned");
		if (account.getStatus() == AccountStatus.ADMIN) throw new BanAdminException("Can't ban an admin");
		account.setStatus(AccountStatus.BANNED); 
	}
	
	public List<Account> computeLeaderboard(){
		return em.createNamedQuery("Account.computeLeaderboard", Account.class)
				.setMaxResults(10)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.getResultList();
	}
}








