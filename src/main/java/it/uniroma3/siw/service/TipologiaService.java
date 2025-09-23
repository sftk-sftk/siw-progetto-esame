package it.uniroma3.siw.service;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Tipologia;
import it.uniroma3.siw.repository.TipologiaRepository;

@Service
public class TipologiaService {
	
	@Autowired
	private TipologiaRepository tipologiaRepository;

	public List<Tipologia> findAll() {
		Iterable<Tipologia> iterable = tipologiaRepository.findAll();
		List<Tipologia> lista = new ArrayList<>();
		iterable.forEach(lista::add);
		return lista;
	}

	public Tipologia findByNome(String nomeTipologia) {
		return tipologiaRepository.findByNome(nomeTipologia);
	}

	public void save(Tipologia tipologia) {
		tipologiaRepository.save(tipologia);
	}

}
