package ro.infoiasi.fiiadmis.db;


import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface TextDatabase {

    Path getRootPath();

    Collection<Table<? extends Entity>> getAllTables();

    Table<? extends Entity> getTable(String tableName);

    void drop() throws IOException;

    <E extends Entity> Table<E> createTable(String tableName, EntityFormatter<E> formatter, boolean replace)
                                    throws IOException;


    void dropTable(String tableName) throws IOException;
}
