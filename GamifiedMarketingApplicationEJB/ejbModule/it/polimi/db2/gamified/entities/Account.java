package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name="account")
@NamedQueries({ @NamedQuery(name = "Account.checkCredentials", query = "SELECT r FROM Account r  WHERE r.username = :usrn and r.password = :pwd"),
				@NamedQuery(name = "Account.findByUsername", query = "SELECT r FROM Account r  WHERE r.username = :usrn"),
				@NamedQuery(name = "Account.findByEmail", query = "SELECT r FROM Account r  WHERE r.email = :email"),
	            @NamedQuery(name = "Account.computeLeaderboard", query = "SELECT r FROM Account r ORDER BY r.totalpoints DESC")})


public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=255)
	private String email;

	@Column(nullable=false, length=20)
	private String password;

	@Column(nullable=false)
	private AccountStatus status;

	@Column (nullable=true)
	private int totalpoints;

	@Column(nullable=false, length=20)
	private String username;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="account")
	private List<Answer> answers;

	//bi-directional many-to-one association to Login
	@OneToMany(mappedBy="account", cascade = CascadeType.REMOVE)
	private List<Login> logins;

	//bi-directional many-to-one association to Userquestionnaire
	@OneToMany(mappedBy="account")
	private List<Userquestionnaire> userquestionnaires;

	public Account() {
	}
	
	public Account(String usrn, String psw, String email) {
		this.username=usrn;
		this.password=psw;
		this.email=email;
		this.totalpoints=0;
		this.status = AccountStatus.USER;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountStatus getStatus() {
		return this.status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public int getTotalpoints() {
		return this.totalpoints;
	}

	public void setTotalpoints(int totalpoints) {
		this.totalpoints = totalpoints;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setAccount(this);
		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setAccount(null);
		return answer;
	}

	public List<Login> getLogins() {
		return this.logins;
	}

	public void setLogins(List<Login> logins) {
		this.logins = logins;
	}

	public Login addLogin(Login login) {
		getLogins().add(login);
		login.setAccount(this);
		return login;
	}

	public Login removeLogin(Login login) {
		getLogins().remove(login);
		login.setAccount(null);
		return login;
	}

	public List<Userquestionnaire> getUserquestionnaires() {
		return this.userquestionnaires;
	}

	public void setUserquestionnaires(List<Userquestionnaire> userquestionnaires) {
		this.userquestionnaires = userquestionnaires;
	}

	public Userquestionnaire addUserquestionnaire(Userquestionnaire userquestionnaire) {
		getUserquestionnaires().add(userquestionnaire);
		userquestionnaire.setAccount(this);
		return userquestionnaire;
	}

	public Userquestionnaire removeUserquestionnaire(Userquestionnaire userquestionnaire) {
		getUserquestionnaires().remove(userquestionnaire);
		userquestionnaire.setAccount(null);
		return userquestionnaire;
	}

}