
CREATE TABLE Utilizador
(
id 	SERIAL not null,
nome 	VARCHAR(10) not null,
apelido VARCHAR(10) not null,
username VARCHAR(10) not null,
pass VARCHAR(10) not null,
saldo INTEGER not null,

CONSTRAINT Utilizador_pk PRIMARY KEY (id)
);



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
 id_utilizador INTEGER not null,
 titulo VARCHAR(50) not null,
 descricao VARCHAR(100) not null,
 valorPretendido INTEGER not null,
 valorActual INTEGER not null,
 status BOOLEAN not null default true,

 CONSTRAINT Projecto_pk PRIMARY KEY(id),
 FOREIGN KEY (id_utilizador) REFERENCES utilizador(id)
);

CREATE TABLE Recompensas(
 id 	INTEGER not null,
 id_projecto INTEGER not null,
 titulo VARCHAR(50) not null,
 recompensa VARCHAR(100) not null,

 CONSTRAINT Recompensa_pk PRIMARY KEY(id),
 FOREIGN KEY (id_projecto) REFERENCES projecto(id)
 );



