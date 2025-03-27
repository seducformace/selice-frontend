-- Criação da tabela principal de faculdades
CREATE TABLE faculties (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    mec_code VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(255) NOT NULL UNIQUE,
    dean_name VARCHAR(255) NOT NULL,
    partnership_responsible VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL
);

-- Criação da tabela de cursos oferecidos (relacionamento com faculties)
CREATE TABLE faculty_courses (
    faculty_id BIGINT NOT NULL,
    course VARCHAR(255),
    CONSTRAINT fk_faculty_courses FOREIGN KEY (faculty_id)
        REFERENCES faculties (id) ON DELETE CASCADE
);
