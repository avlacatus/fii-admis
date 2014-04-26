package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import ro.infoiasi.fiiadmis.dao.parser.EntityIO;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Cosmin on 26/04/14.
 */
public abstract class AbstractEntityDAO<E extends Entity> implements EntityDAO<E> {

    protected abstract Path getDBFilePath();

    protected abstract EntityIO<E> getEntityIO();

    protected E getSingleItem(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(getDBFilePath(), Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty())
                    continue; // skipping empty lines

                E currentItem = getEntityIO().read(line);
                if (filter.apply(currentItem)) {
                    return currentItem;
                }
            }

            return null; // no items with this id was found
        }
    }

    protected List<E> getMultipleItems(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(getDBFilePath(), Charset.defaultCharset())) {
            String line = null;
            final List<E> results = Lists.newArrayList();
            while ((line = reader.readLine()) != null) {

                if (line.isEmpty())
                    continue; // skipping empty lines
                E currentItem = getEntityIO().read(line);
                if (filter.apply(currentItem)) {
                    results.add(currentItem);
                }
            }

            return results;
        }
    }
}
