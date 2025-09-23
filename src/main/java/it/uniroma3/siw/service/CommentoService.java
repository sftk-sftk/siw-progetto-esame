package it.uniroma3.siw.service;

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
		return commentoRepository.findByProdottoAndUser(prodotto,user);
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

}
