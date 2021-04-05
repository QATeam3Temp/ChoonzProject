package com.qa.choonz.persistence.domain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.utils.UserSecurity;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Size(max = 100)
	@Column(unique = true, name = "username")
	private String username;

	@NotNull
	@Size(max = 100)
	@Column(unique = true, name = "password")
	private String password;

	@NotNull
	@Column(unique = true, name = "salt")
	private byte[] salt;
	
	
	
	public User() {
		super();
	}

	public User(long id, @NotNull @Size(max = 100) String username, @NotNull @Size(max = 100) String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		super();
		this.id = id;
		this.username = username;
		this.salt = UserSecurity.getSalt();
		this.password = UserSecurity.encrypt(password, salt);

	}

	public User(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		super();
		this.username = username;
		this.salt = UserSecurity.getSalt();
		this.password = UserSecurity.encrypt(password, salt);
	}

	public User(int id, String username, String password, byte[] salt) {
		super();
		this.id = id;
		this.username = username;
		this.salt = salt;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public long getId() {
		return id;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", salt="
				+ Arrays.toString(salt) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + Arrays.hashCode(salt);
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (!Arrays.equals(salt, other.salt))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}



}
