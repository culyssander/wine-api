package br.com.globalsys.wineapi.models.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.globalsys.wineapi.models.domain.CEP;

public interface CEPRepository extends JpaRepository<CEP, Long>{
	
	Optional<CEP> findByFaixaInicioLessThanEqualAndFaixaFimGreaterThanEqual(Integer faixaInicio, Integer faixaFim);
	
	@Query("select c from CEP c where c.faixaInicio between :faixaInicio and :faixaFim or c.faixaFim between :faixaInicio and :faixaFim")
	List<CEP> findByFaixa(@Param("faixaInicio") Integer faixaInicio, @Param("faixaFim") Integer faixaFim);

}
