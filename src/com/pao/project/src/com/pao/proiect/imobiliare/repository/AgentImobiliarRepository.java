package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.model.AgentImobiliar;
import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgentImobiliarRepository implements Repository<AgentImobiliar, Integer> {
    private static final String INSERT_SQL =
            "INSERT INTO agenti_imobiliari (id, nume, telefon, email, specializare, ani_experienta, agentie_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nume, telefon, email, specializare, ani_experienta, agentie_id FROM agenti_imobiliari WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, nume, telefon, email, specializare, ani_experienta, agentie_id FROM agenti_imobiliari";
    private static final String UPDATE_SQL =
            "UPDATE agenti_imobiliari SET nume = ?, telefon = ?, email = ?, specializare = ?, ani_experienta = ?, agentie_id = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM agenti_imobiliari WHERE id = ?";

    private final Connection connection;
    private final AgentieRepository agentieRepository;

    public AgentImobiliarRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.agentieRepository = new AgentieRepository();
    }

    @Override
    public void save(AgentImobiliar entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getTelefon());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getSpecializare());
            statement.setInt(6, entity.getAniExperienta());
            statement.setInt(7, entity.getAgentieImobiliara().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea agentului imobiliar.", e);
        }
    }

    @Override
    public Optional<AgentImobiliar> findById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapAgent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea agentului imobiliar dupa id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<AgentImobiliar> findAll() {
        List<AgentImobiliar> agenti = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                agenti.add(mapAgent(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea agentilor imobiliari.", e);
        }

        return agenti;
    }

    @Override
    public void update(AgentImobiliar entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getTelefon());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getSpecializare());
            statement.setInt(5, entity.getAniExperienta());
            statement.setInt(6, entity.getAgentieImobiliara().getId());
            statement.setInt(7, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea agentului imobiliar.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea agentului imobiliar.", e);
        }
    }

    private AgentImobiliar mapAgent(ResultSet resultSet) throws SQLException {
        int agentieId = resultSet.getInt("agentie_id");
        AgentieImobiliara agentie = agentieRepository.findById(agentieId)
                .orElseThrow(() -> new RuntimeException("Agentia cu id " + agentieId + " nu a fost gasita."));

        return new AgentImobiliar(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getString("telefon"),
                resultSet.getString("email"),
                resultSet.getString("specializare"),
                resultSet.getInt("ani_experienta"),
                agentie
        );
    }
}
