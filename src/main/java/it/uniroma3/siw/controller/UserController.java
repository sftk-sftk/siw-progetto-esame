package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.Tipologia;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private CommentoService commentoService;

	@GetMapping("/indexUser")
	public String getAdminIndex(Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.USER)) {
			// admin is authenticated, show the dashboard
			User user = credentials.getUser();
			model.addAttribute("user", user);

			return "user/indexUser.html";
		}

		return "redirect:/login"; // Redirect to login if not authenticated
	}

	@GetMapping("/userInfo")
	public String getHostDashboard(Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.USER)) {
			User user = credentials.getUser();

			model.addAttribute("user", user);

			return "user/userInfo.html";
		}

		return "redirect:/login"; // Redirect to login if not authenticated
	}

	// Aggiungi un nuovo commento
	@PostMapping("/newCommento/{id}")
	public String addCommento(@PathVariable Long id, @ModelAttribute("commentoUser") Commento commentoUser,
			Principal principal) {
		// Associa l'utente e il prodotto
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
		User user = credentials.getUser();

		logger.info("Id: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());

		commentoUser.setUser(user);
		logger.info("Id1: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());

		commentoUser.setData(LocalDate.now());
		logger.info("Id2: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());
		logger.info("data: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getData());

		Prodotto prodotto = prodottoService.findById(id);

		commentoUser.setProdotto(prodotto);
		logger.info("Id3: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());

		user.add(commentoUser);
		logger.info("Id4: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());
		prodotto.add(commentoUser);
		logger.info("Id5: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getId());
		logger.info("data1: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getData());

		// Salva il nuovo commento
		commentoUser.setId(null);
		logger.info("data2: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getData());
		prodottoService.save(prodotto);

		logger.info("data3: '{}' Commentoooooooooooooooooooooooooooooooooooooooooooooooooo", commentoUser.getData());
		userService.save(user);

		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}

	// Modifica un commento esistente
	@GetMapping("/editCommento/{id}")
	public String editShowCommento(@PathVariable Long id, Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
		User user = credentials.getUser();

		Prodotto prodotto = prodottoService.findById(id);
		Commento commento = commentoService.findByProdottoAndUser(prodotto, user);
		
		List<Commento> commenti = new ArrayList<>();

		// Verifica che il commento esista
		if (commento != null) {
			
			commenti = prodotto.getCommenti();
			commenti.remove(commento);

			model.addAttribute("prodotto", prodotto);
			model.addAttribute("user", user);
			model.addAttribute("commentoUser", commento);
			model.addAttribute("commenti", commenti);
			
			return "user/editProdottoUser";
		}
		
		return "redirect:/prodotto/" + id;
	}

	@PostMapping("/editCommento/{id}")
	public String editCommento(@PathVariable Long id, @ModelAttribute("commentoUser") Commento commentoUser) {

		// Verifica che il commento esista
		if (commentoService.existsById(commentoUser.getId())) {
			// inserisci parametri nascosti come id, user e prodotto

			commentoUser.setData(LocalDate.now());

			commentoService.save(commentoUser); // Modifica il commento
		}
		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}

	// Cancella un commento
	@GetMapping("/deleteCommento/{id}")
	public String deleteCommento(@PathVariable Long id) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
		User user = credentials.getUser();

		Prodotto prodotto = prodottoService.findById(id);

		Commento commento = commentoService.findByProdottoAndUser(prodotto, user);

		user.remove(commento);
		userService.save(user);

		prodotto.remove(commento);
		prodottoService.save(prodotto);

		// Elimina il commento
		commentoService.delete(commento);

		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}
	
	@GetMapping("/viewAllCommenti")
	public String viewAllCommenti(Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
		User user = credentials.getUser();

		List<Commento> commenti = commentoService.findByUserOrderByDataDesc(user);
		
		model.addAttribute("commenti", commenti);

		return "admin/viewAllCommenti";
	}
	
	@GetMapping("/editUser")
	public String showEditUserForm(Model model, Authentication authentication) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.USER)) {
			// admin is authenticated, show the dashboard
			User user = credentials.getUser();
			
			model.addAttribute("newUser", user);
			return "user/edit/editUser";
		}

		return "redirect:/login";
	}
	
	@PostMapping("/editUser")
	public String updateUser(@ModelAttribute("newUser") @Valid User newUser, BindingResult result, Model model,
			Authentication authentication) {
		if (result.hasErrors()) {
			// Se ci sono errori di validazione, torna al form mostrando gli errori
			return "user/edit/editUser";
		}

		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.USER)) {
			// admin is authenticated, show the dashboard
			User user = credentials.getUser();

			user.setNome(newUser.getNome());
			user.setCognome(newUser.getCognome());
			user.setEmail(newUser.getEmail());

			userService.save(user); // salva Host aggiornato

			return "redirect:/user/userInfo";
		}
		return "redirect:/error";
	}
	
	@GetMapping("/changePassword")
	public String showChangePasswordForm(Model model) {
		return "user/edit/changePassword";
	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			Authentication authentication) {

		// Ottieni il nome utente dall'autenticazione
		String username = authentication.getName();

		// Verifica se la password è stata cambiata correttamente
		if (credentialsService.changePassword(username, oldPassword, newPassword)) {
			// return ResponseEntity.ok("Password modificata con successo");
			return "user/edit/successChangePassword";
		} else {
			// return ResponseEntity.badRequest().body("La vecchia password non è
			// corretta");
			return "user/edit/failChangePassword";
		}
	}
}
