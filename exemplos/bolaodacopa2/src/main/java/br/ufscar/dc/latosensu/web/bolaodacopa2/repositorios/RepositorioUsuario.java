package br.ufscar.dc.latosensu.web.bolaodacopa2.repositorios;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.latosensu.web.bolaodacopa2.entidades.Usuario;

public interface RepositorioUsuario extends CrudRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
