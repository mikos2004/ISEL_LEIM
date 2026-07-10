-- ISEL 24/25 - SBD
-- Miguel Alcobia - A50746
-- Tomás Salvador - A50766

CREATE DATABASE SBD_A50746_A50766;
USE SBD_A50746_A50766;

/*-------------------
	  TABELAS
-------------------*/

-- Tabela Cliente
CREATE TABLE Cliente (
    numeroFiscal CHAR(9),
    nome VARCHAR(255) NOT NULL,
    contacto CHAR(9) NOT NULL,
    morada VARCHAR(255) NOT NULL,
    distrito VARCHAR(100) NOT NULL,
    concelho VARCHAR(100) NOT NULL,
    freguesia VARCHAR(100) NOT NULL,
    preferenciasLinguisticasECulturais TEXT,
    moeda CHAR(3) NOT NULL,
    CONSTRAINT PK_Cliente PRIMARY KEY (numeroFiscal)
);

CREATE TABLE Pessoa (
    numeroFiscal CHAR(9),
    CONSTRAINT pk_pessoa PRIMARY KEY (numeroFiscal),
    CONSTRAINT fk_pessoa_cliente FOREIGN KEY (numeroFiscal) REFERENCES Cliente(numeroFiscal)
);

CREATE TABLE Empresa (
    numeroFiscal CHAR(9),
    capitalSocial DECIMAL(15,2) NOT NULL,
    CONSTRAINT pk_empresa PRIMARY KEY (numeroFiscal),
    CONSTRAINT fk_empresa_cliente FOREIGN KEY (numeroFiscal) REFERENCES Cliente(numeroFiscal)
);

-- Tabela Condutor
CREATE TABLE Condutor (
    numeroCartaConducao CHAR(12),
    nome VARCHAR(255) NOT NULL,
    validadeCarta DATE NOT NULL,
    reputacao DECIMAL(3,2),
    CONSTRAINT PK_Condutor PRIMARY KEY (numeroCartaConducao),
    CONSTRAINT CHK_Reputacao CHECK (reputacao BETWEEN 0 AND 5)
);

-- Tabela Parque
CREATE TABLE Parque (
    localidade VARCHAR(100),
    morada VARCHAR(255) NOT NULL,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL,
    CONSTRAINT PK_Parque PRIMARY KEY (localidade)
);

-- Tabela Tipo Veiculo
CREATE TABLE TipoVeiculo (
    nomeCategoria VARCHAR(50),
    tempoCarta INTEGER NOT NULL,
    tarifa DECIMAL(10,2) NOT NULL,
    idadeMinima INTEGER NOT NULL,
    CONSTRAINT PK_Tipo PRIMARY KEY (nomeCategoria)
);

-- Tabela Desconto
CREATE TABLE Desconto (
    codigo CHAR(6),
    valor DECIMAL(3,2),
    descricao VARCHAR(255),
    dataHoraInicio DATETIME NOT NULL,
    dataHoraFim DATETIME NOT NULL,
    CONSTRAINT PK_Desconto PRIMARY KEY (codigo),
    CONSTRAINT CHK_ValorDesconto CHECK (valor BETWEEN 0 AND 1)
);

-- Tabela Lugar no Parque
CREATE TABLE LugarParque (
    localidade VARCHAR(100),
    piso INTEGER NOT NULL,
    fila CHAR(3) NOT NULL,
    posicao INTEGER NOT NULL,
    tipoVeiculoAdmitido VARCHAR(50) NOT NULL,
    CONSTRAINT PK_LugarParque PRIMARY KEY (localidade, piso, fila, posicao),
    CONSTRAINT FK_LugarParque_Parque FOREIGN KEY (localidade) REFERENCES Parque(localidade)
);

-- Tabela Veículo
CREATE TABLE Veiculo (
    matricula CHAR(8),
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    fotos LONGTEXT,
    cor VARCHAR(20) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    numeroLugares INTEGER NOT NULL,
    numeroPortas INTEGER NOT NULL,
    capacidadeCarga DECIMAL(6,2),
    tipoMotor VARCHAR(20) NOT NULL,
    potencia DECIMAL(5,2) NOT NULL,
    localidade VARCHAR(100),
    nomeCategoria VARCHAR(50),
    CONSTRAINT PK_Veiculo PRIMARY KEY (matricula),
    CONSTRAINT FK_Veiculo_Parque FOREIGN KEY (localidade) REFERENCES Parque(localidade),
    CONSTRAINT FK_Veiculo_Tipo FOREIGN KEY (nomeCategoria) REFERENCES TipoVeiculo(nomeCategoria)
);

-- Tabela Aluguer
CREATE TABLE Aluguer (
    dataHoraInicio DATETIME NOT NULL,
    dataHoraFim DATETIME NOT NULL,
	duracao DECIMAL(6,2) NOT NULL,
    custoFinal DECIMAL(10,2) NOT NULL,
    avaliacao DECIMAL(3,2),
    comentario VARCHAR(150),
    numeroFiscal CHAR(9),
    numeroCartaConducao CHAR(12),
    codigo CHAR(6),
    matricula CHAR(8),
    CONSTRAINT PK_Aluguer PRIMARY KEY (matricula, dataHoraInicio),
    CONSTRAINT FK_Aluguer_Cliente FOREIGN KEY (numeroFiscal) REFERENCES Cliente(numeroFiscal),
    CONSTRAINT FK_Aluguer_Condutor FOREIGN KEY (numeroCartaConducao) REFERENCES Condutor(numeroCartaConducao),
    CONSTRAINT FK_Aluguer_Desconto FOREIGN KEY (codigo) REFERENCES Desconto(codigo),
    CONSTRAINT FK_Aluguer_Veiculo FOREIGN KEY (matricula) REFERENCES Veiculo(matricula),
    CONSTRAINT CHK_Avaliacao CHECK (avaliacao BETWEEN 0 AND 5)
);

-- Tabela Intervenção
CREATE TABLE Intervencao (
    quilometragem INTEGER NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    matricula CHAR(8),
    CONSTRAINT PK_Intervencao PRIMARY KEY (quilometragem, matricula),
    CONSTRAINT FK_Intervencao_Veiculo FOREIGN KEY (matricula) REFERENCES Veiculo(matricula)
);

CREATE TABLE Cliente_Desconto (
    numeroFiscal CHAR(9),
    codigo CHAR(6),
    CONSTRAINT PK_Cliente_Desconto PRIMARY KEY (numeroFiscal),
    CONSTRAINT FK_ClienteDesconto_Cliente FOREIGN KEY (numeroFiscal) REFERENCES Cliente(numeroFiscal),
    CONSTRAINT FK_ClienteDesconto_Desconto FOREIGN KEY (codigo) REFERENCES Desconto(codigo)
);

-- Tabela Parque_Tipo
CREATE TABLE Parque_Tipo (
    localidade VARCHAR(100),
    nomeCategoria VARCHAR(50),
    CONSTRAINT PK_Parque_Tipo PRIMARY KEY (localidade, nomeCategoria),
    CONSTRAINT FK_ParqueTipo_Parque FOREIGN KEY (localidade) REFERENCES Parque(localidade),
    CONSTRAINT FK_ParqueTipo_Tipo FOREIGN KEY (nomeCategoria) REFERENCES TipoVeiculo(nomeCategoria)
);

-- SHOW TABLES; -- Debug para ver as tabelas
-- DESCRIBE LugarParque; -- Debug para ver as colunas

/*-------------------
		VISTAS
-------------------*/

-- Vista para clientes, mostra se são Pessoa ou Empresa
CREATE VIEW vw_Clientes AS
SELECT 
    C.numeroFiscal,
    C.nome,
    C.contacto,
    C.morada,
    C.distrito,
    C.concelho,
    C.freguesia,
    C.preferenciasLinguisticasECulturais,
    C.moeda,
    CASE 
        WHEN P.numeroFiscal IS NOT NULL THEN 'Pessoa'
        WHEN E.numeroFiscal IS NOT NULL THEN 'Empresa'
        ELSE 'Desconhecido'
    END AS tipoCliente,
    E.capitalSocial
FROM Cliente C
LEFT JOIN Pessoa P ON C.numeroFiscal = P.numeroFiscal
LEFT JOIN Empresa E ON C.numeroFiscal = E.numeroFiscal;

-- Consulta
SELECT * FROM vw_Clientes;

-- Vista para informações de alugueres com os dados dos clientes e veículos.
CREATE VIEW vw_AlugueresDetalhados AS
SELECT 
    A.dataHoraInicio,
    A.dataHoraFim,
    TIMESTAMPDIFF(HOUR, A.dataHoraInicio, A.dataHoraFim) AS duracaoHoras,
    A.custoFinal,
    A.avaliacao,
    A.comentario,
    C.nome AS cliente,
    C.numeroFiscal,
    V.matricula,
    V.marca,
    V.modelo,
    V.tipo AS tipoVeiculo,
    D.codigo AS descontoCodigo,
    D.valor AS descontoValor
FROM Aluguer A
JOIN Cliente C ON A.numeroFiscal = C.numeroFiscal
JOIN Veiculo V ON A.matricula = V.matricula
LEFT JOIN Desconto D ON A.codigo = D.codigo;

-- Consulta
SELECT * FROM vw_AlugueresDetalhados WHERE cliente = 'Maria Costa';

-- Vista para ver os veículos disponíveis por tipo
CREATE VIEW vw_VeiculosPorTipo AS
SELECT 
    V.tipo AS tipoVeiculo,
    COUNT(*) AS quantidade,
    AVG(V.potencia) AS potenciaMedia,
    AVG(V.capacidadeCarga) AS capacidadeMedia
FROM Veiculo V
GROUP BY V.tipo;

-- Consulta
SELECT * FROM vw_VeiculosPorTipo;

-- Vista para ver os descontos ativos atualmente
CREATE VIEW vw_DescontosAtivos AS
SELECT 
    D.codigo,
    D.descricao,
    D.valor,
    D.dataHoraInicio,
    D.dataHoraFim
FROM Desconto D
WHERE NOW() BETWEEN D.dataHoraInicio AND D.dataHoraFim;

-- consulta
SELECT * FROM vw_DescontosAtivos;

-- Vista que mostra a que parque está associados cada veiculo
CREATE VIEW vw_VeiculosParque AS
SELECT 
    V.matricula AS veiculoMatricula,
    V.marca,
    V.modelo,
    V.tipo AS tipoVeiculo,
    V.cor,
    P.localidade AS parqueLocalidade,
    P.morada AS parqueMorada,
    P.latitude,
    P.longitude
FROM Veiculo V
JOIN Parque P ON V.localidade = P.localidade;

-- Consulta
SELECT * FROM vw_VeiculosParque;

-- Vista para a Reputação do Condutor, tendo em conta a média das avaliações
CREATE VIEW vw_ReputacaoCondutor AS
SELECT 
    C.numeroCartaConducao,
    C.nome,
    COALESCE(AVG(A.avaliacao), 0) AS reputacao
FROM Condutor C
LEFT JOIN Aluguer A ON C.numeroCartaConducao = A.numeroCartaConducao
GROUP BY C.numeroCartaConducao;

-- Consulta
SELECT * FROM vw_ReputacaoCondutor;
