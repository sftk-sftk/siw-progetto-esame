package it.uniroma3.siw.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.ProdottoRepository;

@Service
public class ProdottoService {

	@Autowired
	private ProdottoRepository prodottoRepository;

	public List<Prodotto> find3Prodotti() {
		return prodottoRepository.find3Prodotti();
	}

	public List<Prodotto> findAll() {
		Iterable<Prodotto> iterable = prodottoRepository.findAll();
		List<Prodotto> lista = new ArrayList<>();
		iterable.forEach(lista::add);
		return lista;
	}

	public List<Prodotto> filtraProdotti(String nome, Long tipologiaId, BigDecimal prezzoMax) {
		String nomeLowerCase = new String();
		if (nome != null)
			nomeLowerCase = nome.toLowerCase();
		return prodottoRepository.findByFiltri(nomeLowerCase, tipologiaId, prezzoMax);
	}

	public void save(Prodotto prodotto) {
		prodottoRepository.save(prodotto);
		
	}

	public List<Prodotto> findAllById(List<Long> ids) {
	    //return prodottoRepository.findAllById(ids);
	    Iterable<Prodotto> iterable = prodottoRepository.findAllById(ids);
		List<Prodotto> lista = new ArrayList<>();
		iterable.forEach(lista::add);
		return lista;
	}

	public List<Prodotto> find3Prodotti10Euro() {
		return prodottoRepository.findTop3ByPrezzoLessThanEqualOrderByIdDesc(BigDecimal.valueOf(10.00));
	}

	public List<Prodotto> findProdotti10Euro() {
		return prodottoRepository.findByPrezzoLessThanEqualOrderByPrezzoAsc(BigDecimal.valueOf(10.00));
	}

	public Prodotto findById(Long id) {
		return prodottoRepository.findById(id).orElse(null);
	}

	public List<Prodotto> findTopByCommenti(Pageable pageable) {
		return prodottoRepository.findTop3ByCommenti(pageable);
	}

	public void delete(Prodotto prodotto) {
		prodottoRepository.delete(prodotto);
		
	}


}
