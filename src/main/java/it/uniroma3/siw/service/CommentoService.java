package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CommentoRepository;

@Service
public class CommentoService {

	private static final Logger logger = LoggerFactory.getLogger(CommentoService.class);

	@Autowired
	private CommentoRepository commentoRepository;

	public Commento findByProdottoAndUser(Prodotto prodotto, User user) {
		return commentoRepository.findByProdottoAndUserOrderByDataDesc(prodotto, user);
	}

	public void save(Commento commento) {
		commentoRepository.save(commento);

	}

	public boolean existsById(Long id) {
		return commentoRepository.existsById(id);
	}

	public Commento findById(Long id) {
		return commentoRepository.findById(id).orElse(null);
	}

	public void delete(Commento commento) {
		commentoRepository.delete(commento);
	}

	public List<Commento> findByUser(User user) {
		return commentoRepository.findByUser(user);
	}

	public List<Commento> findByUserOrderByDataDesc(User user) {
		return commentoRepository.findByUserOrderByDataDesc(user);
	}

	public List<Commento> findAllByOrderByDataDesc() {
		return commentoRepository.findAllByOrderByDataDesc();
	}

	public List<Commento> filtraCommenti(String nomeProdotto, String parola, LocalDate data) {
		// Se tutti null, restituisci lista vuota
		if ((nomeProdotto == null || nomeProdotto.isBlank()) && (parola == null || parola.isBlank()) && data == null) {
			return Collections.emptyList();
		}

		String dataStr = (data != null) ? data.toString() : null; // Formato YYYY-MM-DD
		return commentoRepository.filtraCommenti(nomeProdotto, parola, dataStr);
	}

}
