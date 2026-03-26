package br.senai.estoque.gerenciamento_estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.FuncionarioAutorizado;

public interface FuncionarioAutorizadoRepository extends JpaRepository<FuncionarioAutorizado, Long> {

    boolean existsByNifIgnoreCaseAndNomeIgnoreCaseAndAtivoTrue(String nif, String nome);
}
