package it.polimi.db2.gamified.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name="product")
@NamedQueries({@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p"),
			   @NamedQuery(name="Product.findByName", query="SELECT p FROM Product p WHERE p.name = :prodName")})
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=200)
	private String description;

	@Column(nullable=false, length=40)
	private String name;

	@Column(nullable=false, precision=10, scale=2)
	private BigDecimal price;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] image;

	//bi-directional many-to-one association to Questionnaire
	@OneToMany(mappedBy="product", cascade = CascadeType.REMOVE)
	private List<Questionnaire> questionnaires;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="product", cascade = CascadeType.REMOVE)
	private List<Review> reviews;

	public Product() {
	}
	
	public Product(String name, BigDecimal price, String description, byte[] image) {
		this.name=name;
		this.price=price;
		this.description=description;
		this.image=image;
		this.questionnaires = null;
		this.reviews = null;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public byte[] getImage() {
		return this.image;
	}

	public String getImageData() {
		return Base64.getMimeEncoder().encodeToString(image);
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}

	public void setQuestionnaires(List<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}

	public Questionnaire addQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().add(questionnaire);
		questionnaire.setProduct(this);

		return questionnaire;
	}

	public Questionnaire removeQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().remove(questionnaire);
		questionnaire.setProduct(null);

		return questionnaire;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setProduct(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setProduct(null);

		return review;
	}

}