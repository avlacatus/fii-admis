package ro.infoiasi.fiiadmis.db;

import com.google.common.base.Preconditions;
import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TableImpl<E extends Entity> implements Table<E> {

    private final Path tablePath;
    private final EntityFormatter<E> formatter;


    public TableImpl(Path dbPath, String tableName, EntityFormatter<E> formatter) throws IOException {
        Preconditions.checkArgument(dbPath != null, "Cannot init a table from a null path!");
        Preconditions.checkArgument(tableName != null, "Cannot init a table with a null name!");
        Preconditions.checkArgument(formatter != null, "Cannot init a table with a null formatter!");

        this.tablePath = Paths.get(dbPath.toString(), tableName);
        createTableFileIfNotExists(tablePath);
        this.formatter = formatter;
    }

    private void createTableFileIfNotExists(Path tablePath) throws IOException {
        if (Files.notExists(tablePath)) {
            Files.createFile(tablePath);
        }
    }

    @Override
    public String getTableName() {
        return tablePath.getFileName().toString();
    }

    @Override
    public Path getTablePath() {
        return tablePath;
    }

    @Override
    public EntityFormatter<E> getFormatter() {
        return formatter;
    }


}
