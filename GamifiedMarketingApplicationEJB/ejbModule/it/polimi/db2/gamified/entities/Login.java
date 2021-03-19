package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the login database table.
 * 
 */
@Entity
@Table(name="login")
@NamedQuery(name = "Login.findByUserId", query = "SELECT l FROM Login l WHERE l.account = :usrId")

@NamedQueries({	@NamedQuery(name = "Login.findByUserId", query = "SELECT l FROM Login l WHERE l.account = :usrId"),
	            @NamedQuery(name = "Login.findByUserDate", query = "SELECT l FROM Login l WHERE (l.account.id = :usrId AND l.timestamp >= :dateStart AND l.timestamp < :dateEnd)")})

public class Login implements Serializable {
	private static final long serialVersionUID = 1L; 

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date timestamp;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="accountid", nullable=false)
	private Account account;

	public Login() {
	}

	public Login(Date ts, Account account) {
		this.timestamp = ts;
		this.account = account;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}