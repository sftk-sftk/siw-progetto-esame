package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;

@Repository
public interface CommentoRepository extends CrudRepository<Commento, Long> {

	Commento findByProdottoAndUserOrderByDataDesc(Prodotto prodotto, User user);

	List<Commento> findByUser(User user);

	List<Commento> findByUserOrderByDataDesc(User user);

	List<Commento> findAllByOrderByDataDesc();

	/*
	 * @Query("SELECT c FROM Commento c WHERE (:nomeProdotto IS NULL OR LOWER(c.prodotto.nome) LIKE LOWER(CONCAT('%', :nomeProdotto, '%'))) AND (:parola IS NULL OR LOWER(c.descrizione) LIKE LOWER(CONCAT('%', :parola, '%'))) AND (:data IS NULL OR c.data = :data)"
	 * ) List<Commento> filtraCommenti(@Param("nomeProdotto") String
	 * nomeProdotto, @Param("parola") String parola,
	 * 
	 * @Param("data") LocalDate data);
	 */
	/*
	 * @Query("SELECT c FROM Commento c WHERE " +
	 * "(:nomeProdotto IS NULL OR c.prodotto.nome LIKE CONCAT('%', :nomeProdotto, '%')) AND "
	 * + "(:parola IS NULL OR c.descrizione LIKE CONCAT('%', :parola, '%')) AND " +
	 * "(:data IS NULL OR c.data = :data)") List<Commento>
	 * filtraCommenti(@Param("nomeProdotto") String nomeProdotto, @Param("parola")
	 * String parola,
	 * 
	 * @Param("data") LocalDate data);
	 */

	
	@Query("SELECT c FROM Commento c JOIN c.prodotto p WHERE "
			+ "(:nomeProdotto IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomeProdotto, '%'))) AND "
			+ "(:parola IS NULL OR LOWER(c.descrizione) LIKE LOWER(CONCAT('%', :parola, '%'))) AND "
			+ "(:dataStr IS NULL OR TO_CHAR(c.data, 'YYYY-MM-DD') = :dataStr)")
	List<Commento> filtraCommenti(@Param("nomeProdotto") String nomeProdotto, @Param("parola") String parola,
			@Param("dataStr") String dataStr);
}
