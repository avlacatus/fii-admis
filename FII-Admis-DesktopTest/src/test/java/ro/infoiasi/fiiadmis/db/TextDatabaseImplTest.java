package ro.infoiasi.fiiadmis.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ro.infoiasi.fiiadmis.db.parser.DefaultCandidateFormatter;
import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TextDatabaseImplTest {

    private TextDatabaseImpl db;
    private Table<Candidate> candidateTable;
    private static final String dbName = "dbp";
    private static final Path dbPath = Paths.get(dbName);
    private static final String fakeTableName = "tbl";
    private static final Path fakeTablePath = Paths.get(dbName, fakeTableName);

    @Before
    public void setUp() throws Exception {

        db = new TextDatabaseImpl(dbName);

        EntityFormatter<Candidate> formatter = new DefaultCandidateFormatter();
        candidateTable = db.openTableOrCreateIfNotExists(fakeTableName, formatter);

    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(fakeTablePath);
        Files.deleteIfExists(dbPath);

    }

    @Test
    public void testDbCreated() {

        assertTrue(Files.exists(dbPath));

        assertTrue(Files.isDirectory(dbPath));
    }

    @Test
    public void testTableCreated() throws IOException {

        assertTrue(Files.exists(fakeTablePath));

        assertTrue(Files.isRegularFile(fakeTablePath));

        assertSame(candidateTable, db.openTableOrCreateIfNotExists(fakeTableName, new DefaultCandidateFormatter()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTablename() throws IOException {

        db.openTableOrCreateIfNotExists(null, new DefaultCandidateFormatter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTableFormatter() throws IOException {

        db.openTableOrCreateIfNotExists("whatever", null);
    }

    @Test
    public void testGetRootPath() {

        assertEquals(dbPath, db.getRootPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDropInexistentTable() throws IOException {

        db.dropTable("whatever");
    }

    @Test()
    public void testDropTable() throws IOException {

        db.dropTable(fakeTableName);

        assertFalse(Files.exists(fakeTablePath));
        assertTrue(Files.exists(dbPath));

        assertEquals(0, db.getAllTables().size());
    }

    @Test()
    public void testDropDb() throws IOException {

        db.drop();

        assertFalse(Files.exists(fakeTablePath));
        assertTrue(Files.exists(dbPath));

        assertEquals(0, db.getAllTables().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testDropDbCreateException() throws IOException {

        db.drop();

        db.openTableOrCreateIfNotExists("whatever", new DefaultCandidateFormatter());
    }

    @Test(expected = IllegalStateException.class)
    public void testDropDbTwiceException() throws IOException {

        db.drop();

        db.drop();
    }

    @Test(expected = IllegalStateException.class)
    public void testDropDbDropTableException() throws IOException {

        db.drop();

        db.dropTable(fakeTableName);
    }
}
