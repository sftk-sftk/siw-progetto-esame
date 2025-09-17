package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
public class Prodotto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "name field must not be blank")
	@Column(nullable = false)
	private String nome;

	@NotBlank(message = "price field must not be blank")
	@Column(nullable = false)
	private BigDecimal prezzo;

	@NotBlank(message = "description field must not be blank")
	@Column(nullable = false)
	private String descrizione;

	@ManyToMany
	private List<Prodotto> prodottiSimili = new ArrayList<>();

	@OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Commento> commenti = new ArrayList<>();

	@NotBlank(message = "tipology field must not be blank")
	@Column(nullable = false)
	@ManyToOne
	private Tipologia tipologia;

	// GETTER E SETTER
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Prodotto> getProdottiSimili() {
		return prodottiSimili;
	}

	public void setProdottiSimili(List<Prodotto> prodottiSimili) {
		this.prodottiSimili = prodottiSimili;
	}

	public List<Commento> getCommenti() {
		return commenti;
	}

	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	// HASHCODE E EQUALS
	@Override
	public int hashCode() {
		return Objects.hash(commenti, descrizione, id, nome, prezzo, prodottiSimili, tipologia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prodotto other = (Prodotto) obj;
		return Objects.equals(commenti, other.commenti) && Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(prezzo, other.prezzo) && Objects.equals(prodottiSimili, other.prodottiSimili)
				&& Objects.equals(tipologia, other.tipologia);
	}
}
