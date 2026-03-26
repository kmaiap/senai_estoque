merge into funcionarios_autorizados (id, nome, nif, ativo) key(id) values
    (1, 'Maria Silva', 'NIF1001', true),
    (2, 'Joao Souza', 'NIF1002', true);

merge into categorias_materiais (id, nome, descricao) key(id) values
    (1, 'Informatica', 'Equipamentos e acessorios de laboratorio'),
    (2, 'Escritorio', 'Materiais administrativos');

merge into materiais (id, nome, descricao, quantidade_disponivel, unidade_medida, categoria_id) key(id) values
    (1, 'Mouse USB', 'Mouse optico para laboratorios', 25, 'unidade', 1),
    (2, 'Caderno', 'Caderno para atividades internas', 40, 'unidade', 2);

merge into ativos_patrimoniais (id, nome, descricao, codigo_patrimonio, localizacao, estado_conservacao, ativo) key(id) values
    (1, 'Projetor Epson', 'Projetor multimidia da sala 3', 'PAT-2001', 'Sala 3', 'Bom', true),
    (2, 'Notebook Dell', 'Notebook de apoio do laboratorio', 'PAT-2002', 'Laboratorio 1', 'Otimo', true);
