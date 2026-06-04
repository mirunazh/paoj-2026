# Real Estate Marketplace

Proiectul modeleaza o platforma imobiliara in pachetul `com.pao.proiect.imobiliare`. Etapa I ramane implementata in memorie prin servicii Singleton, iar Etapa II adauga persistenta JDBC, tranzactii si audit CSV fara sa inlocuiasca logica OOP existenta.

## Actiuni demonstrate in sistem
1. Adaugarea unei agentii imobiliare noi.
2. Inregistrarea unui agent imobiliar nou.
3. Inregistrarea unui client nou.
4. Adaugarea unei proprietati noi in sistem.
5. Adaugarea unui anunt pentru o proprietate.
6. Programarea unei vizionari pentru un client.
7. Schimbarea statusului unei vizionari.
8. Incheierea unui contract pentru o proprietate disponibila.
9. Adaugarea unei proprietati in lista de favorite a unui client.
10. Cautarea proprietatilor dupa oras, buget si tip de tranzactie.

Aceste 10 actiuni sunt apelate direct din `Main`, iar fiecare trece prin `AuditService`.

## Tipuri de obiecte din domeniu
1. `AgentieImobiliara`
2. `AgentImobiliar`
3. `Client`
4. `Proprietate`
5. `Apartament`
6. `Casa`
7. `Teren`
8. `Anunt`
9. `Vizionare`
10. `Contract`
11. `ListaFavorite`
12. `CodProprietate`

## Cerinte Etapa I acoperite
- Sunt definite mai mult de 8 clase de domeniu, cu atribute private/protected si metode specifice.
- Exista ierarhiile `Persoana -> Angajat -> AgentImobiliar` si `Proprietate -> Apartament/Casa/Teren`.
- `Persoana` este clasa abstracta folosita in ierarhie.
- `CodProprietate` este clasa imutabila.
- Exista exceptiile custom `EntitateNegasitaException` si `ProprietateIndisponibilaException`.
- Sunt folosite `List`, `Map`, `Set` si `TreeSet`, inclusiv colectii sortate si map-uri de indexare.
- Serviciile Singleton ofera operatii de adaugare, stergere, cautare si listare in memorie.

## Cerinte Etapa II adaugate
- `schema.sql` este disponibil in radacina proiectului si in `resources/`.
- `db.properties` este disponibil in `resources/`, iar `DatabaseConnection` il incarca fara a hardcoda credentiale.
- Interfata generica `Repository<T, ID>` este implementata pentru cel putin 5 entitati:
  `AgentieImobiliara`, `AgentImobiliar`, `Client`, `Proprietate`, `Contract`.
- Toate interogarile SQL folosesc `PreparedStatement` si `try-with-resources`.
- `ContractRepository.saveWithTransaction(...)` executa explicit o tranzactie JDBC cu `commit` si `rollback`.
- `RaportRepository` contine interogari `JOIN` pentru rapoarte agregate si detaliate.
- `AuditService` scrie thread-safe in `audit.csv`, in modul append.

## Structura proiectului

```text
src/
└── com/pao/proiect/imobiliare/
    ├── Main.java
    ├── model/
    ├── service/
    ├── repository/
    ├── exception/
    └── util/

resources/
├── db.properties
└── schema.sql

schema.sql
audit.csv
```

## Observatii de rulare
- Aplicatia compileaza cu `javac` pe pachetul proiectului.
- Demo-ul din `Main` ruleaza mai intai scenariile din Etapa I, apoi incearca demo-ul JDBC din Etapa II.
- Daca baza de date sau driverul JDBC nu sunt disponibile, aplicatia continua si afiseaza un mesaj clar pentru partea de JDBC.
