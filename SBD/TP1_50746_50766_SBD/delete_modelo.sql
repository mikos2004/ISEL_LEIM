-- ISEL 24/25 - SBD
-- Miguel Alcobia - A50746
-- Tomás Salvador - A50766

USE SBD_A50746_A50766;

-- Para verificar todas as FK para criar os seus comandos de DROP
/*
SELECT 
    TABLE_NAME, 
    CONSTRAINT_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'sbd_a50746_a50766' 
    AND REFERENCED_TABLE_NAME IS NOT NULL;
*/

-- Eliminar Chaves Estrangeiras
ALTER TABLE Aluguer DROP FOREIGN KEY FK_Aluguer_Cliente;
ALTER TABLE Aluguer DROP FOREIGN KEY FK_Aluguer_Condutor;
ALTER TABLE Aluguer DROP FOREIGN KEY FK_Aluguer_Desconto;
ALTER TABLE Aluguer DROP FOREIGN KEY FK_Aluguer_Veiculo;
ALTER TABLE Cliente_Desconto DROP FOREIGN KEY FK_ClienteDesconto_Cliente;
ALTER TABLE Cliente_Desconto DROP FOREIGN KEY FK_ClienteDesconto_Desconto;
ALTER TABLE Intervencao DROP FOREIGN KEY FK_Intervencao_Veiculo;
ALTER TABLE LugarParque DROP FOREIGN KEY FK_LugarParque_Parque;
ALTER TABLE Parque_Tipo DROP FOREIGN KEY FK_ParqueTipo_Parque;
ALTER TABLE Parque_Tipo DROP FOREIGN KEY FK_ParqueTipo_Tipo;
ALTER TABLE Veiculo DROP FOREIGN KEY FK_Veiculo_Parque;
ALTER TABLE Veiculo DROP FOREIGN KEY FK_Veiculo_Tipo;
ALTER TABLE Empresa DROP FOREIGN KEY FK_Empresa_Cliente;
ALTER TABLE Pessoa DROP FOREIGN KEY FK_Pessoa_Cliente;

-- Eliminar as Tabelas
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS Condutor;
DROP TABLE IF EXISTS Parque;
DROP TABLE IF EXISTS LugarParque;
DROP TABLE IF EXISTS Veiculo;
DROP TABLE IF EXISTS TipoVeiculo;
DROP TABLE IF EXISTS Aluguer;
DROP TABLE IF EXISTS Intervencao;
DROP TABLE IF EXISTS Desconto;
DROP TABLE IF EXISTS Cliente_Desconto;
DROP TABLE IF EXISTS Parque_Tipo;

-- Eliminar a Base de Dados
DROP DATABASE IF EXISTS sbd_a50746_a50766;
