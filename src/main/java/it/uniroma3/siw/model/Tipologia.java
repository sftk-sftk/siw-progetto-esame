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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Tipologia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "name field must not be blank")
	@Column(nullable = false)
	private String nome;

	@OneToMany(mappedBy = "tipologia", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Prodotto> prodotti;

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

	public List<Prodotto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}

	// HASHCODE E EQUALS
	@Override
	public int hashCode() {
		return Objects.hash(id, nome, prodotti);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tipologia other = (Tipologia) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(prodotti, other.prodotti);
	}

	public void add(Prodotto prodotto) {
		if(prodotti==null) {
			prodotti = new ArrayList<>();
		}
		prodotti.add(prodotto);
		
	}
}
