package br.senai.estoque.gerenciamento_estoque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioAutorizadoRepository;
import br.senai.estoque.gerenciamento_estoque.repository.FuncionarioRepository;
import br.senai.estoque.gerenciamento_estoque.util.SenhaUtil;

@Service
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioAutorizadoRepository funcionarioAutorizadoRepository;

    public AuthService(FuncionarioRepository funcionarioRepository,
            FuncionarioAutorizadoRepository funcionarioAutorizadoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioAutorizadoRepository = funcionarioAutorizadoRepository;
    }

    public Funcionario cadastrar(String nome, String nif, String senha) {
        String nomeLimpo = validarTexto(nome, "Nome");
        String nifLimpo = validarTexto(nif, "NIF").toUpperCase();

        if (senha == null || senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
        }

        if (!funcionarioAutorizadoRepository.existsByNifIgnoreCaseAndNomeIgnoreCaseAndAtivoTrue(nifLimpo, nomeLimpo)) {
            throw new IllegalArgumentException("NIF e nome nao estao autorizados para cadastro.");
        }

        if (funcionarioRepository.existsByNifIgnoreCase(nifLimpo)) {
            throw new IllegalArgumentException("Ja existe um funcionario cadastrado com este NIF.");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nomeLimpo);
        funcionario.setNif(nifLimpo);
        funcionario.setSenhaHash(SenhaUtil.gerarHash(senha));
        funcionario.setAtivo(true);

        return funcionarioRepository.save(funcionario);
    }

    public Optional<Funcionario> autenticar(String nif, String senha) {
        String nifLimpo = validarTexto(nif, "NIF").toUpperCase();
        String senhaHash = SenhaUtil.gerarHash(senha == null ? "" : senha);

        return funcionarioRepository.findByNifIgnoreCaseAndAtivoTrue(nifLimpo)
                .filter(funcionario -> funcionario.getSenhaHash().equals(senhaHash));
    }

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " e obrigatorio.");
        }

        return valor.trim();
    }
}
