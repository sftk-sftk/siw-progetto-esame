package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Prodotto;

@Repository
public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {

}
