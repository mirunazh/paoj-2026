# Real Estate Marketplace

## 10 actiuni / interogari posibile
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

## 8 tipuri de obiecte din domeniu
1. AgentieImobiliara
2. AgentImobiliar
3. Client
4. Proprietate
5. Apartament
6. Casa
7. Teren
8. Anunt
9. Vizionare
10. Contract
11. ListaFavorite
12. CodProprietate

## Observatii OOP
- Ierarhie de mostenire: `Persoana -> Angajat -> AgentImobiliar` si `Proprietate -> Apartament/Casa/Teren`.
- Clasa abstracta: `Persoana`.
- Clasa imutabila: `CodProprietate`.
- Exceptii custom: `EntitateNegasitaException`, `ProprietateIndisponibilaException`.

## Colectii folosite
- `List` pentru colectii de obiecte.
- `Map` pentru indexare dupa ID sau grupare.
- `TreeSet` pentru proprietati sortate.
- `Set` pentru proprietatile din listele de favorite.

## Servicii
- `AgentieService`
- `AgentImobiliarService`
- `ClientService`
- `ProprietateService`
- `ApartamentService`
- `CasaService`
- `TerenService`
- `AnuntService`
- `VizionareService`
- `ContractService`
- `ListaFavoriteService`

Toate serviciile sunt implementate ca Singleton si sunt accesibile din `Main`.

## Etapa II
- Persistenta JDBC prin `DatabaseConnection` si repository-uri concrete.
- Tranzactie JDBC explicita in `ContractRepository.saveWithTransaction(...)`.
- Interogari `JOIN` in `RaportRepository`.
- Audit thread-safe in `AuditService`, cu scriere append in `audit.csv`.


# Real Estate Marketplace

##### ETAPA 1
/// Fiecare dintre aceste interogari apeleaza o functie din Service-ul aferent. Aceste interogari sunt pastrate numai in memorie, nu au efect asupra bazei de date.
1. Interogari:
=== AGENTII === (din AgentieService.java)
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String nume))
- Cauta dupa oras (getByOras(String oras))
- Cauta dupa nume si oras (getByNumeSiOras(String nume, String oras))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== AGENTI IMOBILIARI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String nume))
- Cauta dupa specializare (getBySpecializare(String specializare))
- Cauta dupa nume si specializare (getByNumeSiSpecializare(String nume, String specializare))
- Afiseaza agentii unei agentii (getByAgentie(int idAgentie))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== CLIENTI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String nume))
- Cauta dupa email (getByEmail(String email))
- Cauta dupa nume si buget maxim (getByNumeSiBugetMaxim(String nume, double bugetMaxim))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== PROPRIETATI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String titlu))
- Filtrare dupa oras (getByOras(String oras))
- Filtrare dupa buget maxim (getByBugetMaxim(double bugetMaxim)) - intoarce toate proprietatile cu pret <= decat suma
- Filtrare dupa tip tranzactie (getByTipTranzactie(String tipTranzactie))
- Filtrare multi-criteriu (getByOrasBugetSiTranzactie(String oras, double bugetMaxim, String tipTranzactie)) - proprietatile trb sa respecte oras, buget maxim si tip tranzactie
- Afiseaza proprietatile unei agentii (getByAgentie(int idAgentie)) 
- Afiseaza pretul mediu (calculeazaPretMediu()) - pretul mediu al tuturor proprietatilor
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== APARTAMENTE ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String titlu))
- Filtrare dupa camere (getByCamere(int camere)) - intoarce apartamentele cu exact numarul de camere cerut
- Filtrare dupa etaj (getByEtaj(int etaj)) - afiseaza apartamentele de la etajul cerut
- Filtrare multi-criteriu (getByOrasCamereEtaj(String oras, int camere, int etaj)) - apartamentele trb sa respecte oras, numar camere si etaj
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== CASE ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String titlu))
- Filtrare dupa suprafata curte (getBySuprafataCurte(double suprafataMinima))
- Filtrare dupa etaje (getByEtaje(int numarEtaje))
- Filtrare multi-criteriu (getByOrasCurteSiEtaje(String oras, double suprafataCurte, int numarEtaje))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== ADMIN TERENURI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String titlu))
- Filtrare intravilan/extravilan (getByIntravilan(boolean intravilan))
- Filtrare dupa suprafata (getBySuprafata(double suprafataMinima))
- Filtrare multi-criteriu (getByOrasTipSiSuprafata(String oras, boolean intravilan, double suprafataMinima))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== ANUNTURI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume (getByNume(String titlu))
- Afiseaza anunturi active (getActive())
- Cauta dupa agent si status (getByAgentSiStatus(int idAgent, boolean activ))
- Activeaza anunt (seteazaStatus(int id, boolean activ))
- Dezactiveaza anunt (seteazaStatus(int id, boolean activ))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== VIZIONARI ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa status (getByStatus(String status))
- Afiseaza vizionari client (getByClient(int idClient))
- Afiseaza vizionari agent (getByAgent(int idAgent))
- Cauta dupa status si agent (getByStatusSiAgent(String status, int idAgent))
- Schimba status (schimbaStatus(int id, String statusNou))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== CONTRACTE ===
- Adauga (add())
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa tip (getByNume(String tipContract))
- Afiseaza contracte client (getByClient(int idClient))
- Afiseaza contracte agent (getByAgent(int idAgent))
- Cauta dupa tip si agent (getByTipSiAgent(String tipContract, int idAgent))
- Afiseaza suma totala contracte (calculeazaSumaTotala())
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

=== ADMIN LISTE FAVORITE ===
- Adauga lista (add(int idClient))
- Afiseaza toate (listAll())
- Cauta dupa ID (afiseazaById(...))
- Cauta dupa nume client (getByNume(String numeClient))
- Cauta dupa client (getByClient(int idClient))
- Cauta dupa client si proprietate (getByClientSiProprietate(int idClient, int idProprietate))
- Adauga proprietate in lista (adaugaProprietate(int idLista, int idProprietate))
- Elimina proprietate din lista (eliminaProprietate(int idLista, int idProprietate))
- Verifica existenta proprietate (verificaExistenta(int idLista, int idProprietate))
- Update dupa ID (updateById(...))
- Sterge dupa ID (deleteById(...))

2. 
