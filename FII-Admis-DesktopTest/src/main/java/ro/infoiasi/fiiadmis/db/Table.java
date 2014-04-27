package ro.infoiasi.fiiadmis.db;

import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.nio.file.Path;

public interface Table<E extends Entity> {

    String getTableName();
    Path getTablePath();
    EntityFormatter<E> getFormatter();
}
