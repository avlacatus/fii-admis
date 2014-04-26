package ro.infoiasi.fiiadmis.dao.parser;

import ro.infoiasi.fiiadmis.model.Entity;

public interface EntityIO<E extends Entity> {

    String getFieldSeparator();

    E read(String textLine);

    String write(E entity);
}
