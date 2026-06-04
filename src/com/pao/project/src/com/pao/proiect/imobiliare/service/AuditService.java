package com.pao.proiect.imobiliare.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {
    private static final String HEADER = "nume_actiune,timestamp";
    private final Path auditPath = resolveAuditPath();
    private final Lock lock = new ReentrantLock();

    private AuditService() {
    }

    private static class Holder {
        private static final AuditService INSTANCE = new AuditService();
    }

    public static AuditService getInstance() {
        return Holder.INSTANCE;
    }

    public void logAction(String actionName) {
        lock.lock();
        try {
            Path parent = auditPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            boolean shouldWriteHeader = Files.notExists(auditPath) || Files.size(auditPath) == 0;
            try (BufferedWriter writer = Files.newBufferedWriter(
                    auditPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                if (shouldWriteHeader) {
                    writer.write(HEADER);
                    writer.newLine();
                }
                writer.write(actionName + "," + LocalDateTime.now());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea in audit.csv.", e);
        } finally {
            lock.unlock();
        }
    }

    private Path resolveAuditPath() {
        for (Path candidate : getAuditPathCandidates()) {
            Path parent = candidate.getParent();
            if (parent == null || Files.exists(parent)) {
                return candidate;
            }
        }
        return Path.of("audit.csv");
    }

    private List<Path> getAuditPathCandidates() {
        return List.of(
                Path.of("audit.csv"),
                Path.of("src", "com", "pao", "project", "audit.csv"),
                Path.of("src", "com", "pao", "project", "src", "audit.csv")
        );
    }
}
