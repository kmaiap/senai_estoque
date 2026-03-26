package br.senai.estoque.gerenciamento_estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.MovimentacaoEstoque;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findTop5ByOrderByDataMovimentacaoDesc();
}
