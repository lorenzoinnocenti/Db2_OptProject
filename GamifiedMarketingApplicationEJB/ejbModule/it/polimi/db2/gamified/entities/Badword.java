package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the badword database table.
 * 
 */
@Entity
@Table(name="badword")
@NamedQuery(name="Badword.findAll", query="SELECT b FROM Badword b")
public class Badword implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BADWORD_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BADWORD_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=20)
	private String word;

	public Badword() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}