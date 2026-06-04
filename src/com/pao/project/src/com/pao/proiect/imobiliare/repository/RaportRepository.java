package com.pao.proiect.imobiliare.repository;

import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaportRepository {
    private static final String CLIENTI_CU_VIZIONARI_ACTIVE_SQL =
            "SELECT c.id, c.nume, COUNT(v.id) AS numar_vizionari_active " +
                    "FROM clienti c " +
                    "LEFT JOIN vizionari v ON c.id = v.client_id AND v.status IN (?, ?) " +
                    "GROUP BY c.id, c.nume " +
                    "ORDER BY numar_vizionari_active DESC, c.nume ASC";

    private static final String PROPRIETATI_CELE_MAI_VIZIONATE_SQL =
            "SELECT p.id, p.titlu, ai.nume AS agentie, COUNT(v.id) AS numar_vizionari " +
                    "FROM proprietati p " +
                    "JOIN agentii_imobiliare ai ON p.agentie_id = ai.id " +
                    "LEFT JOIN vizionari v ON p.id = v.proprietate_id " +
                    "GROUP BY p.id, p.titlu, ai.nume " +
                    "ORDER BY numar_vizionari DESC, p.titlu ASC " +
                    "LIMIT 5";

    private static final String VIZIONARI_ACTIVE_CU_DETALII_SQL =
            "SELECT v.id, v.data_vizionare, v.ora_vizionare, v.status, " +
                    "c.nume AS client, ag.nume AS agent, p.titlu AS proprietate " +
                    "FROM vizionari v " +
                    "JOIN clienti c ON v.client_id = c.id " +
                    "JOIN agenti_imobiliari ag ON v.agent_id = ag.id " +
                    "JOIN proprietati p ON v.proprietate_id = p.id " +
                    "WHERE v.status IN (?, ?) " +
                    "ORDER BY v.data_vizionare ASC, v.ora_vizionare ASC";

    private static final String TOP_AGENTI_DUPA_CONTRACTE_SQL =
            "SELECT ag.id, ag.nume, ai.nume AS agentie, COUNT(c.id) AS numar_contracte, " +
                    "COALESCE(SUM(c.valoare), 0) AS valoare_totala " +
                    "FROM agenti_imobiliari ag " +
                    "JOIN agentii_imobiliare ai ON ag.agentie_id = ai.id " +
                    "LEFT JOIN contracte c ON ag.id = c.agent_id " +
                    "GROUP BY ag.id, ag.nume, ai.nume " +
                    "ORDER BY numar_contracte DESC, valoare_totala DESC, ag.nume ASC " +
                    "LIMIT 5";

    private static final String CONTRACTELE_UNUI_CLIENT_SQL =
            "SELECT c.id, c.numar_contract, c.tip_contract, c.data_semnare, c.valoare, " +
                    "cl.nume AS client, ag.nume AS agent, p.titlu AS proprietate " +
                    "FROM contracte c " +
                    "JOIN clienti cl ON c.client_id = cl.id " +
                    "JOIN agenti_imobiliari ag ON c.agent_id = ag.id " +
                    "JOIN proprietati p ON c.proprietate_id = p.id " +
                    "WHERE cl.id = ? " +
                    "ORDER BY c.data_semnare DESC, c.id DESC";

    private final Connection connection;

    public RaportRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<String> findClientiCuNumarulDeVizionariActive() {
        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(CLIENTI_CU_VIZIONARI_ACTIVE_SQL)) {
            statement.setString(1, "programata");
            statement.setString(2, "confirmata");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rezultate.add(
                            "Client ID: " + resultSet.getInt("id") +
                                    ", nume: " + resultSet.getString("nume") +
                                    ", vizionari active: " + resultSet.getInt("numar_vizionari_active")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la raportul clientilor cu vizionari active.", e);
        }

        return rezultate;
    }

    public List<String> findTop5ProprietatiCeleMaiVizionateCuAgentiaLor() {
        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(PROPRIETATI_CELE_MAI_VIZIONATE_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(
                        "Proprietate ID: " + resultSet.getInt("id") +
                                ", titlu: " + resultSet.getString("titlu") +
                                ", agentie: " + resultSet.getString("agentie") +
                                ", numar vizionari: " + resultSet.getInt("numar_vizionari")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la raportul proprietatilor cele mai vizionate.", e);
        }

        return rezultate;
    }

    public List<String> findVizionariActiveCuDetalii() {
        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(VIZIONARI_ACTIVE_CU_DETALII_SQL)) {
            statement.setString(1, "programata");
            statement.setString(2, "confirmata");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rezultate.add(
                            "Vizionare ID: " + resultSet.getInt("id") +
                                    ", data: " + resultSet.getString("data_vizionare") +
                                    ", ora: " + resultSet.getString("ora_vizionare") +
                                    ", status: " + resultSet.getString("status") +
                                    ", client: " + resultSet.getString("client") +
                                    ", agent: " + resultSet.getString("agent") +
                                    ", proprietate: " + resultSet.getString("proprietate")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la raportul vizionarilor active.", e);
        }

        return rezultate;
    }

    public List<String> findTop5AgentiDupaNumarulDeContracte() {
        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(TOP_AGENTI_DUPA_CONTRACTE_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(
                        "Agent ID: " + resultSet.getInt("id") +
                                ", nume: " + resultSet.getString("nume") +
                                ", agentie: " + resultSet.getString("agentie") +
                                ", contracte: " + resultSet.getInt("numar_contracte") +
                                ", valoare totala: " + resultSet.getDouble("valoare_totala")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la raportul top agenti dupa contracte.", e);
        }

        return rezultate;
    }

    public List<String> findContracteleUnuiClientCuDetalii(int clientId) {
        List<String> rezultate = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(CONTRACTELE_UNUI_CLIENT_SQL)) {
            statement.setInt(1, clientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rezultate.add(
                            "Contract ID: " + resultSet.getInt("id") +
                                    ", numar: " + resultSet.getString("numar_contract") +
                                    ", tip: " + resultSet.getString("tip_contract") +
                                    ", data: " + resultSet.getString("data_semnare") +
                                    ", valoare: " + resultSet.getDouble("valoare") +
                                    ", client: " + resultSet.getString("client") +
                                    ", agent: " + resultSet.getString("agent") +
                                    ", proprietate: " + resultSet.getString("proprietate")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la raportul contractelor unui client.", e);
        }

        return rezultate;
    }
}
