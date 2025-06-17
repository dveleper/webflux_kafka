CREATE TABLE IF NOT EXISTS personas (
    db_id INT AUTO_INCREMENT PRIMARY KEY,
    persona_id VARCHAR(255) NOT NULL,
    nombre VARCHAR(255),
    edad INT
); 