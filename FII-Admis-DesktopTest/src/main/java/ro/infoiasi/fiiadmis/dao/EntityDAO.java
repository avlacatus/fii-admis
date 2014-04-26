package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.util.List;

public interface EntityDAO<E extends Entity> {

    E getItemById(String entityId) throws IOException;

    List<E> getItems(Predicate<E> filter) throws IOException;

    String addItem(E item) throws IOException;

    void updateItem(E item) throws IOException;

    void deleteItem(String entityId) throws IOException;
}
