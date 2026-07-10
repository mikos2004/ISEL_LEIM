-- ISEL 24/25 - SBD
-- Miguel Alcobia - A50746
-- Tomás Salvador - A50766

-- Carregar dados na BD --
USE SBD_A50746_A50766;

-- Inserir dados na tabela Cliente
INSERT INTO Cliente (numeroFiscal, nome, contacto, morada, distrito, concelho, freguesia, preferenciasLinguisticasECulturais, moeda)
VALUES
('123456789', 'João Silva', '912345678', 'Rua das Flores, 12', 'Lisboa', 'Lisboa', 'Lumiar', '["Português", "Inglês"]', 'EUR'),
('987654321', 'Maria Costa', '911234567', 'Avenida Principal, 45', 'Porto', 'Porto', 'Cedofeita', '["Português", "Francês"]', 'EUR'),
('456123789', 'Carlos Santos', '913456789', 'Travessa da Paz, 3', 'Faro', 'Faro', 'Sé', '["Espanhol", "Português"]', 'USD'),
('477654321', 'Empresa XPTO', '913456789', 'Rua B', 'Porto', 'Porto', 'Cedofeita', "Inglês", 'EUR'),
('234567890', 'Paulo Horta', '912345678', 'Rua A', 'Lisboa', 'Lisboa', 'Alvalade', 'Português', 'EUR'),
('587582375', 'Empresa Huan', '935475612', 'Rua do Largo', 'Lisboa', 'Cascais', 'Estoril', '["Português", "Inglês", "Mandarim"]', 'CNY'),
('789456123', 'TechNova Solutions', '912345678', 'Avenida Central', 'Porto', 'Gaia', 'Canidelo', '["Português", "Espanhol", "Inglês"]', 'EUR'),
('123987654', 'Transportes Silva', '913578246', 'Travessa do Comércio', 'Setúbal', 'Almada', 'Caparica', '["Português"]', 'BRL');

-- Associar o cliente à Pessoa
INSERT INTO Pessoa (numeroFiscal)
VALUES 
('123456789'),
('987654321'),
('456123789'),
('234567890');

-- Associar o cliente à Empresa com capital social
INSERT INTO Empresa (numeroFiscal, capitalSocial)
VALUES 
('477654321', 50000.00),
('587582375', 100000.00), 
('789456123', 750000.00), 
('123987654', 250000.00); 

-- Inserir dados na tabela Condutor
INSERT INTO Condutor (numeroCartaConducao, nome, validadeCarta, reputacao)
VALUES
('123456789012', 'Miguel Carvalho', '2030-05-20', NULL),
('987654321098', 'Jorge Almeida', '2028-12-15', NULL),
('456123789456', 'Francisco Peixoto', '2025-09-10', NULL);

-- Inserir dados na tabela Parque
INSERT INTO Parque (localidade, morada, latitude, longitude)
VALUES
('Lisboa', 'Rua do Estacionamento, 10', 38.7169, -9.1390),
('Porto', 'Avenida do Parque, 25', 41.1496, -8.6109),
('Faro', 'Praça Central, 8', 37.0194, -7.9304);

-- Inserir dados na tabela LugarParque
INSERT INTO LugarParque (localidade, piso, fila, posicao, tipoVeiculoAdmitido)
VALUES
('Lisboa', 1, 'A', 1, 'Familiar'),
('Lisboa', 1, 'A', 2, 'Comercial'),
('Porto', 2, 'B', 5, 'Motociclo');

-- Inserir dados na tabela Tipo
INSERT INTO TipoVeiculo (nomeCategoria, tempoCarta, tarifa, idadeMinima)
VALUES
('Familiar', 1, 30.00, 18),
('Comercial', 3, 50.00, 21),
('Motociclo', 0, 15.00, 16);

-- Inserir dados na tabela Veiculo
INSERT INTO Veiculo (matricula, marca, modelo, fotos, cor, tipo, numeroLugares, numeroPortas, capacidadeCarga, tipoMotor, potencia, localidade, nomeCategoria)
VALUES
('11-AA-11', 'Renault', 'Clio', NULL, 'Vermelho', 'Familiar', 5, 4, 300.0, 'Combustão', 66.0, 'Lisboa', 'Familiar'),
('22-BB-22', 'Ford', 'Transit', NULL, 'Branco', 'Comercial', 3, 4, 1000.0, 'Diesel', 96.0, 'Lisboa', 'Comercial'),
('33-CC-33', 'Honda', 'CBR500R', NULL, 'Preto', 'Motociclo', 2, 0, 0.0, 'Combustão', 35.0, 'Porto', 'Motociclo');

-- Inserir dados na tabela Desconto
INSERT INTO Desconto (codigo, valor, descricao, dataHoraInicio, dataHoraFim)
VALUES
('DSC01', 0.10, '10% de desconto para novos clientes', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('DSC02', 0.15, '15% para alugueres superiores a 7 dias', '2024-06-01 00:00:00', '2024-08-31 23:59:59'),
('DSC03', 0.05, '5% de desconto em alugueres de comerciais', '2024-03-01 00:00:00', '2024-12-31 23:59:59');

-- Inserir dados na tabela Cliente_Desconto
INSERT INTO Cliente_Desconto (numeroFiscal, codigo)
VALUES
('123456789', 'DSC01'),
('987654321', 'DSC02'),
('456123789', 'DSC03');

-- Inserir dados na tabela Aluguer
INSERT INTO Aluguer (dataHoraInicio, dataHoraFim, duracao, custoFinal, avaliacao, comentario, numeroFiscal, numeroCartaConducao, codigo, matricula)
VALUES
('2024-11-01 08:00:00', '2024-11-07 08:00:00', 
    TIMESTAMPDIFF(SECOND, '2024-11-01 08:00:00', '2024-11-07 08:00:00') / 3600, 
    180.00, 5.0, 'Adorei, vou voltar e recomendar.', '123456789', '123456789012', 'DSC01', '11-AA-11'),
('2024-11-08 08:00:00', '2024-11-014 08:00:00', 
    TIMESTAMPDIFF(SECOND, '2024-11-08 08:00:00', '2024-11-14 08:00:00') / 3600, 
    180.00, 3.5, 'Gostei, mas o interior de veículo tinha alguns sinais de maus tratos', '123456789', '123456789012', NULL, '22-BB-22'),
('2024-11-02 10:00:00', '2024-11-04 10:00:00', 
    TIMESTAMPDIFF(SECOND, '2024-11-02 10:00:00', '2024-11-04 10:00:00') / 3600, 
    120.00, 1.5, 'Nunca mais volto a alugar nenhum carro aqui! O condutor foi muito mal educado. Serviço Lamentável!', '987654321', '987654321098', 'DSC02', '22-BB-22'),
('2024-11-03 12:00:00', '2024-11-03 18:00:00', 
    TIMESTAMPDIFF(SECOND, '2024-11-03 12:00:00', '2024-11-03 18:00:00') / 3600, 
    45.00, 4.8, 'Ótimo serviço', '456123789', '456123789456', NULL, '33-CC-33'),
('2024-04-02 10:00:00', '2024-04-04 10:00:00', 
    TIMESTAMPDIFF(SECOND, '2024-04-02 10:00:00', '2024-04-04 10:00:00') / 3600, 
    120.00, 3.0, 'Mercadoria chegou ligeiramente danificada', '987654321', '456123789456', NULL, '22-BB-22');

-- Atualização das reputações dos condutores
UPDATE Condutor C
SET reputacao = COALESCE(
    (SELECT AVG(A.avaliacao)
     FROM Aluguer A
     WHERE A.numeroCartaConducao = C.numeroCartaConducao), 
    0
)
WHERE EXISTS (
    SELECT 1
    FROM Aluguer A
    WHERE A.numeroCartaConducao = C.numeroCartaConducao
);

-- Inserir dados na tabela Intervencao
INSERT INTO Intervencao (quilometragem, tipo, matricula)
VALUES
(15000, 'Revisão', '11-AA-11'),
(5000, 'Troca de óleo', '22-BB-22'),
(1000, 'Manutenção geral', '33-CC-33');

-- Inserir dados na tabela Parque_Tipo
INSERT INTO Parque_Tipo (localidade, nomeCategoria)
VALUES
('Lisboa', 'Familiar'),
('Lisboa', 'Comercial'),
('Porto', 'Motociclo');

