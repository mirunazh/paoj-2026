package com.pao.proiect.imobiliare.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() {
        Properties properties = new Properties();

        try (InputStream input = openPropertiesStream()) {
            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea fisierului db.properties.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date.", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private InputStream openPropertiesStream() throws IOException {
        InputStream classpathStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");
        if (classpathStream != null) {
            return classpathStream;
        }

        for (Path candidate : getDbPropertiesCandidates()) {
            if (Files.exists(candidate)) {
                return Files.newInputStream(candidate);
            }
        }

        throw new IOException("Fisierul db.properties nu a fost gasit nici in classpath, nici in proiect.");
    }

    private List<Path> getDbPropertiesCandidates() {
        return List.of(
                Path.of("resources", "db.properties"),
                Path.of("src", "resources", "db.properties"),
                Path.of("src", "com", "pao", "project", "resources", "db.properties"),
                Path.of("src", "com", "pao", "project", "src", "resources", "db.properties")
        );
    }
}
