# Sistema de Gerenciamento de Estoque e Patrimonio

## Descricao do projeto
Projeto didatico em Java com Maven e Spring Boot para controlar materiais, movimentacoes de estoque e ativos patrimoniais de uma unidade escolar do SENAI-SP.

## Objetivo do sistema
Permitir o cadastro e acesso de funcionarios autorizados por NIF, acompanhar o estoque de materiais e organizar o patrimonio da escola em uma base simples para estudos.

## Requisitos Funcionais
- Cadastrar funcionario apenas se nome e NIF estiverem na lista de autorizacao.
- Realizar login com NIF e senha.
- Exibir uma area interna apos autenticacao.
- Cadastrar, listar, buscar, atualizar e excluir categorias de materiais.
- Cadastrar, listar, buscar, atualizar e excluir materiais.
- Registrar entradas e saidas de estoque com atualizacao da quantidade disponivel.
- Cadastrar, listar, buscar, atualizar e excluir ativos patrimoniais.
- Disponibilizar um endpoint de teste `GET /ping`.
- Exibir telas web simples com Thymeleaf para pagina inicial, login, cadastro e dashboard.

## Requisitos Nao Funcionais
- Codigo simples, legivel e adequado para iniciantes.
- Estrutura Maven reaproveitada sem criar um novo projeto.
- Separacao basica de responsabilidades entre controller, service, repository, model e util.
- Persistencia com JPA usando banco H2 em memoria para facilitar testes locais.
- Interface web responsiva com identidade visual inspirada no SENAI.

## Estrutura do projeto
O projeto Maven esta dentro da pasta `gerenciamento-estoque/`.

```text
gerenciamento-estoque/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/br/senai/estoque/gerenciamento_estoque/
    │   │   ├── GerenciamentoEstoqueApplication.java
    │   │   ├── controller/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── util/
    │   └── resources/
    │       ├── static/
    │       ├── templates/
    │       ├── application.properties
    │       └── data.sql
    └── test/
```

## Tecnologias utilizadas
- Java 21
- Maven
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Thymeleaf
- H2 Database

## Dados iniciais
Ao iniciar a aplicacao, alguns registros de exemplo sao carregados:
- Funcionarios autorizados para cadastro.
- Categorias e materiais iniciais.
- Ativos patrimoniais iniciais.

## Como executar
1. Abra um terminal na pasta `gerenciamento-estoque`.
2. Execute `./mvnw spring-boot:run` no Linux/macOS ou `mvnw.cmd spring-boot:run` no Windows.
3. Acesse `http://localhost:8080`.
4. Para cadastrar um usuario, use um dos NIFs autorizados carregados no `data.sql`.
