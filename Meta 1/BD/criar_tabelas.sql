﻿

CREATE TABLE Utilizador
(
id 	SERIAL not null,
username VARCHAR(100) not null,
pass VARCHAR(100) not null,
saldo INTEGER not null,

CONSTRAINT Utilizador_pk PRIMARY KEY (id)
);

CREATE TABLE Niveis_Extra
(
 id SERIAL not null,
 descricao VARCHAR(100) not null,
 valor	INTEGER not null,
 id_projecto INTEGER not null,

 CONSTRAINT Niveis_Extra_pk PRIMARY KEY (id),
 FOREIGN KEY (id_projecto) REFERENCES Projecto (id)
);

CREATE TABLE Projecto
(
 id 	SERIAL not null,
 titulo VARCHAR(50) not null,
 descricao VARCHAR(100) not null,
 valorPretendido INTEGER not null,
 valorActual INTEGER not null,
 status BOOLEAN not null default true,
 data_limite DATE ,
 id_niveis_extra INTEGER,

-- FOREIGN KEY (id_niveis_extra) REFERENCES Niveis_Extra(id),
 CONSTRAINT Projecto_pk PRIMARY KEY(id)
);

CREATE TABLE Mensagem
(
 id 		SERIAL not null,
 id_user_envia	INTEGER not null,
 id_projecto	INTEGER not null,
 pergunta 	VARCHAR(50) not null,
 resposta	VARCHAR(100),

FOREIGN KEY (id_user_envia) REFERENCES Utilizador(id),
FOREIGN KEY (id_projecto) REFERENCES Projecto(id),
CONSTRAINT Mensagens_pk PRIMARY KEY (id)
);


CREATE TABLE Recompensas(
 id 	SERIAL not null,
 valor INTEGER not null,
 id_projecto INTEGER not null,
 titulo VARCHAR(100) not null,
 

 CONSTRAINT Recompensa_pk PRIMARY KEY(id),
 FOREIGN KEY (id_projecto) REFERENCES Projecto(id)
 );

CREATE TABLE Product_Type(
 id_projecto	INTEGER not null,
 contador	INTEGER not null,
 descricao	VARCHAR(100),

 FOREIGN KEY (id_projecto) REFERENCES Projecto(id)
);

CREATE TABLE Projecto_User(
 id_projecto	INTEGER not null,
 id_user	INTEGER not null,

 FOREIGN KEY (id_projecto) REFERENCES Projecto(id),
 FOREIGN KEY (id_user) REFERENCES Utilizador(id)
);

CREATE TABLE Recompensa_User(
 id_recompensa	INTEGER not null,
 id_user	INTEGER not null,

 FOREIGN KEY (id_recompensa) REFERENCES Recompensas(id),
 FOREIGN KEY (id_user) REFERENCES Utilizador(id)
);

CREATE TABLE Pledge_User
(
id_user	INTEGER not null,
id_projecto INTEGER not null,
valor INTEGER not null,

 FOREIGN KEY (id_user) REFERENCES Utilizador(id),
 FOREIGN KEY (id_projecto) REFERENCES Projecto(id)
);
