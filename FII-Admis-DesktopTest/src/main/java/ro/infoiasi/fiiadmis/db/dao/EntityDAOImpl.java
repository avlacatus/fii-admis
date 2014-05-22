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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class EntityDAOImpl<E extends Entity> implements EntityDAO<E> {

	private final Table<E> table;
	private final Object toSync = new Object();

	public EntityDAOImpl(Table<E> table) {
		this.table = table;
	}

	@Override
	public String addItem(E item) throws IOException {
        Preconditions.checkArgument(item != null, "Cannot insert a null object!");
        Preconditions.checkArgument(table != null, "Cannot insert an object into a null table!");

		synchronized (toSync) {
			String newId = RandomStringUtils.randomAlphanumeric(4);
			while (getItemById(newId) != null) {
				newId = RandomStringUtils.randomAlphanumeric(4); // ensuring that the id is unique
			}

			item.setId(newId);
			try (BufferedWriter writer = Files.newBufferedWriter(table.getTablePath(), Charset.defaultCharset(),
					StandardOpenOption.APPEND)) {
				writer.write(table.getFormatter().write(item));
				writer.write(System.lineSeparator());
			}
            Preconditions.checkArgument(getItemById(item.getId()) != null, "Object was not added!");
            return newId;
        }

	}

	@Override
	public E getItemById(final String entityId) throws IOException {
		Preconditions.checkArgument(entityId != null && entityId.length() > 0, "Invalid entity id!");
		return getSingleItem(new Predicate<E>() {
			@Override
			public boolean apply(E input) {
				return entityId.equals(input.getId());
			}
		});
	}

	@Override
	public List<E> getItems(Predicate<E> filter, Comparator<E> comparator) throws IOException {
		return getItems(filter, comparator, -1);
	}

	@Override
	public List<E> getItems(Predicate<E> filter, Comparator<E> comparator, int maxResults) throws IOException {
		return getMultipleItems(filter, comparator, maxResults);
	}

	@Override
	public void updateItem(E item) throws IOException {
		Preconditions.checkArgument(item != null, "The input must not be null");
        Preconditions.checkArgument(table != null, "Cannot update an object into a null table!");
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
        Preconditions.checkArgument(getItemById(item.getId()).equals(item), "Object was not updated!");
	}

	@Override
	public void deleteItem(String entityId) throws IOException {
        Preconditions.checkArgument(entityId != null && entityId.length() > 0, "Invalid entity id!");
        Preconditions.checkArgument(table != null, "Cannot delete an object from a null table!");
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
        Preconditions.checkArgument(getItemById(entityId) == null, "Object was not deleted!");
	}

	private E getSingleItem(Predicate<E> filter) throws IOException {
		List<E> outputList = getMultipleItems(filter, null, 1);
		if (outputList != null && outputList.size() > 0) {
			return outputList.get(0);
		} else {
			return null;
		}
	}

	private List<E> getMultipleItems(Predicate<E> filter, Comparator<E> comparator, int maxResults) throws IOException {
        Preconditions.checkArgument(table != null, "Cannot read objects from a null table!");
		synchronized (toSync) {
			try (BufferedReader reader = Files.newBufferedReader(table.getTablePath(), Charset.defaultCharset())) {
				String line = null;
				List<E> results = Lists.newArrayList();
				while ((line = reader.readLine()) != null) {
					if (line.isEmpty())
						continue; // skipping empty lines
					E currentItem = table.getFormatter().read(line);
					if (filter != null) {
						if (filter.apply(currentItem)) {
							results.add(currentItem);
						}
					} else {
						results.add(currentItem);
					}

				}

				if (comparator != null) {
					Collections.sort(results, comparator);
				}

				if (maxResults != -1 && results.size() > 0 && maxResults < results.size()) {
					results = results.subList(0, maxResults);
				}
				reader.close();
				return results;
			}
		}
	}
}
