package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the login database table.
 * 
 */
@Entity
@Table(name="login")
@NamedQuery(name = "Login.findByUserId", query = "SELECT l FROM Login l WHERE l.account = :usrId")
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false)
	private Timestamp timestamp;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="accountid", nullable=false)
	private Account account;

	public Login() {
	}

	public Login(Timestamp ts, Account account) {
		this.timestamp = ts;
		this.account = account;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}