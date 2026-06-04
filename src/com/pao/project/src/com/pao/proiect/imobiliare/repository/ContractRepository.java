package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.model.AgentImobiliar;
import com.pao.proiect.imobiliare.model.Client;
import com.pao.proiect.imobiliare.model.Contract;
import com.pao.proiect.imobiliare.model.Proprietate;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContractRepository implements Repository<Contract, Integer> {
    private static final String INSERT_SQL =
            "INSERT INTO contracte (id, numar_contract, tip_contract, data_semnare, valoare, client_id, agent_id, proprietate_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, numar_contract, tip_contract, data_semnare, valoare, client_id, agent_id, proprietate_id FROM contracte WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, numar_contract, tip_contract, data_semnare, valoare, client_id, agent_id, proprietate_id FROM contracte";
    private static final String UPDATE_SQL =
            "UPDATE contracte SET numar_contract = ?, tip_contract = ?, data_semnare = ?, valoare = ?, client_id = ?, agent_id = ?, proprietate_id = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM contracte WHERE id = ?";
    private static final String UPDATE_PROPRIETATE_DISPONIBILITATE_SQL =
            "UPDATE proprietati SET disponibila = ? WHERE id = ?";

    private final Connection connection;
    private final ClientRepository clientRepository;
    private final AgentImobiliarRepository agentRepository;
    private final ProprietateRepository proprietateRepository;

    public ContractRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.clientRepository = new ClientRepository();
        this.agentRepository = new AgentImobiliarRepository();
        this.proprietateRepository = new ProprietateRepository();
    }

    @Override
    public void save(Contract entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            populateStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea contractului.", e);
        }
    }

    public void saveWithTransaction(Contract entity) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement contractStatement = connection.prepareStatement(INSERT_SQL);
                 PreparedStatement proprietateStatement = connection.prepareStatement(UPDATE_PROPRIETATE_DISPONIBILITATE_SQL)) {
                populateStatement(contractStatement, entity);
                contractStatement.executeUpdate();

                proprietateStatement.setBoolean(1, false);
                proprietateStatement.setInt(2, entity.getProprietate().getId());
                proprietateStatement.executeUpdate();

                connection.commit();
                entity.getProprietate().setDisponibila(false);
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tranzactionala a contractului.", e);
        }
    }

    @Override
    public Optional<Contract> findById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapContract(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea contractului dupa id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Contract> findAll() {
        List<Contract> contracte = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                contracte.add(mapContract(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea contractelor.", e);
        }
        return contracte;
    }

    @Override
    public void update(Contract entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getNumarContract());
            statement.setString(2, entity.getTipContract());
            statement.setDate(3, Date.valueOf(entity.getDataSemnare()));
            statement.setDouble(4, entity.getValoare());
            statement.setInt(5, entity.getClient().getId());
            statement.setInt(6, entity.getAgentImobiliar().getId());
            statement.setInt(7, entity.getProprietate().getId());
            statement.setInt(8, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea contractului.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea contractului.", e);
        }
    }

    private void populateStatement(PreparedStatement statement, Contract entity) throws SQLException {
        statement.setInt(1, entity.getId());
        statement.setString(2, entity.getNumarContract());
        statement.setString(3, entity.getTipContract());
        statement.setDate(4, Date.valueOf(entity.getDataSemnare()));
        statement.setDouble(5, entity.getValoare());
        statement.setInt(6, entity.getClient().getId());
        statement.setInt(7, entity.getAgentImobiliar().getId());
        statement.setInt(8, entity.getProprietate().getId());
    }

    private Contract mapContract(ResultSet resultSet) throws SQLException {
        int clientId = resultSet.getInt("client_id");
        int agentId = resultSet.getInt("agent_id");
        int proprietateId = resultSet.getInt("proprietate_id");

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Clientul cu id " + clientId + " nu a fost gasit."));
        AgentImobiliar agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agentul cu id " + agentId + " nu a fost gasit."));
        Proprietate proprietate = proprietateRepository.findById(proprietateId)
                .orElseThrow(() -> new RuntimeException("Proprietatea cu id " + proprietateId + " nu a fost gasita."));

        return new Contract(
                resultSet.getInt("id"),
                resultSet.getString("numar_contract"),
                resultSet.getString("tip_contract"),
                resultSet.getDate("data_semnare").toString(),
                resultSet.getDouble("valoare"),
                client,
                agent,
                proprietate
        );
    }
}
