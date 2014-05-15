package ro.infoiasi.fiiadmis.service.rest.dao.business;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.db.TextDatabaseImpl;
import ro.infoiasi.fiiadmis.db.dao.EntityDAOImpl;
import ro.infoiasi.fiiadmis.db.parser.DefaultAdmissionResultsFormatter;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResult.Status;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.service.rest.resources.AdmissionResultComparator;
import ro.infoiasi.fiiadmis.service.rest.resources.CandidateComparator;

public class AdmissionResultsProcessorTest {

    private static final Logger LOG = Logger.getLogger(AdmissionResultsProcessorTest.class);

    private static final Comparator<Candidate> BY_LAST_NAME = CandidateComparator.byLastName();

    private static final List<Candidate> mockCandidates = new ArrayList<Candidate>();

    private EntityDAOImpl<Candidate> candidatesDao;
    private EntityDAOImpl<AdmissionResult> admissionResultsDao;

    private TextDatabaseImpl db;

    @Before
    public void initializeMockDaoHolder() throws IOException {
        // Create dummy list with candidates.
        mockCandidates.add(new Candidate("a", "Maria", "Jolie", "123456789123", 9.12, 9.14));
        mockCandidates.add(new Candidate("b", "John", "Smith", "312456789123", 8.23, 5.13));
        mockCandidates.add(new Candidate("c", "Jane", "Ron", "345612789123", 9.67, 6.58));

        // Mock candidates dao.
        candidatesDao = Mockito.mock(EntityDAOImpl.class);
        Mockito
                .when(candidatesDao.getItems(null, BY_LAST_NAME))
                .thenReturn(mockCandidates);
        Mockito
                .when(candidatesDao.getItems(null, null))
                .thenReturn(mockCandidates);

        // Create dummt database and tables
        db = new TextDatabaseImpl("testdb");
        Table<AdmissionResult> table = db.openTableOrCreateIfNotExists("test",
                new DefaultAdmissionResultsFormatter());

        // Create admission results dao.
        admissionResultsDao = new EntityDAOImpl<AdmissionResult>(table);
    }

    @org.junit.After
    public void clearDao() throws IOException {
        candidatesDao = null;
        admissionResultsDao = null;
        db.drop();
    }

    @Test
    public void test() throws IOException {
        AdmissionResultsProcessor.get().processResults(candidatesDao, admissionResultsDao,
                AdmissionResultComparator.byFinalGrade());

        List<AdmissionResult> results = admissionResultsDao.getItems(null, AdmissionResultComparator.byFinalGrade());

        assertEquals(3, results.size());

        check(results.get(0), "a", 9.13, Status.TAX_FREE);
        check(results.get(1), "c", 8.125, Status.TAX);
        check(results.get(2), "b", 6.68, Status.REJECTED);
    }

    private static void check(AdmissionResult result, String expectedCandidateId,
            double expectedFinalGrade, Status expectedStatus) {
        assertEquals(4, result.getId().length());
        assertEquals(expectedCandidateId, result.getCandidateId());
        assertEquals(expectedFinalGrade, result.getFinalGrade(), 0.5);
        assertEquals(expectedStatus, result.getAdmissionStatus());
    }
}
