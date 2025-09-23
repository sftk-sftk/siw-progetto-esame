package it.uniroma3.siw.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Prodotto;

@Repository
public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {

	@Query(value = "SELECT * FROM prodotto ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
	List<Prodotto> find3Prodotti();

	@Query("SELECT p FROM Prodotto p WHERE "
			+ "(:nome IS NULL OR p.nome LIKE CONCAT('%', :nome, '%')) AND "
			+ "(:tipologiaId IS NULL OR p.tipologia.id = :tipologiaId) AND "
			+ "(:prezzoMax IS NULL OR p.prezzo <= :prezzoMax)")
	List<Prodotto> findByFiltri(@Param("nome") String nome, @Param("tipologiaId") Long tipologiaId,
			@Param("prezzoMax") BigDecimal prezzoMax);

	List<Prodotto> findTop3ByPrezzoLessThanEqualOrderByIdDesc(BigDecimal prezzo);

	List<Prodotto> findByPrezzoLessThanEqualOrderByPrezzoAsc(BigDecimal prezzo);

}
