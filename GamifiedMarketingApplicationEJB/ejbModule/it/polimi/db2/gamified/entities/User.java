package it.polimi.db2.gamified.entities;

import it.polimi.db2.gamified.entities.Account;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Users
 *
 */
@Entity
@DiscriminatorValue ("U")
public class User extends Account implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}
   
}