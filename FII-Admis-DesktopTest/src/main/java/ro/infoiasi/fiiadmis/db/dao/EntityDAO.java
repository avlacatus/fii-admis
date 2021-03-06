package ro.infoiasi.fiiadmis.db.dao;

import com.google.common.base.Predicate;

import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public interface EntityDAO<E extends Entity> {

    E getItemById(String entityId) throws IOException;

    List<E> getItems(Predicate<E> filter, Comparator<E> comparator) throws IOException;
    
    List<E> getItems(Predicate<E> filter, Comparator<E> comparator, int maxResults) throws IOException;

    String addItem(E item) throws IOException;

    void updateItem(E item) throws IOException;

    void deleteItem(String entityId) throws IOException;
}
