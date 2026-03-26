package br.senai.estoque.gerenciamento_estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.CategoriaMaterial;

public interface CategoriaMaterialRepository extends JpaRepository<CategoriaMaterial, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}
