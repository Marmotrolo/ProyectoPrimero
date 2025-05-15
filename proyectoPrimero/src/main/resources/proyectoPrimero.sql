-- Tabla principal de participantes
CREATE TABLE Participantes (
  Id_participante INT AUTO_INCREMENT PRIMARY KEY,
  Nombre VARCHAR(100) NOT NULL,
  Curso VARCHAR(10) NOT NULL CHECK (Curso IN ('1ESO', '2ESO')),
  Tipo ENUM('INDIVIDUAL', 'GRUPO') NOT NULL
);

-- Tabla para grupos (extiende Participantes)
CREATE TABLE Grupos (
  Id_participante INT PRIMARY KEY,
  Nombre_grupo VARCHAR(100) NOT NULL,
  FOREIGN KEY (Id_participante) REFERENCES Participantes(Id_participante)
  ON DELETE CASCADE
);

-- Tabla para miembros de grupos (relación muchos a muchos)
CREATE TABLE MiembrosGrupo (
  Id_grupo INT NOT NULL,
  Id_participante INT NOT NULL,
  PRIMARY KEY (Id_grupo, Id_participante),
  FOREIGN KEY (Id_grupo) REFERENCES Grupos(Id_participante) ON DELETE CASCADE,
  FOREIGN KEY (Id_participante) REFERENCES Participantes(Id_participante) ON DELETE CASCADE
);

-- Usuario de prueba
INSERT INTO usuarios (usuario, clave, direccion) VALUES ('pepep', 'pepe1234', 'Calle Proyecto')
ON DUPLICATE KEY UPDATE clave = 'pepe1234';

INSERT INTO usuarios (usuario, clave, direccion) VALUES ('pepa', 'pepe1234', 'Calle Proyecto 2')
ON DUPLICATE KEY UPDATE clave = 'pepe1234';
