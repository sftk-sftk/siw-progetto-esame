package it.uniroma3.siw.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.Tipologia;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.TipologiaService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private TipologiaService tipologiaService;

	// home page
	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// crea wishlist: prodotto in più whilist è il più amato
		List<Prodotto> prodottiPiuDiscussi3 = prodottoService.find3Prodotti();
		List<Prodotto> prodotti10Euro3 = prodottoService.find3Prodotti10Euro();
		
		model.addAttribute("prodottiPiuDiscussi3", prodottiPiuDiscussi3);
		model.addAttribute("prodotti10Euro3", prodotti10Euro3);
		
		return "index.html";
	}

	@GetMapping("/catalogo")
	public String filtraProdotti(@RequestParam(required = false) String nome,
			@RequestParam(required = false) Long tipologiaId, @RequestParam(required = false) BigDecimal prezzoMax,
			Model model) {

		List<Prodotto> prodottiFiltrati = prodottoService.filtraProdotti(nome, tipologiaId, prezzoMax);
		List<Tipologia> tipologie = tipologiaService.findAll();

		model.addAttribute("prodotti", prodottiFiltrati);
		model.addAttribute("tipologie", tipologie);
		return "catalogo"; // nome della pagina Thymeleaf (catalogo.html)
	}
	
	@GetMapping(value = "/catalogoPiuDiscussi")
	public String catalogoPiuDiscussi(Model model) {
		
		List<Prodotto> prodottiPiuDiscussi = prodottoService.findAll();
		
		model.addAttribute("prodottiPiuDiscussi", prodottiPiuDiscussi);
		
		return "catalogoPiuDiscussi";
	}
	
	@GetMapping(value = "/prodotto/{id}")
	public String prodotto(@PathVariable("id") Long id, Model model) {
		
		Prodotto prodotto = prodottoService.findById(id);
		if(prodotto == null) {
			return "catalogo";
		}
		
		model.addAttribute("prodotto", prodotto);
		
		return "prodotto";
	}
	
	@GetMapping(value = "/catalogo10Euro")
	public String catalogo10Euro(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<Prodotto> prodotti10Euro = prodottoService.findProdotti10Euro();
		
		model.addAttribute("prodotti10Euro", prodotti10Euro);
		
		return "catalogo10Euro";
	}

	// login e registrazione
	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "auth/login";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {
		Optional<Credentials> credentialsOpt = credentialsService.getAuthenticatedUserCredentials();
		logger.info("Before if");

		if (credentialsOpt.isPresent()) {
			Credentials credentials = credentialsOpt.get();

			// Redirect based on role
			if (credentials.isUser()) {
				logger.info("User logged in, redirecting to the user dashboard");
				return "redirect:/";
			}
			if (credentials.isAdmin()) {
				logger.info("Admin logged in, redirecting to the admin dashboard");
				return "redirect:/admin/indexAdmin";
			} else {
				logger.info("Erroreeeeeeeeeeeeeeeeeeeeee");
				return "redirect:/error";
			}
		}

		// If not authenticated, redirect to login
		return "redirect:/";
	}

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		/*
		 * model.addAttribute("user", new User()); model.addAttribute("credentials", new
		 * Credentials());
		 */
		User user = new User();
		model.addAttribute("isOidc", false);
		model.addAttribute("user", user);
		return "auth/register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked, Model model) {
		logger.info(
				"User registered AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		logger.info("User '{}' registered ", user.getId()); // sarà sempre null finché non lo salvi nel database
		logger.info("User '{}' registered ", user.getNome());
		if (!userBindingResult.hasErrors()
				&& !authService.isNicknameOREmailAlreadyTaken(user.getEmail(), user.getEmail())) {

			// Use the simplified registration method
			authService.registerUser(user, password);
			logger.info("User '{}' registered successfully", user.getEmail());
			return "redirect:/";
		} else {
			return "redirect:/nicknameOrEmailErrorRegister";
		}
	}

	@GetMapping(value = "/registerAdmin")
	public String showRegisterAdminForm(Model model) {
		/*
		 * model.addAttribute("user", new User()); model.addAttribute("credentials", new
		 * Credentials());
		 */
		User user = new User();
		model.addAttribute("isOidc", false);
		model.addAttribute("user", user);
		return "auth/registerAdmin";
	}

	@PostMapping("/registerAdmin")
	public String registerAdmin(@ModelAttribute("user") User user, BindingResult userBindingResult,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "password", required = false) String password, Model model) {

		logger.info("User '{}' registered ", user.getNome());

		if (!userBindingResult.hasErrors()
				&& !authService.isNicknameOREmailAlreadyTaken(user.getEmail(), user.getEmail())) {

			// Use the simplified registration method
			authService.registerAdmin(user, password);
			
			logger.info("User '{}' registered successfully", user.getEmail());
			
			return "redirect:/";
		} else {
			return "redirect:/nicknameOrEmailErrorRegister";
		}
	}

}
