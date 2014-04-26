package ro.infoiasi.fiiadmis.db.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import ro.infoiasi.fiiadmis.db.TextDatabase;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class EntityDAOImpl<E extends Entity> implements EntityDAO<E> {

    private final TextDatabase<E> db;

    public EntityDAOImpl(TextDatabase<E> db) {
        this.db = db;
    }

    private E getSingleItem(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(db.getPath(), Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty())
                    continue; // skipping empty lines

                E currentItem = db.getFormatter().read(line);
                if (filter.apply(currentItem)) {
                    return currentItem;
                }
            }

            return null; // no items with this id was found
        }
    }

    private List<E> getMultipleItems(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(db.getPath(), Charset.defaultCharset())) {
            String line = null;
            final List<E> results = Lists.newArrayList();
            while ((line = reader.readLine()) != null) {

                if (line.isEmpty())
                    continue; // skipping empty lines
                E currentItem = db.getFormatter().read(line);
                if (filter.apply(currentItem)) {
                    results.add(currentItem);
                }
            }

            return results;
        }
    }

    @Override
    public String addItem(E item) throws IOException {
        String newId = RandomStringUtils.randomAlphanumeric(4);
        item.setId(newId);
        try (BufferedWriter writer = Files.newBufferedWriter(db.getPath(), Charset.defaultCharset(), StandardOpenOption.APPEND)) {
            writer.write(db.getFormatter().write(item));
            writer.write(System.lineSeparator());
        }
        return newId;
    }

    @Override
    public E getItemById(final String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "id must not be null");
        return getSingleItem(new Predicate<E>() {
            @Override
            public boolean apply(E input) {
                return entityId.equals(input.getId());
            }
        });
    }

    @Override
    public List<E> getItems(Predicate<E> filter) throws IOException {
        Preconditions.checkArgument(filter != null, "Filter must not be null");
        return getMultipleItems(filter);
    }

    @Override
    public void updateItem(E item) throws IOException {
        Preconditions.checkArgument(item != null, "The input must not be null");
        Path tmpPath = Paths.get(db.getPath().toString() + ".tmp");
        Scanner s = new Scanner(db.getPath());
        try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.isEmpty())
                    continue; // skipping empty lines
                E currentE = db.getFormatter().read(line);

                if (item.getId().equals(currentE.getId())) {
                    writer.write(db.getFormatter().write(item));
                    writer.write(System.lineSeparator());
                } else {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
            s.close();
        }
        Files.move(tmpPath, db.getPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void deleteItem(String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "id must be null");
        Path tmpPath = Paths.get(db.getPath().toString() + ".tmp");
        Scanner s = new Scanner(db.getPath());

        try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {

            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.isEmpty())
                    continue; // skipping empty lines
                E currentE = db.getFormatter().read(line);

                if (!entityId.equals(currentE.getId())) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
            s.close();
        }
        Files.move(tmpPath, db.getPath(), StandardCopyOption.REPLACE_EXISTING);

    }
}
