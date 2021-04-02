package com.qa.choonz.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
    @NotNull
    @Size(max = 100)
    @Column(unique = true,name = "username")
    private String username;
    
    @NotNull
    @Size(max = 100)
    @Column(unique = true,name = "password")
    private String saltedPassword;
    
    @NotNull
    @Size(max = 100)
    @Column(unique = true,name = "salt")
    private String salt;

	public User(long id, @NotNull @Size(max = 100) String username, @NotNull @Size(max = 100) String saltedPassword) {
		super();
		this.id = id;
		this.username = username;
		this.salt = Security.getSalt();
		this.saltedPassword = Security.encrypt(saltedPassword,salt);
	}
	
	

	public long getId() {
		return id;
	}



	public String getUsername() {
		return username;
	}



	public String getSaltedPassword() {
		return saltedPassword;
	}



	public void setId(long id) {
		this.id = id;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public void setSaltedPassword(String password) {
		this.saltedPassword = saltedPassword;
	}


	

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", saltedPassword=" + saltedPassword + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((saltedPassword == null) ? 0 : saltedPassword.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (saltedPassword == null) {
			if (other.saltedPassword != null)
				return false;
		} else if (!saltedPassword.equals(other.saltedPassword))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
    
    
    
}
