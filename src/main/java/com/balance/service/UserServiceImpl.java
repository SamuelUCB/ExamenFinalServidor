package com.balance.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import com.balance.model.Terminal;
import com.balance.model.Token;
import com.balance.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.balance.model.Role;
import com.balance.model.User;
import com.balance.repository.RoleRepository;
import com.balance.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private TerminalRepository terminalRepository;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByTokenId(Integer token_id) {
		return userRepository.findByTokenId(token_id);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		Role userRole = roleRepository.findByRole("LIMITED");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		terminalRepository.findOne(user.getTerminal().getSerial()).setActive(true);
		userRepository.save(user);
	}

	@Override
	public User getUserById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.delete(id);
	}
	@Override
	public Iterable<User> listAllUsers(){
		return userRepository.findAll();
	}

	@Override
	public void saveUserEdited(User user){

		userRepository.save(user);
	}
}