package it.uniroma3.siw.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.Tipologia;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.TipologiaService;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private TipologiaService tipologiaService;

	@Autowired
	private CommentoService commentoService;

	@GetMapping("/indexAdmin")
	public String getAdminIndex(Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.ADMIN)) {
			// admin is authenticated, show the dashboard
			User admin = credentials.getUser();
			List<Tipologia> tipologie = tipologiaService.findAll();

			model.addAttribute("admin", admin); // Add user to the model for display
			model.addAttribute("tipologie", tipologie);

			return "admin/indexAdmin.html";
		}

		return "redirect:/login"; // Redirect to login if not authenticated
	}

	@GetMapping("/adminInfo")
	public String getHostDashboard(Model model) {
		Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);

		if (credentials != null && credentials.getRole().equals(Credentials.ADMIN)) {
			// admin is authenticated, show the dashboard
			User admin = credentials.getUser();
			model.addAttribute("admin", admin); // Add user to the model for display

			return "admin/adminInfo.html";
		}

		return "redirect:/login"; // Redirect to login if not authenticated
	}

	@GetMapping("/newProdotto")
	public String newFacility(Model model) {
		List<Tipologia> tipologie = tipologiaService.findAll();
		List<Prodotto> prodotti = prodottoService.findAll();

		model.addAttribute("prodotto", new Prodotto());
		model.addAttribute("tipologie", tipologie);
		model.addAttribute("prodotti", prodotti);

		return "admin/new/newProdotto";
	}

	@PostMapping("/newProdotto")
	@Transactional
	public String saveProdotto(@ModelAttribute("prodotto") @Valid Prodotto prodotto, BindingResult result,
			@RequestParam(value = "tipologieSelect", required = false) String tipologiaSelezionata,
			@RequestParam(value = "nomeTipologia", required = false) String nomeTipologia,
			@RequestParam(value = "prodottiSimili", required = false) List<Long> prodottiSimiliIds,
			@RequestParam("fileImmagine") MultipartFile fileImmagine, Model model) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("tipologie", tipologiaService.findAll());
			model.addAttribute("prodotti", prodottoService.findAll());
			return "admin/new/newProdotto";
		}

		if (!fileImmagine.isEmpty()) {
			String nomeFile = UUID.randomUUID() + "_" + fileImmagine.getOriginalFilename();
			Path percorso = Paths.get("src/main/resources/static/images/prodotti", nomeFile);
			Files.createDirectories(percorso.getParent()); // crea la cartella se non esiste
			Files.write(percorso, fileImmagine.getBytes());

			prodotto.setImmagine(nomeFile); // Salva solo il nome del file
		}

		Tipologia tipologia = null;
		logger.info("Nome: '{}' Tipologia", tipologia);

		if (tipologiaSelezionata.equals("__new__")) {

			if (nomeTipologia == null || nomeTipologia.trim().isEmpty()) {

				logger.info("Nome 1: '{}' Tipologia", tipologia);

				result.rejectValue("tipologia", "NotBlank", "Inserisci il nome della nuova tipologia");
				model.addAttribute("tipologie", tipologiaService.findAll());
				model.addAttribute("prodotti", prodottoService.findAll());
				return "admin/new/newProdotto";
			}
			tipologia = tipologiaService.findByNome(nomeTipologia.trim());

			if (tipologia == null) {
				tipologia = new Tipologia();
				tipologia.setNome(nomeTipologia.trim());
			}

			logger.info("Nome 2: '{}' Tipologia", tipologia);

		} else {

			logger.info("Nome 3: '{}' Tipologia", tipologia);

			tipologia = tipologiaService.findByNome(tipologiaSelezionata);
		}

		logger.info("Nome 4: '{}' Tipologia", tipologia);

		prodotto.setTipologia(tipologia);
		logger.info("Nome 5: '{}' Tipologia", tipologia);
		tipologia.add(prodotto);
		logger.info("Nome 6: '{}' Tipologia", tipologia);

		// Gestione prodotti affini
		if (prodottiSimiliIds != null && !prodottiSimiliIds.isEmpty()) {
			List<Prodotto> prodottiSimili = prodottoService.findAllById(prodottiSimiliIds);
			prodotto.setProdottiSimili(prodottiSimili);
		}
		logger.info("Nome 7: '{}' Tipologia", tipologia);
		tipologiaService.save(tipologia);
		logger.info("Nome 8: '{}' Tipologia", tipologia);

		prodottoService.save(prodotto);

		return "redirect:/admin/indexAdmin";
	}
	/*
	 * @PostMapping("/editProdottiAffini/{id}")
	 * 
	 * @Transactional public String listProdottiSimili(@PathVariable("id") Long
	 * id, @RequestParam(value = "prodottiSimili", required = false) List<Long>
	 * prodottiSimiliIds, BindingResult result, Model model) throws IOException { if
	 * (result.hasErrors()) { model.addAttribute("tipologie",
	 * tipologiaService.findAll()); model.addAttribute("prodotti",
	 * prodottoService.findAll()); return "admin/new/newProdotto"; }
	 * 
	 * // Gestione prodotti affini if (prodottiSimiliIds != null &&
	 * !prodottiSimiliIds.isEmpty()) { List<Prodotto> prodottiSimili =
	 * prodottoService.findAllById(prodottiSimiliIds);
	 * prodotto.setProdottiSimili(prodottiSimili); }
	 * logger.info("Nome 1: '{}' Tipologia", tipologia);
	 * 
	 * prodottoService.save(prodotto);
	 * 
	 * return "redirect:/admin/indexAdmin"; }
	 * 
	 * @PostMapping("/editProdottiAffini/{id}")
	 * 
	 * @Transactional public String saveProdottiSimili(@PathVariable("id") Long
	 * id, @RequestParam(value = "prodottiSimili", required = false) List<Long>
	 * prodottiSimiliIds, BindingResult result, Model model) throws IOException { if
	 * (result.hasErrors()) { model.addAttribute("tipologie",
	 * tipologiaService.findAll()); model.addAttribute("prodotti",
	 * prodottoService.findAll()); return "admin/new/newProdotto"; }
	 * 
	 * // Gestione prodotti affini if (prodottiSimiliIds != null &&
	 * !prodottiSimiliIds.isEmpty()) { List<Prodotto> prodottiSimili =
	 * prodottoService.findAllById(prodottiSimiliIds);
	 * prodotto.setProdottiSimili(prodottiSimili); }
	 * logger.info("Nome 1: '{}' Tipologia", tipologia);
	 * 
	 * prodottoService.save(prodotto);
	 * 
	 * return "redirect:/admin/indexAdmin"; }
	 */

	@GetMapping("/deleteCommento/{id}")
	public String deleteCommento(@PathVariable Long id, HttpServletRequest request) {
		/*
		 * Credentials credentials =
		 * credentialsService.getAuthenticatedUserCredentials().orElse(null); User user
		 * = credentials.getUser();
		 * 
		 * Prodotto prodotto = prodottoService.findById(id);
		 * 
		 * Commento commento = commentoService.findByProdottoAndUser(prodotto, user);
		 */
		Commento commento = commentoService.findById(id);

		Prodotto prodotto = commento.getProdotto();
		User user = commento.getUser();

		user.remove(commento);
		userService.save(user);

		prodotto.remove(commento);
		prodottoService.save(prodotto);

		// Elimina il commento
		commentoService.delete(commento);

		// Recupera l'URL della pagina precedente
		String referer = request.getHeader("Referer");

		// Se presente, reindirizza alla pagina precedente
		if (referer != null) {
			return "redirect:" + referer;
		}

		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}

	@GetMapping("/viewAllCommenti")
	public String viewAllCommenti(@RequestParam(required = false) String nomeProdotto,
			@RequestParam(required = false) String parola, @RequestParam(required = false) LocalDate data,
			Model model) {

		List<Commento> commenti = commentoService.filtraCommenti(nomeProdotto, parola, data);

		model.addAttribute("commenti", commenti);

		model.addAttribute("nomeProdotto", nomeProdotto);
		model.addAttribute("parola", parola);
		model.addAttribute("data", data);

		return "admin/viewAllCommenti";
	}

	@GetMapping("/deleteProdotto/{id}")
	public String deleteProdotto(@PathVariable Long id) {

		Prodotto prodotto = prodottoService.findById(id);

		// Rimuovi questo prodotto da tutti i prodotti che lo hanno in prodottiSimili
		List<Prodotto> tuttiProdotti = prodottoService.findAll();
		for (Prodotto p : tuttiProdotti) {
			if (p.getProdottiSimili().contains(prodotto)) {
				p.getProdottiSimili().remove(prodotto);
				prodottoService.save(p);
			}
		}

		// Elimina il commento
		prodottoService.delete(prodotto);

		return "redirect:/catalogo"; // Reindirizza alla pagina del prodotto
	}

	@GetMapping("/editProdotto/{id}")
	public String editProdottoForm(@PathVariable Long id, Model model) {
		Prodotto prodotto = prodottoService.findById(id);
		if (prodotto == null) {
			return "redirect:/admin/indexAdmin"; // prodotto non trovato
		}

		model.addAttribute("prodotto", prodotto);
		model.addAttribute("tipologie", tipologiaService.findAll());
		model.addAttribute("prodotti", prodottoService.findAll());

		return "admin/edit/editProdotto"; // la view del form modifica prodotto
	}

	@PostMapping("/editProdotto/{id}")
	@Transactional
	public String updateProdotto(@PathVariable Long id, @ModelAttribute("prodotto") @Valid Prodotto prodottoModificato,
			BindingResult result,
			@RequestParam(value = "tipologieSelect", required = false) String tipologiaSelezionata,
			@RequestParam(value = "nomeTipologia", required = false) String nomeTipologia,
			@RequestParam(value = "prodottiSimili", required = false) List<Long> prodottiSimiliIds,
			@RequestParam("fileImmagine") MultipartFile fileImmagine, Model model) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("tipologie", tipologiaService.findAll());
			model.addAttribute("prodotti", prodottoService.findAll());
			return "admin/edit/editProdotto";
		}

		// Carica il prodotto originale dal DB
		Prodotto prodottoOriginale = prodottoService.findById(id);
		if (prodottoOriginale == null) {
			return "redirect:/admin/indexAdmin"; // prodotto non trovato
		}

		// Aggiorna i campi semplici
		prodottoOriginale.setNome(prodottoModificato.getNome());
		prodottoOriginale.setPrezzo(prodottoModificato.getPrezzo());
		prodottoOriginale.setDescrizione(prodottoModificato.getDescrizione());

		// Gestione immagine
		if (!fileImmagine.isEmpty()) {
			String nomeFile = UUID.randomUUID() + "_" + fileImmagine.getOriginalFilename();
			Path percorso = Paths.get("src/main/resources/static/images/prodotti", nomeFile);
			Files.createDirectories(percorso.getParent());
			Files.write(percorso, fileImmagine.getBytes());
			prodottoOriginale.setImmagine(nomeFile);
		}

		// Gestione tipologia
		Tipologia tipologia = null;

		if ("__new__".equals(tipologiaSelezionata)) {
			if (nomeTipologia == null || nomeTipologia.trim().isEmpty()) {
				result.rejectValue("tipologia", "NotBlank", "Inserisci il nome della nuova tipologia");
				model.addAttribute("tipologie", tipologiaService.findAll());
				model.addAttribute("prodotti", prodottoService.findAll());
				return "admin/edit/editProdotto";
			}
			tipologia = tipologiaService.findByNome(nomeTipologia.trim());
			if (tipologia == null) {
				tipologia = new Tipologia();
				tipologia.setNome(nomeTipologia.trim());
			}
		} else {
			tipologia = tipologiaService.findByNome(tipologiaSelezionata);
		}

		prodottoOriginale.setTipologia(tipologia);
		tipologia.add(prodottoOriginale);

		// Gestione prodotti simili: aggiorna la lista
		if (prodottiSimiliIds != null && !prodottiSimiliIds.isEmpty()) {
			List<Prodotto> prodottiSimili = prodottoService.findAllById(prodottiSimiliIds);
			prodottoOriginale.setProdottiSimili(prodottiSimili);
		} else {
			prodottoOriginale.getProdottiSimili().clear();
		}

		tipologiaService.save(tipologia);
		prodottoService.save(prodottoOriginale);

		return "redirect:/prodotto/" + id; // Reindirizza alla pagina del prodotto
	}
}