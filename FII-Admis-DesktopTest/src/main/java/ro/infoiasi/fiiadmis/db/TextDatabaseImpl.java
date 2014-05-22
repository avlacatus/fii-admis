package ro.infoiasi.fiiadmis.db;


import com.google.common.base.Preconditions;
import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TextDatabaseImpl implements TextDatabase {

    private final Path dbRootPath;

    private final Map<String, Table<? extends Entity>> tables;

    private final Object toSync = new Object();

    private boolean dropped = false;


    public TextDatabaseImpl(String dbRootPathName) throws IOException {
        Preconditions.checkArgument(dbRootPathName != null && dbRootPathName.length() > 0, "Invalid database path name!");
        this.dbRootPath = Paths.get(dbRootPathName);
        createDbFolderIfNotExists(dbRootPath);
        tables = new HashMap<>();
    }

    private void createDbFolderIfNotExists(Path dbRootPath) throws IOException {
        if (Files.notExists(dbRootPath)) {
            Files.createDirectory(dbRootPath);
        }
    }

    @Override
    public Path getRootPath() {
        return dbRootPath;
    }

    @Override
    public Collection<Table<? extends Entity>> getAllTables() {
        return tables.values();
    }

    @Override
    public void drop() throws IOException {
        Preconditions.checkState(!dropped, "Database was dropped! Operation no longer possible!");
        synchronized (toSync) {

            for (Table<? extends Entity> t : getAllTables()) {
                Files.delete(t.getTablePath());
            }
            tables.clear();
            dropped = true;
        }
    }

    @Override
    public <E extends Entity> Table<E> openTableOrCreateIfNotExists(String tableName, EntityFormatter<E> formatter)
                                        throws IOException {
        Preconditions.checkState(!dropped, "Database was dropped! Operation no longer possible!");
        Preconditions.checkArgument(tableName != null && tableName.length() > 0, "Invalid table name!");
        Preconditions.checkArgument(formatter != null, "Formatter must not be null!");
        synchronized (toSync) {

            if (!tables.containsKey(tableName)) {

                Table<E> newTable = new TableImpl<>(dbRootPath, tableName, formatter);
                tables.put(tableName, newTable);
            }
            return (Table<E>) (tables.get(tableName));
        }
    }


    @Override
    public void dropTable(String tableName) throws IOException {
        Preconditions.checkState(!dropped, "Database was dropped! Operation no longer possible!");
        Preconditions.checkArgument(tables.containsKey(tableName), tableName +  "does not exist in the database!");

        synchronized (toSync) {

            Files.deleteIfExists(tables.get(tableName).getTablePath());
            tables.remove(tableName);
        }
    }

}
