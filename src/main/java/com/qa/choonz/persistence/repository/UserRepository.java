package com.qa.choonz.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


	@Query("SELECT us FROM User us WHERE us.username = ?1")
	public User findbyName(String username);

}
