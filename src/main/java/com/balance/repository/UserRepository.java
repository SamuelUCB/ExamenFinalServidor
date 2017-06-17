package com.balance.repository;

import com.balance.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.balance.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
	User findByTokenId(Integer token_id);
}