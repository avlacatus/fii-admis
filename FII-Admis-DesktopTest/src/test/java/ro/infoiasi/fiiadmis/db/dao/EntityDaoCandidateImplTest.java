package ro.infoiasi.fiiadmis.db.dao;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.db.parser.DefaultCandidateFormatter;
import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityDaoCandidateImplTest {

    private EntityDAOImpl<Candidate> dao;
    private static final String TESTTABLE = "testtable";
    private static final Path testPath = Paths.get(TESTTABLE);
    private List<Candidate> testCandidates;
    private static final Comparator<Candidate> BY_LAST_NAME =
            new Comparator<Candidate>() {
                @Override
                public int compare(Candidate o1, Candidate o2) {
                    if (o1 != null && o2 != null) {
                        if (o1.getLastName() != null) {
                            return -o1.getLastName().compareTo(o2.getLastName());
                        } else
                            return 1;

                    }
                    return 0;
                }
            };


    @Before
    public void setup() throws IOException {

        testCandidates = Lists.newArrayList();
        Candidate c1 = new Candidate("id1", "fn1", "ln1", "sid1", 9, 8.5);
        Candidate c2 = new Candidate("id2", "fn2", "ln2", "sid2", 6, 7.5);
        testCandidates.add(c1);
        testCandidates.add(c2);


        if (!Files.exists(testPath)){
            Files.createFile(testPath);
        }

        EntityFormatter<Candidate> formatter = new DefaultCandidateFormatter();
        BufferedWriter writer = new BufferedWriter(new FileWriter(testPath.toFile()));

        writer.write(formatter.write(c1));
        writer.write(System.lineSeparator());
        writer.write(formatter.write(c2));
        writer.write(System.lineSeparator());

        writer.close();

        Table<Candidate> mockTable = Mockito.mock(Table.class);


        Mockito.when(mockTable.getFormatter())
                .thenReturn(formatter);

        Mockito.when(mockTable.getTableName())
                .thenReturn(TESTTABLE);

        Mockito.when(mockTable.getTablePath())
               .thenReturn(testPath);

        dao = new EntityDAOImpl<>(mockTable);
    }

    @After
    public void cleanup() throws IOException {
        Files.deleteIfExists(testPath);
    }

    @Test
    public void testGetItemById() throws Exception {
        Candidate expected = testCandidates.get(0);

        Candidate id1 = dao.getItemById("id1");

        assertEquals(expected, id1);
    }

    @Test
    public void testGetItemByInexistentId() throws Exception {
        Candidate id1 = dao.getItemById("id9");

        assertNull(id1);
    }

    @Test
    public void testGetAllItems() throws Exception {

        List<Candidate> items = dao.getItems(null, null);
        assertEquals(testCandidates, items);
    }

    @Test
    public void testGetItemsWithPredicate() throws Exception {

        List<Candidate> items = dao.getItems(CandidateFilters.byATestGrade(8, 0.5), null);
        assertEquals(testCandidates, items);
    }

    @Test
    public void testGetItemsWithComparator() throws Exception {

        List<Candidate> items = dao.getItems(null, BY_LAST_NAME);

        Collections.sort(testCandidates, BY_LAST_NAME);
        assertEquals(testCandidates, items);


    }

    @Test
    public void testUpdateItem() throws Exception {
        Candidate candidate = testCandidates.get(0);
        candidate.setFirstName("modif");

        dao.updateItem(candidate);

        Candidate itemById = dao.getItemById(candidate.getId());

        assertEquals(candidate, itemById);
    }

    @Test
    public void testAddItem() throws Exception {
        Candidate newc = new Candidate("id3", "fn3", "ln3", "sid3", 9, 9);

        dao.addItem(newc);

        List<Candidate> items = dao.getItems(CandidateFilters.byFirstName("fn3"), null);

        assertEquals(1, items.size());

        Candidate returnedc = items.get(0);

        // the new candidate will have a randomly generated id
        assertEquals(newc.getFirstName(), returnedc.getFirstName());
        assertEquals(newc.getLastName(), returnedc.getLastName());
        assertEquals(newc.getSocialId(), returnedc.getSocialId());
        assertEquals(newc.getATestGrade(), returnedc.getATestGrade(), 0.0);
        assertEquals(newc.getGpaGrade(), returnedc.getGpaGrade(), 0.0);
    }

    @Test
    public void testDeleteItem() throws Exception {
        Candidate candidate = testCandidates.get(0);

        testCandidates.remove(0);

        dao.deleteItem(candidate.getId());

        List<Candidate> items = dao.getItems(null, null);

        assertEquals(testCandidates, items);
    }


}
