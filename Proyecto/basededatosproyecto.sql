create database competicion_deportiva;
use competicion_deportiva;

CREATE TABLE Participantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    curso VARCHAR(10) NOT NULL);

-- Tabla Grupos (para formulario grupal)
CREATE TABLE Grupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) DEFAULT 'Equipo deportivo',
    curso VARCHAR(10) NOT NULL
);

-- Tabla Miembros (relación grupo-participante)
CREATE TABLE ParticipanteGrupo (
    grupo_id INT,
    participante_id INT,
    PRIMARY KEY (grupo_id, participante_id),
    FOREIGN KEY (grupo_id) REFERENCES Grupos(id),
    FOREIGN KEY (participante_id) REFERENCES Participantes(id)
);