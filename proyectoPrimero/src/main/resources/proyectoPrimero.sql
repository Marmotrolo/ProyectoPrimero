create database competicion_deportiva;
use competicion_deportiva;

CREATE TABLE Participantes (
  Id_participante INT PRIMARY KEY,
  Nombre VARCHAR(100),
  Curso VARCHAR(100)
);

CREATE TABLE ParticipanteIndividual (
  Id_participante INT PRIMARY KEY,
  FOREIGN KEY (Id_participante) REFERENCES Participantes(Id_participante)
);

CREATE TABLE Grupo (
  Id_participante INT PRIMARY KEY,
  Nombre_Grupo VARCHAR(100),
  FOREIGN KEY (Id_participante) REFERENCES Participantes(Id_participante)
);


CREATE TABLE ParticipanteGrupo (
  Id_Participante INT,
  Id_Grupo INT,
  PRIMARY KEY (Id_Participante, Id_Grupo),
  FOREIGN KEY (Id_Participante) REFERENCES Participantes(Id_participante),
  FOREIGN KEY (Id_Grupo) REFERENCES Grupo(Id_participante)
);

-- Usuario de prueba
INSERT INTO usuarios (usuario, clave, direccion) VALUES ('pepep', 'pepe1234', 'Calle Proyecto')
ON DUPLICATE KEY UPDATE clave = 'pepe1234';

INSERT INTO usuarios (usuario, clave, direccion) VALUES ('pepa', 'pepe1234', 'Calle Proyecto 2')
ON DUPLICATE KEY UPDATE clave = 'pepe1234';
