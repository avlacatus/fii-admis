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


    public TextDatabaseImpl(String dbRootPathName) throws IOException {

        this.dbRootPath = Paths.get(dbRootPathName);

        createDb(dbRootPath);

        tables = new HashMap<>();
    }

    private void createDb(Path dbRootPath) throws IOException {
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
    public Table<? extends Entity> getTable(String tableName) {
        return tables.get(tableName);
    }

    @Override
    public void drop() throws IOException {

        for (Table t : getAllTables()) {
            Files.delete(t.getTablePath());
        }

        try {
            Files.delete(dbRootPath); // only delete the db directory if it's empty (it contained only the tables)
        } catch (IOException e) {
            // this occurs if the current directory is not empty
            // if this is the case, for safety, do not delete it
        }

        tables.clear();
    }

    @Override
    public <E extends Entity> Table<E> createTable(String tableName, EntityFormatter<E> formatter, boolean replace)
                                        throws IOException {

        Preconditions.checkArgument(tableName != null, "table name must not be null");
        Preconditions.checkArgument(formatter != null, "formatter must not be null");

        if (!tables.containsKey(tableName) || replace) {

            Table<E> newTable = new TableImpl<>(dbRootPath, tableName, formatter);
            tables.put(tableName, newTable);
        }
        return (Table<E>) getTable(tableName);
    }


    @Override
    public void dropTable(String tableName) throws IOException {
        Preconditions.checkArgument(tables.containsKey(tableName), tableName +  "does not exist in the database");

        Files.deleteIfExists(tables.get(tableName).getTablePath());
        tables.remove(tableName);
    }

}
