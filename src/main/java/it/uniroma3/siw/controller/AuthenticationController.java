package it.uniroma3.siw.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
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

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "forms/login";
	}

	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		/*
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN)) {
				return "admin/indexAdmin.html";
			}
		}*/
		//crea wishlist: prodotto in più whilist è il più amato
		List<Prodotto> prodotti = prodottoService.find5Prodotti();
		model.addAttribute("prodotti", prodotti);
		return "index.html";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {
/*
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "redirect:/admin/indexAdmin";
		}
		return "redirect:/";*/
		Optional<Credentials> credentialsOpt = credentialsService.getAuthenticatedUserCredentials();

        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();

            // Redirect based on role
            if (credentials.isUser()) {
                logger.info("User logged in, redirecting to the user dashboard");
                return "redirect:/";
            } if (credentials.isAdmin()) {
                logger.info("Admin logged in, redirecting to the admin dashboard");
                return "redirect:/admin/indexAdmin";
            } else {
                logger.info("Erroreeeeeeeeeeeeeeeeeeeeee");
                return "redirect:/error";
            }
        }

        // If not authenticated, redirect to login
        return "redirect:/lingua/cinese";
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
		return "forms/register";
	}

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "freelancerCheckbox", required = false) String freelancerChecked,
                            Model model) {
    	logger.info("User registered AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    	logger.info("User '{}' registered ", user.getId()); //sarà sempre null finché non lo salvi nel database
    	logger.info("User '{}' registered ", user.getNome());
        if (!userBindingResult.hasErrors() && !authService.isNicknameOREmailAlreadyTaken(user.getEmail(),user.getEmail())) {

            // Use the simplified registration method
            authService.registerUser(user, password);
            logger.info("User '{}' registered successfully", user.getEmail());
            return "redirect:/";
        } else {
            return "redirect:/nicknameOrEmailErrorRegister";
        }
    }

}
