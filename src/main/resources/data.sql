-- Inserção de faculdades fictícias
INSERT INTO faculties (
    id, name, mec_code, cnpj, dean_name, partnership_responsible, contact_phone, city, state
) VALUES
(1, 'Faculdade de Educação', '123456', '12.345.678/0001-90', 'Dr. João Silva', 'Ana Pereira', '(85) 98888-1111', 'Fortaleza', 'Ceará'),
(2, 'Faculdade de Ciências Humanas', '654321', '98.765.432/0001-09', 'Dra. Maria Lima', 'Carlos Silva', '(85) 97777-2222', 'Caucaia', 'Ceará');

-- Inserção dos cursos oferecidos pelas faculdades
INSERT INTO faculty_courses (faculty_id, course) VALUES
(1, 'História'),
(1, 'Geografia'),
(1, 'Português'),
(2, 'Sociologia'),
(2, 'Filosofia'),
(2, 'Pedagogia');
