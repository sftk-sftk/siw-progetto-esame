package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Tipologia;

@Repository
public interface TipologiaRepository extends CrudRepository<Tipologia, Long> {

	Tipologia findByNome(String nomeTipologia);

	
}
