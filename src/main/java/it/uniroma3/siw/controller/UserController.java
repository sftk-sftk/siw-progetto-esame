package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.Tipologia;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

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
	public String addCommento(@PathVariable Long id, @ModelAttribute("commento") Commento commento, Principal principal) {
		// Associa l'utente e il prodotto
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
		User user = credentials.getUser();

		commento.setUser(user);

		commento.setData(LocalDate.now());

		// Assicurati che il prodotto sia valido (potrebbe venire da un ID nella
		// request)
		Prodotto prodotto = prodottoService.findById(id);
		
		commento.setProdotto(prodotto);
		
		user.add(commento);
		prodotto.add(commento);

		// Salva il nuovo commento
		userService.save(user);
		prodottoService.save(prodotto);
		
		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}

	// Modifica un commento esistente
	@PostMapping("/editCommento/{id}")
	public String editCommento(@PathVariable Long id, @ModelAttribute("commento") Commento commento) {
		
		// Verifica che il commento esista
		if (commentoService.existsById(commento.getId())) {
			//inserisci parametri nascosti come id, user e prodotto
			
			commento.setData(LocalDate.now());
			
			commentoService.save(commento); // Modifica il commento
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
}
