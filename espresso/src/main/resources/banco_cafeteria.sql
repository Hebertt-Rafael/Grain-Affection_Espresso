-- ==============================================================================
-- SCRIPT DE CRIAÇÃO E POPULAÇÃO DO BANCO DE DADOS
-- Projeto: Grain & Affection Espresso - Cafeteria Gourmet
-- Disciplina: Prática Diagnóstica em Desenvolvimento Web
-- Banco de Dados: MySQL 8.0+
-- Regra: Pelo menos quatro registros para todas as tabelas criadas.
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS cafeteria_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cafeteria_db;

-- ------------------------------------------------------------------------------
-- 1. TABELA: usuarios
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS itens_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    telefone VARCHAR(30),
    endereco VARCHAR(255),
    perfil VARCHAR(20) NOT NULL -- 'CLIENTE', 'GERENTE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------------------------
-- 2. TABELA: categorias (Grupos de Produtos do Anexo I)
-- ------------------------------------------------------------------------------
CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE,
    descricao VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------------------------
-- 3. TABELA: produtos (3 itens por grupo do Anexo I)
-- ------------------------------------------------------------------------------
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao TEXT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    imagem VARCHAR(255),
    id_categoria BIGINT NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_produto_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------------------------
-- 4. TABELA: pedidos
-- Critério essencial: Atendimento no Balcão, Mesas (1 a 15) ou Entrega.
-- Status administrativo: "EM_ATENDIMENTO" ou "FINALIZADO"
-- ------------------------------------------------------------------------------
CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    data_pedido DATETIME NOT NULL,
    tipo_atendimento VARCHAR(20) NOT NULL, -- 'BALCAO', 'MESA', 'ENTREGA'
    numero_mesa INT NULL,                 -- 1 a 15 (se tipo_atendimento = 'MESA')
    endereco_entrega VARCHAR(255) NULL,    -- Endereço (se tipo_atendimento = 'ENTREGA')
    status VARCHAR(25) NOT NULL,          -- 'EM_ATENDIMENTO', 'FINALIZADO'
    valor_total DECIMAL(10,2) NOT NULL,
    observacoes TEXT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------------------------
-- 5. TABELA: itens_pedido
-- ------------------------------------------------------------------------------
CREATE TABLE itens_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (id_produto) REFERENCES produtos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==============================================================================
-- CARGA DE DADOS (DML) - MÍNIMO 4 REGISTROS POR TABELA
-- ==============================================================================

-- 1. Inserção de Usuários (2 Gerentes e 3 Clientes = 5 registros)
INSERT INTO usuarios (nome, email, senha, telefone, endereco, perfil) VALUES
('Marina Alves (Gerente)', 'gerente@grainaffection.com.br', 'admin123', '(11) 98765-4321', 'Rua Augusta, 1200 - Consolação, São Paulo', 'GERENTE'),
('Victor Moraes (Gerente)', 'profvmoraes@grainaffection.com.br', 'admin123', '(11) 99999-8888', 'Av. Paulista, 1000 - São Paulo', 'GERENTE'),
('Ana Beatriz Lima', 'ana@exemplo.com', 'cliente123', '(11) 91234-5678', 'Rua Oscar Freire, 15 - Jardins', 'CLIENTE'),
('Lucas Oliveira', 'lucas@exemplo.com', 'cliente123', '(11) 92345-6789', 'Rua das Flores, 88 - Centro', 'CLIENTE'),
('Juliana Santos', 'juliana@exemplo.com', 'cliente123', '(11) 93456-7890', 'Alameda Santos, 450 - Cerqueira César', 'CLIENTE');

-- 2. Inserção de Categorias (7 grupos do Anexo I = 7 registros)
INSERT INTO categorias (id, nome, descricao) VALUES
(1, 'Bebidas Quentes', 'Espressos aromáticos, cappuccinos e infusões'),
(2, 'Bebidas Geladas', 'Iced coffees, cold brews e bebidas refrescantes'),
(3, 'Padaria e Confeitaria', 'Croissants, bolos e confeitaria artesanal'),
(4, 'Lanches Salgados', 'Sanduíches, pão de queijo recheado e salgados'),
(5, 'Sobremesas', 'Pudins, tiramisù e doces artesanais'),
(6, 'Opções Saudáveis', 'Opções leves, saladas, iogurte e tapioca'),
(7, 'Produtos para Venda', 'Grãos de café especiais, canecas e itens exclusivos');

-- 3. Inserção de Produtos (3 itens de cada um dos 7 grupos = 21 registros)
INSERT INTO produtos (nome, descricao, preco, imagem, id_categoria, disponivel) VALUES
-- Grupo 1: Bebidas Quentes
('Café Expresso', 'Extrato puro de grão 100% arábica de torra média, crema aveludada e notas intensas de cacau e caramelo.', 14.80, '/img/espresso.jpg', 1, TRUE),
('Cappuccino', 'Harmonia perfeita entre espresso encorpado, leite vaporizado em microespuma sedosa e toque de canela.', 26.65, '/img/cappuccino.jpg', 1, TRUE),
('Mocaccino', 'Espresso duplo combinado com ganache de chocolate meio amargo 70%, leite cremoso e raspas de cacau.', 28.50, '/img/mocha.jpg', 1, TRUE),
-- Grupo 2: Bebidas Geladas
('Café Gelado (Iced Coffee)', 'Espresso duplo sobre pedras de gelo cristalino com leite cremoso e toque de fava de baunilha.', 24.50, '/img/icedlatte.jpg', 2, TRUE),
('Cold Brew', 'Café especial infusionado a frio lentamente por 18 horas, suave, refrescante e com notas cítricas.', 27.90, '/img/coldbrew.jpg', 2, TRUE),
('Frappé', 'Batido gelado e cremoso de café espresso especial, sorvete de creme, calda de caramelo salgado e chantilly.', 31.00, '/img/macchiato.jpg', 2, TRUE),
-- Grupo 3: Padaria e Confeitaria
('Croissant', 'Massa folhada francesa de fermentação lenta, dourada e crocante por fora e macia com manteiga extra por dentro.', 16.50, '/img/croissant.jpg', 3, TRUE),
('Brownie', 'Brownie fudge de chocolate belga meio amargo com casquinha crocante, interior úmido e nozes pecan.', 18.90, '/img/brownie.jpg', 3, TRUE),
('Cheesecake', 'Base crocante amanteigada, creme suave de cream cheese com toque cítrico e calda rústica de frutas vermelhas.', 24.00, '/img/cheesecake.jpg', 3, TRUE),
-- Grupo 4: Lanches Salgados
('Sanduíche Natural', 'Ciabatta artesanal tostada no azeite com muçarela de búfala, tomate confit, folhas verdes e pesto de manjericão.', 26.00, '/img/caprese.jpg', 4, TRUE),
('Misto Quente', 'Pão brioche dourado na manteiga clarificada com queijo gruyère e prato derretidos e presunto royal.', 22.50, '/img/torrada.jpg', 4, TRUE),
('Pão de Queijo Recheado', 'Pão de queijo mineiro da Serra da Canastra assado na hora, recheado com requeijão cremoso de corte.', 12.00, '/img/paodequeijo.jpg', 4, TRUE),
-- Grupo 5: Sobremesas
('Tiramisù', 'Sobremesa clássica italiana com biscoitos embebidos em café espresso, creme de mascarpone e cacau puro.', 34.00, '/img/tiramisu.jpg', 5, TRUE),
('Petit Gâteau', 'Bolinho quente de chocolate nobre com centro fluido, acompanhado de gelato de baunilha e calda quente.', 36.50, '/img/affogato.jpg', 5, TRUE),
('Pudim', 'Pudim de leite condensado lisinho e aveludado com fava de baunilha e calda âmbar de caramelo.', 21.00, '/img/pudim.jpg', 5, TRUE),
-- Grupo 6: Opções Saudáveis
('Salada de Frutas', 'Bowl refrescante de frutas da estação selecionadas com sementes de chia, hortelã fresca e mel silvestre.', 21.50, '/img/salada-frutas.jpg', 6, TRUE),
('Iogurte com Granola', 'Parfait de iogurte grego natural com granola artesanal de castanhas, melado e compota de frutas vermelhas.', 23.00, '/img/parfait-iogurte.jpg', 6, TRUE),
('Tapioca', 'Goma fininha e crocante na chapa recheada com queijo minas frescal, tomate seco artesanal e orégano.', 22.00, '/img/tapioca.jpg', 6, TRUE),
-- Grupo 7: Produtos para Venda
('Grãos de Café Especiais (250g)', 'Pacote com válvula aromática de grãos 100% arábica de torra média, notas de chocolate, avelã e caramelo.', 48.00, '/img/graos-cafe.jpg', 7, TRUE),
('Caneca Personalizada', 'Caneca exclusiva em cerâmica artesanal fosca (320ml) com detalhes dourados e selo Grain & Affection.', 55.00, '/img/caneca.jpg', 7, TRUE),
('Geleia Artesanal', 'Pote de geleia caseira de frutas vermelhas cozida lentamente em tacho, sem conservantes (200g).', 32.00, '/img/geleia.jpg', 7, TRUE);

-- 4. Inserção de Pedidos (5 pedidos demonstrando Balcão, Mesas 1 a 15 e Entrega)
INSERT INTO pedidos (id, id_usuario, data_pedido, tipo_atendimento, numero_mesa, endereco_entrega, status, valor_total, observacoes) VALUES
(1, 3, '2026-09-29 08:30:00', 'BALCAO', NULL, NULL, 'FINALIZADO', 41.45, 'Sem açúcar no café, por favor.'),
(2, 4, '2026-09-29 09:15:00', 'MESA', 4, NULL, 'EM_ATENDIMENTO', 47.00, 'Mesa próxima à janela.'),
(3, 5, '2026-09-29 09:40:00', 'ENTREGA', NULL, 'Alameda Santos, 450 - Apto 82 - Cerqueira César', 'EM_ATENDIMENTO', 55.50, 'Interfone tocar duas vezes.'),
(4, 3, '2026-09-29 10:10:00', 'MESA', 12, NULL, 'FINALIZADO', 28.50, 'Porção extra de canela.'),
(5, 4, '2026-09-29 10:35:00', 'ENTREGA', NULL, 'Rua das Flores, 88 - Centro', 'EM_ATENDIMENTO', 89.50, 'Embalar bem para entrega de moto.');

-- 5. Inserção de Itens de Pedido (9 registros cobrindo todos os pedidos)
INSERT INTO itens_pedido (id_pedido, id_produto, quantidade, preco_unitario, subtotal) VALUES
(1, 1, 1, 14.80, 14.80),
(1, 2, 1, 26.65, 26.65),
(2, 4, 1, 24.50, 24.50),
(2, 11, 1, 22.50, 22.50),
(3, 13, 1, 34.00, 34.00),
(3, 7, 1, 16.50, 16.50),
(4, 3, 1, 28.50, 28.50),
(5, 14, 1, 36.50, 36.50),
(5, 19, 1, 48.00, 48.00);

-- Fim do Script