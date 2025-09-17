package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void save(User user) {
		this.userRepository.save(user);
	}

	public List<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean alreadyExists(User user) {
		List<User> users = this.userRepository.findByEmail(user.getEmail());
		if (users.size() > 0)
			return true;
		else
			return false;
	}

}
