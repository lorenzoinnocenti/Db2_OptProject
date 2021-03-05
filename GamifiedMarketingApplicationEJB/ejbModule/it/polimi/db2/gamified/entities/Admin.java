package it.polimi.db2.gamified.entities;

import it.polimi.db2.gamified.entities.Account;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Admin
 *
 */
@Entity
@DiscriminatorValue ("A")
public class Admin extends Account implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Admin() {
		super();
	}
}