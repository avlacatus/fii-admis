package ro.infoiasi.fiiadmis.db;


import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.nio.file.Path;

public interface TextDatabase<E extends Entity> {

    Path getPath();

    EntityFormatter<E> getFormatter();
}
