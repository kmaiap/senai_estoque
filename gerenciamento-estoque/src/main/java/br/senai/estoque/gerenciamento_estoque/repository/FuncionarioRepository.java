package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByNifIgnoreCaseAndAtivoTrue(String nif);

    boolean existsByNifIgnoreCase(String nif);
}
