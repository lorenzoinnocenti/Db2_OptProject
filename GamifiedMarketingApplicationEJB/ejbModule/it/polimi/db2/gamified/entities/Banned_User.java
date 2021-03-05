package it.polimi.db2.gamified.entities;

import it.polimi.db2.gamified.entities.Account;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Banned_User
 *
 */
@Entity
@DiscriminatorValue ("B")
public class Banned_User extends Account implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Banned_User() {
		super();
	}
   
}