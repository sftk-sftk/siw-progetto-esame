package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Prodotto;

@Repository
public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {

	@Query(value = "SELECT * FROM prodotto ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
	List<Prodotto> find5Prodotti();

}
