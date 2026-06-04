DROP TABLE IF EXISTS favorite_proprietati;
DROP TABLE IF EXISTS liste_favorite;
DROP TABLE IF EXISTS anunturi;
DROP TABLE IF EXISTS vizionari;
DROP TABLE IF EXISTS contracte;
DROP TABLE IF EXISTS terenuri;
DROP TABLE IF EXISTS case_;
DROP TABLE IF EXISTS apartamente;
DROP TABLE IF EXISTS proprietati;
DROP TABLE IF EXISTS agenti_imobiliari;
DROP TABLE IF EXISTS clienti;
DROP TABLE IF EXISTS agentii_orase_active;
DROP TABLE IF EXISTS agentii_imobiliare;

CREATE TABLE agentii_imobiliare (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    adresa VARCHAR(255) NOT NULL,
    telefon VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    an_deschidere INT NOT NULL
);

CREATE TABLE agentii_orase_active (
    agentie_id INT NOT NULL,
    oras VARCHAR(100) NOT NULL,
    PRIMARY KEY (agentie_id, oras),
    FOREIGN KEY (agentie_id) REFERENCES agentii_imobiliare(id)
);

CREATE TABLE clienti (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    telefon VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    buget DECIMAL(12, 2) NOT NULL,
    tip_client VARCHAR(50) NOT NULL
);

CREATE TABLE agenti_imobiliari (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    telefon VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    specializare VARCHAR(100) NOT NULL,
    ani_experienta INT NOT NULL,
    agentie_id INT NOT NULL,
    FOREIGN KEY (agentie_id) REFERENCES agentii_imobiliare(id)
);

CREATE TABLE proprietati (
    id INT PRIMARY KEY,
    cod_proprietate VARCHAR(30) NOT NULL UNIQUE,
    titlu VARCHAR(150) NOT NULL,
    oras VARCHAR(100) NOT NULL,
    adresa VARCHAR(255) NOT NULL,
    pret DECIMAL(12, 2) NOT NULL,
    tip_tranzactie VARCHAR(30) NOT NULL,
    suprafata DECIMAL(10, 2) NOT NULL,
    disponibila BOOLEAN NOT NULL,
    tip_proprietate VARCHAR(30) NOT NULL,
    agentie_id INT NOT NULL,
    FOREIGN KEY (agentie_id) REFERENCES agentii_imobiliare(id)
);

CREATE TABLE apartamente (
    proprietate_id INT PRIMARY KEY,
    numar_camere INT NOT NULL,
    etaj INT NOT NULL,
    numar_bai INT NOT NULL,
    balcon BOOLEAN NOT NULL,
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);

CREATE TABLE case_ (
    proprietate_id INT PRIMARY KEY,
    numar_camere INT NOT NULL,
    numar_etaje INT NOT NULL,
    suprafata_curte DECIMAL(10, 2) NOT NULL,
    garaj BOOLEAN NOT NULL,
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);

CREATE TABLE terenuri (
    proprietate_id INT PRIMARY KEY,
    intravilan BOOLEAN NOT NULL,
    deschidere DECIMAL(10, 2) NOT NULL,
    utilitati BOOLEAN NOT NULL,
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);

CREATE TABLE contracte (
    id INT PRIMARY KEY,
    numar_contract VARCHAR(50) NOT NULL UNIQUE,
    tip_contract VARCHAR(50) NOT NULL,
    data_semnare DATE NOT NULL,
    valoare DECIMAL(12, 2) NOT NULL,
    client_id INT NOT NULL,
    agent_id INT NOT NULL,
    proprietate_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clienti(id),
    FOREIGN KEY (agent_id) REFERENCES agenti_imobiliari(id),
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);

CREATE TABLE vizionari (
    id INT PRIMARY KEY,
    data_vizionare DATE NOT NULL,
    ora_vizionare TIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    client_id INT NOT NULL,
    agent_id INT NOT NULL,
    proprietate_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clienti(id),
    FOREIGN KEY (agent_id) REFERENCES agenti_imobiliari(id),
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);

CREATE TABLE anunturi (
    id INT PRIMARY KEY,
    titlu VARCHAR(150) NOT NULL,
    descriere TEXT NOT NULL,
    data_publicare DATE NOT NULL,
    activ BOOLEAN NOT NULL,
    proprietate_id INT NOT NULL,
    agent_id INT NOT NULL,
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id),
    FOREIGN KEY (agent_id) REFERENCES agenti_imobiliari(id)
);

CREATE TABLE liste_favorite (
    id INT PRIMARY KEY,
    client_id INT NOT NULL UNIQUE,
    FOREIGN KEY (client_id) REFERENCES clienti(id)
);

CREATE TABLE favorite_proprietati (
    lista_favorite_id INT NOT NULL,
    proprietate_id INT NOT NULL,
    PRIMARY KEY (lista_favorite_id, proprietate_id),
    FOREIGN KEY (lista_favorite_id) REFERENCES liste_favorite(id),
    FOREIGN KEY (proprietate_id) REFERENCES proprietati(id)
);
