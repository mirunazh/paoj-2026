package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.model.Proprietate;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProprietateRepository implements Repository<Proprietate, Integer> {
    private static final String INSERT_SQL =
            "INSERT INTO proprietati (id, cod_proprietate, titlu, oras, adresa, pret, tip_tranzactie, suprafata, disponibila, tip_proprietate, agentie_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, cod_proprietate, titlu, oras, adresa, pret, tip_tranzactie, suprafata, disponibila, tip_proprietate, agentie_id FROM proprietati WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, cod_proprietate, titlu, oras, adresa, pret, tip_tranzactie, suprafata, disponibila, tip_proprietate, agentie_id FROM proprietati";
    private static final String UPDATE_SQL =
            "UPDATE proprietati SET cod_proprietate = ?, titlu = ?, oras = ?, adresa = ?, pret = ?, tip_tranzactie = ?, suprafata = ?, disponibila = ?, tip_proprietate = ?, agentie_id = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM proprietati WHERE id = ?";

    private final Connection connection;
    private final AgentieRepository agentieRepository;

    public ProprietateRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.agentieRepository = new AgentieRepository();
    }

    @Override
    public void save(Proprietate entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getCodProprietate().getValoare());
            statement.setString(3, entity.getTitlu());
            statement.setString(4, entity.getOras());
            statement.setString(5, entity.getAdresa());
            statement.setDouble(6, entity.getPret());
            statement.setString(7, entity.getTipTranzactie());
            statement.setDouble(8, entity.getSuprafata());
            statement.setBoolean(9, entity.isDisponibila());
            statement.setString(10, determineTipProprietate(entity));
            statement.setInt(11, entity.getAgentieImobiliara().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea proprietatii.", e);
        }
    }

    @Override
    public Optional<Proprietate> findById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProprietate(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea proprietatii dupa id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Proprietate> findAll() {
        List<Proprietate> proprietati = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                proprietati.add(mapProprietate(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea proprietatilor.", e);
        }

        return proprietati;
    }

    @Override
    public void update(Proprietate entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getCodProprietate().getValoare());
            statement.setString(2, entity.getTitlu());
            statement.setString(3, entity.getOras());
            statement.setString(4, entity.getAdresa());
            statement.setDouble(5, entity.getPret());
            statement.setString(6, entity.getTipTranzactie());
            statement.setDouble(7, entity.getSuprafata());
            statement.setBoolean(8, entity.isDisponibila());
            statement.setString(9, determineTipProprietate(entity));
            statement.setInt(10, entity.getAgentieImobiliara().getId());
            statement.setInt(11, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea proprietatii.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea proprietatii.", e);
        }
    }

    private Proprietate mapProprietate(ResultSet resultSet) throws SQLException {
        int agentieId = resultSet.getInt("agentie_id");
        AgentieImobiliara agentie = agentieRepository.findById(agentieId)
                .orElseThrow(() -> new RuntimeException("Agentia cu id " + agentieId + " nu a fost gasita."));

        return new Proprietate(
                resultSet.getInt("id"),
                resultSet.getString("titlu"),
                resultSet.getString("oras"),
                resultSet.getString("adresa"),
                resultSet.getDouble("pret"),
                resultSet.getString("tip_tranzactie"),
                resultSet.getDouble("suprafata"),
                resultSet.getBoolean("disponibila"),
                agentie
        );
    }

    private String determineTipProprietate(Proprietate proprietate) {
        return proprietate.getClass().getSimpleName().toLowerCase();
    }
}
