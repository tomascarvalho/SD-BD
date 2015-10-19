
CREATE TABLE Utilizador
(
id 	SERIAL not null,
nome 	VARCHAR(10) not null,
apelido VARCHAR(10) not null,
username VARCHAR(10) not null,
pass VARCHAR(10) not null,
saldo INTEGER not null,

CONSTRAINT Utilizador_pk PRIMARY KEY (id));



CREATE TABLE Mensagem
(
 id 		SERIAL not null,
 pergunta 	VARCHAR(50) not null,
 resposta	VARCHAR(50) not null,
 username 	VARCHAR(10) not null, 
 
CONSTRAINT Mensagens_pk PRIMARY KEY (id));


CREATE TABLE Projecto
(
 id 	SERIAL not null,
 titulo VARCHAR(10) not null,
 descricao VARCHAR(10) not null,
 valorPretendido INTEGER not null,
 valorActual INTEGER not null,

 CONSTRAINT Projecto_pk PRIMARY KEY(id)
);


