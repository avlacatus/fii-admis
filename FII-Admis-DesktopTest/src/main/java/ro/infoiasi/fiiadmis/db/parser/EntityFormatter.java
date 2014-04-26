package ro.infoiasi.fiiadmis.db.parser;

import ro.infoiasi.fiiadmis.model.Entity;

public interface EntityFormatter<E extends Entity> {

    String getFieldSeparator();

    E read(String textLine);

    String write(E entity);
}
