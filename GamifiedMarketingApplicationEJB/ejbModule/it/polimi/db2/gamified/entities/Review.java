package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the review database table.
 * 
 */
@Entity
@Table(name="review")
@NamedQuery(name="Review.findByProductId", query="SELECT r FROM Review r where r.product = :prdId")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=40)
	private String authorname;

	@Column(nullable=false, precision=10)
	private BigDecimal rating;

	@Column(length=400)
	private String reviewtext;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="productid", nullable=false)
	private Product product;

	public Review() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthorname() {
		return this.authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public BigDecimal getRating() {
		return this.rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getReviewtext() {
		return this.reviewtext;
	}

	public void setReviewtext(String reviewtext) {
		this.reviewtext = reviewtext;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}