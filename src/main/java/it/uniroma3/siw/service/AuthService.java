package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;

@Service
public class AuthService {
	
	@Autowired
    private CredentialsService credentialsService;
	     
	@Autowired
    private UserService userService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	public void registerUser(User user, String password) {
		Credentials credentials = new Credentials();
        credentials.setUser(user);
        credentials.setRole(Credentials.USER);
        //credentials.setUsername(user.getUsername());
        credentials.setEmail(user.getEmail());
        credentials.setPassword(passwordEncoder.encode(password));
    
        // Save the user and credentials
        userService.save(user);
        credentialsService.save(credentials);
		
	}
	
	public void registerAdmin(User user, String password) {
		Credentials credentials = new Credentials();
        credentials.setUser(user);
        credentials.setRole(Credentials.ADMIN);
        //credentials.setUsername(user.getUsername());
        credentials.setEmail(user.getEmail());
        credentials.setPassword(passwordEncoder.encode(password));
    
        // Save the user and credentials
        userService.save(user);
        credentialsService.save(credentials);
		
	}

	public boolean isNicknameOREmailAlreadyTaken(String username, String email) {
		//if(this.userService.findByUsername(username).size()>0 || this.userService.findByEmail(email).size()>0)
		if(this.userService.findByEmail(email).size()>0)
			return true;
		else
			return false;
	}
}
