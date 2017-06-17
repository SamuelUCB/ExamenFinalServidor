package com.balance.service;

import com.balance.model.Token;
import com.balance.model.User;

public interface UserService {
	User findUserByEmail(String email);
	User findByTokenId(Integer token_id);
	void saveUser(User user);
	Iterable<User> listAllUsers();

	User getUserById(Integer id);
	void deleteUser(Integer id);
	void saveUserEdited(User user);
}