package it.uniroma3.siw.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "description field must not be blank")
	@Column(nullable = false)
	private String descrizione;
	
	@NotBlank(message = "date field must not be blank")
    @Column(nullable = false)
    private LocalDate data;

	// Relazione con User
	@JoinColumn(name = "user_id", nullable = false) // Foreign key verso User
	@ManyToOne
	private User user;

	// Relazione con Prodotto
	@JoinColumn(name = "prodotto_id", nullable = false) // Foreign key verso Prodotto
	@ManyToOne
	private Prodotto prodotto;

	// GETTER E SETTER
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	// HASHCODE E EQUALS
	@Override
	public int hashCode() {
		return Objects.hash(descrizione, id, prodotto, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commento other = (Commento) obj;
		return Objects.equals(descrizione, other.descrizione) && Objects.equals(id, other.id)
				&& Objects.equals(prodotto, other.prodotto) && Objects.equals(user, other.user);
	}

}
