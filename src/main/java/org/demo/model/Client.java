package org.demo.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		    
    private String apiKey = UUID.randomUUID().toString();
	
	@NotEmpty(message = "Client name must not be empty")
	@Size(max = 100)
	private String name;
	
	@NotEmpty(message = "Email must not be empty")
	@Email(message = "Email must be a valid email address")
	@Column(unique=true)
	private String email;
	
	public Client() {}
	
	public Client(String name, String email, String apiKey) {
		this.name = name;
		this.email = email;
		this.apiKey = apiKey;
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apiKey, email, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(apiKey, other.apiKey) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", apiKey=" + apiKey + ", name=" + name + ", email=" + email + "]";
	}
	
}
