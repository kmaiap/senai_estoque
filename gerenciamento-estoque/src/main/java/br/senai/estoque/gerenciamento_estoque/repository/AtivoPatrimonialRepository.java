package br.senai.estoque.gerenciamento_estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;

public interface AtivoPatrimonialRepository extends JpaRepository<AtivoPatrimonial, Long> {

    boolean existsByCodigoPatrimonioIgnoreCase(String codigoPatrimonio);
}
