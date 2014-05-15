package ro.infoiasi.fiiadmis.db.dao;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.db.parser.DefaultAdmissionResultsFormatter;
import ro.infoiasi.fiiadmis.db.parser.EntityFormatter;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResultFilters;

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

public class EntityDaoAdmissionResultImplTest {

    private EntityDAOImpl<AdmissionResult> dao;
    private static final String TESTTABLEADM = "testtableadm";
    private static final Path testPath = Paths.get(TESTTABLEADM);
    private List<AdmissionResult> testAdmissionResults;
    private static final Comparator<AdmissionResult> BY_FINAL_GRADE = new Comparator<AdmissionResult>() {
        @Override
        public int compare(AdmissionResult o1, AdmissionResult o2) {
            return -Double.compare(o1.getFinalGrade(), o2.getFinalGrade());
        }
    };


    @Before
    public void setup() throws IOException {

        testAdmissionResults = Lists.newArrayList();
        AdmissionResult c1 = new AdmissionResult("id1", "candid1", 9.4, AdmissionResult.Status.TAX_FREE);
        AdmissionResult c2 = new AdmissionResult("id2", "candid2", 7.5, AdmissionResult.Status.TAX);
        testAdmissionResults.add(c1);
        testAdmissionResults.add(c2);


        if (!Files.exists(testPath)){
            Files.createFile(testPath);
        }

        EntityFormatter<AdmissionResult> formatter = new DefaultAdmissionResultsFormatter();
        BufferedWriter writer = new BufferedWriter(new FileWriter(testPath.toFile()));

        writer.write(formatter.write(c1));
        writer.write(System.lineSeparator());
        writer.write(formatter.write(c2));
        writer.write(System.lineSeparator());

        writer.close();

        Table<AdmissionResult> mockTable = Mockito.mock(Table.class);


        Mockito.when(mockTable.getFormatter())
                .thenReturn(formatter);

        Mockito.when(mockTable.getTableName())
                .thenReturn("testtableadm");

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
        AdmissionResult expected = testAdmissionResults.get(0);

        AdmissionResult id1 = dao.getItemById("id1");

        assertEquals(expected, id1);
    }

    @Test
    public void testGetItemByInexistentId() throws Exception {
        AdmissionResult id1 = dao.getItemById("id9");

        assertNull(id1);
    }

    @Test
    public void testGetAllItems() throws Exception {

        List<AdmissionResult> items = dao.getItems(null, null);
        assertEquals(testAdmissionResults, items);
    }

    @Test
    public void testGetItemsWithPredicate() throws Exception {

        List<AdmissionResult> items = dao.getItems(AdmissionResultFilters.byFinalGrade(8, 0.5), null);
        assertEquals(testAdmissionResults.get(1), items.get(0));
    }

    @Test
    public void testGetItemsWithComparator() throws Exception {

        List<AdmissionResult> items = dao.getItems(null, BY_FINAL_GRADE);

        Collections.sort(testAdmissionResults, BY_FINAL_GRADE);
        assertEquals(testAdmissionResults, items);


    }

    @Test
    public void testUpdateItem() throws Exception {
        AdmissionResult result = testAdmissionResults.get(0);
        result.setFinalGrade(7);

        dao.updateItem(result);

        AdmissionResult itemById = dao.getItemById(result.getId());

        assertEquals(result, itemById);
    }

    @Test
    public void testAddItem() throws Exception {
        AdmissionResult newc = new AdmissionResult(null, "candid3", 8.75, AdmissionResult.Status.TAX);

        dao.addItem(newc);

        List<AdmissionResult> items = dao.getItems(AdmissionResultFilters.byCandidateId("candid3"), null);

        assertEquals(1, items.size());

        AdmissionResult returnedc = items.get(0);

        // the new result will have a randomly generated id
        assertEquals(newc.getCandidateId(), returnedc.getCandidateId());
        assertEquals(newc.getFinalGrade(), returnedc.getFinalGrade(), 0.0);
        assertEquals(newc.getAdmissionStatus(), returnedc.getAdmissionStatus());
    }

    @Test
    public void testDeleteItem() throws Exception {
        AdmissionResult result = testAdmissionResults.get(0);

        testAdmissionResults.remove(0);

        dao.deleteItem(result.getId());

        List<AdmissionResult> items = dao.getItems(null, null);

        assertEquals(testAdmissionResults, items);
    }


}
