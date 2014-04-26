package ro.infoiasi.fiiadmis.db;


import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextDatabaseImpl<E extends Entity> implements TextDatabase<E> {

    private final Path dbFilePath;
    private final EntityFormatter<E> formatter;

    public TextDatabaseImpl(String dbFilePathName, EntityFormatter<E> formatter) throws IOException {
        this.dbFilePath = Paths.get(dbFilePathName);
        this.formatter = formatter;
        createDb(dbFilePath);
    }

    private void createDb(Path dbFilePath) throws IOException {
        if (Files.notExists(dbFilePath)) {
            Files.createFile(dbFilePath);
        }
    }

    @Override
    public Path getPath() {
        return dbFilePath;
    }

    @Override
    public EntityFormatter<E> getFormatter() {
        return formatter;
    }


}
