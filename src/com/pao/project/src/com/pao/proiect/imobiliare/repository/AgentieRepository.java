package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgentieRepository implements Repository<AgentieImobiliara, Integer> {
    private static final String INSERT_AGENTIE_SQL =
            "INSERT INTO agentii_imobiliare (id, nume, adresa, telefon, email, an_deschidere) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_ORAS_SQL =
            "INSERT INTO agentii_orase_active (agentie_id, oras) VALUES (?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nume, adresa, telefon, email, an_deschidere FROM agentii_imobiliare WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, nume, adresa, telefon, email, an_deschidere FROM agentii_imobiliare";
    private static final String SELECT_ORASE_SQL =
            "SELECT oras FROM agentii_orase_active WHERE agentie_id = ? ORDER BY oras";
    private static final String UPDATE_AGENTIE_SQL =
            "UPDATE agentii_imobiliare SET nume = ?, adresa = ?, telefon = ?, email = ?, an_deschidere = ? WHERE id = ?";
    private static final String DELETE_ORASE_SQL =
            "DELETE FROM agentii_orase_active WHERE agentie_id = ?";
    private static final String DELETE_AGENTIE_SQL =
            "DELETE FROM agentii_imobiliare WHERE id = ?";

    private final Connection connection;

    public AgentieRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(AgentieImobiliara entity) {
        try (PreparedStatement agentieStatement = connection.prepareStatement(INSERT_AGENTIE_SQL)) {
            agentieStatement.setInt(1, entity.getId());
            agentieStatement.setString(2, entity.getNume());
            agentieStatement.setString(3, entity.getAdresa());
            agentieStatement.setString(4, entity.getTelefon());
            agentieStatement.setString(5, entity.getEmail());
            agentieStatement.setInt(6, entity.getAnDeschidere());
            agentieStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea agentiei.", e);
        }

        saveOraseActive(entity.getId(), entity.getOraseActive());
    }

    @Override
    public Optional<AgentieImobiliara> findById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapAgentie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea agentiei dupa id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<AgentieImobiliara> findAll() {
        List<AgentieImobiliara> agentii = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                agentii.add(mapAgentie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea agentiilor.", e);
        }

        return agentii;
    }

    @Override
    public void update(AgentieImobiliara entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_AGENTIE_SQL)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getAdresa());
            statement.setString(3, entity.getTelefon());
            statement.setString(4, entity.getEmail());
            statement.setInt(5, entity.getAnDeschidere());
            statement.setInt(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea agentiei.", e);
        }

        deleteOraseActive(entity.getId());
        saveOraseActive(entity.getId(), entity.getOraseActive());
    }

    @Override
    public void delete(Integer id) {
        deleteOraseActive(id);

        try (PreparedStatement statement = connection.prepareStatement(DELETE_AGENTIE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea agentiei.", e);
        }
    }

    private AgentieImobiliara mapAgentie(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        return new AgentieImobiliara(
                id,
                resultSet.getString("nume"),
                resultSet.getString("adresa"),
                resultSet.getString("telefon"),
                resultSet.getString("email"),
                resultSet.getInt("an_deschidere"),
                loadOraseActive(id)
        );
    }

    private String[] loadOraseActive(int agentieId) {
        List<String> orase = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ORASE_SQL)) {
            statement.setInt(1, agentieId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orase.add(resultSet.getString("oras"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea oraselor active.", e);
        }

        return orase.toArray(new String[0]);
    }

    private void saveOraseActive(int agentieId, String[] oraseActive) {
        if (oraseActive == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_ORAS_SQL)) {
            for (String oras : oraseActive) {
                statement.setInt(1, agentieId);
                statement.setString(2, oras);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea oraselor active.", e);
        }
    }

    private void deleteOraseActive(int agentieId) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ORASE_SQL)) {
            statement.setInt(1, agentieId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea oraselor active.", e);
        }
    }
}
