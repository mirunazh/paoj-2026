package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.model.Client;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, Integer> {
    private static final String INSERT_SQL =
            "INSERT INTO clienti (id, nume, telefon, email, buget, tip_client) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nume, telefon, email, buget, tip_client FROM clienti WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, nume, telefon, email, buget, tip_client FROM clienti";
    private static final String UPDATE_SQL =
            "UPDATE clienti SET nume = ?, telefon = ?, email = ?, buget = ?, tip_client = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM clienti WHERE id = ?";

    private final Connection connection;

    public ClientRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Client entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getTelefon());
            statement.setString(4, entity.getEmail());
            statement.setDouble(5, entity.getBuget());
            statement.setString(6, entity.getTipClient());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului.", e);
        }
    }

    @Override
    public Optional<Client> findById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapClient(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului dupa id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clienti = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clienti.add(mapClient(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea clientilor.", e);
        }

        return clienti;
    }

    @Override
    public void update(Client entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getTelefon());
            statement.setString(3, entity.getEmail());
            statement.setDouble(4, entity.getBuget());
            statement.setString(5, entity.getTipClient());
            statement.setInt(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului.", e);
        }
    }

    private Client mapClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getString("telefon"),
                resultSet.getString("email"),
                resultSet.getDouble("buget"),
                resultSet.getString("tip_client")
        );
    }
}
