package ro.infoiasi.fiiadmis.db.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class EntityDAOImpl<E extends Entity> implements EntityDAO<E> {

    private final Table<E> table;
    private final Object toSync = new Object();


    public EntityDAOImpl(Table<E> table) {
        this.table = table;
    }

    private E getSingleItem(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(table.getTablePath(), Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty())
                    continue; // skipping empty lines

                E currentItem = table.getFormatter().read(line);
                if (filter.apply(currentItem)) {
                    return currentItem;
                }
            }

            return null; // no items with this id was found
        }
    }

    private List<E> getMultipleItems(Predicate<E> filter) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(table.getTablePath(), Charset.defaultCharset())) {
            String line = null;
            final List<E> results = Lists.newArrayList();
            while ((line = reader.readLine()) != null) {

                if (line.isEmpty())
                    continue; // skipping empty lines
                E currentItem = table.getFormatter().read(line);
                if (filter.apply(currentItem)) {
                    results.add(currentItem);
                }
            }

            return results;
        }
    }

    @Override
    public String addItem(E item) throws IOException {

        synchronized (toSync) {
            String newId = RandomStringUtils.randomAlphanumeric(4);
            while (getItemById(newId) != null) {
                newId = RandomStringUtils.randomAlphanumeric(4);   // ensuring that the id is unique
            }

            item.setId(newId);
            try (BufferedWriter writer = Files.newBufferedWriter(table.getTablePath(), Charset.defaultCharset(), StandardOpenOption.APPEND)) {
                writer.write(table.getFormatter().write(item));
                writer.write(System.lineSeparator());
            }
            return newId;
        }
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
        synchronized (toSync) {
            Path tmpPath = Paths.get(table.getTablePath().toString() + ".tmp");
            Scanner s = new Scanner(table.getTablePath());
            try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    if (line.isEmpty())
                        continue; // skipping empty lines
                    E currentE = table.getFormatter().read(line);

                    if (item.getId().equals(currentE.getId())) {
                        writer.write(table.getFormatter().write(item));
                        writer.write(System.lineSeparator());
                    } else {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    }
                }
                s.close();
            }
            Files.move(tmpPath, table.getTablePath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void deleteItem(String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null, "id must be null");
        synchronized (toSync) {
            Path tmpPath = Paths.get(table.getTablePath().toString() + ".tmp");
            Scanner s = new Scanner(table.getTablePath());

            try (BufferedWriter writer = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())) {

                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    if (line.isEmpty())
                        continue; // skipping empty lines
                    E currentE = table.getFormatter().read(line);

                    if (!entityId.equals(currentE.getId())) {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    }
                }
                s.close();
            }
            Files.move(tmpPath, table.getTablePath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }
}
