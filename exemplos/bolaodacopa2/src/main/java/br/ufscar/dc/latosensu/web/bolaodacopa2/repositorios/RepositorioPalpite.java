package br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Palpite;


public interface RepositorioPalpite extends CrudRepository<Palpite, Long> {
    @Query("from Palpite p where p.palpiteiro.email = :email")
    Palpite findByPalpiteiroEmail(@Param("email") String email);
}