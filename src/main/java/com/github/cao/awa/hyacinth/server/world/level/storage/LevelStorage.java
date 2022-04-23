package com.github.cao.awa.hyacinth.server.world.level.storage;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

public class LevelStorage {
    private static final String DEFAULT_ICON = "icon.png";
    final Path savesDirectory;
    private final Path backupsDirectory;

    public LevelStorage(Path savesDirectory, Path backupsDirectory) {
        try {
            Files.createDirectories(Files.exists(savesDirectory) ? savesDirectory.toRealPath() : savesDirectory);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        this.savesDirectory = savesDirectory;
        this.backupsDirectory = backupsDirectory;
    }

    public static LevelStorage create(Path path) {
        return new LevelStorage(path, path.resolve("../backups"));
    }

    public class Session implements AutoCloseable {
        final SessionLock lock;
        final Path directory;
        private final String directoryName;

        public Session(String directoryName) throws IOException {
            this.directoryName = directoryName;
            this.directory = LevelStorage.this.savesDirectory.resolve(directoryName);
            this.lock = SessionLock.create(this.directory);
        }


        private void checkValid() {
            if (!this.lock.isValid()) {
                throw new IllegalStateException("Lock is no longer valid");
            }
        }

        public Optional<Path> getIconFile() {
            if (!this.lock.isValid()) {
                return Optional.empty();
            }
            return Optional.of(this.directory.resolve(LevelStorage.DEFAULT_ICON));
        }

        @Override
        public void close() throws IOException {
            this.lock.close();
        }
    }
}
