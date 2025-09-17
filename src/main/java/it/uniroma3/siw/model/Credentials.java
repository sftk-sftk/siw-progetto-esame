package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Credentials {

	public static final String ADMIN = "ADMIN";
	public static final String USER = "USER";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "email must not be blank")
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank(message = "password must not be blank")
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private User user;
	
	
	public boolean isUser() {
		return this.role.equals("USER");
	}
	
	public boolean isAdmin() {
		return this.role.equals("ADMIN");
	}
	

	// GETTER E SETTER
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// HASHCODE E EQUALS
	@Override
	public int hashCode() {
		return Objects.hash(email, id, password, role, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(password, other.password) && Objects.equals(role, other.role)
				&& Objects.equals(user, other.user);
	}
}
