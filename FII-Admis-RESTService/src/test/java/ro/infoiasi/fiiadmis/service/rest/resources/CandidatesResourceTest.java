package ro.infoiasi.fiiadmis.service.rest.resources;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.ext.json.JsonRepresentation;
import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.db.dao.EntityDAOImpl;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;
import ro.infoiasi.fiiadmis.service.rest.util.DummyJsonGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CandidatesResourceTest {

    private static final Logger LOG = Logger.getLogger(CandidatesResourceTest.class);

    private static final Comparator<AdmissionResult> BY_FINAL_GRADE = AdmissionResultComparator.byFinalGrade();
    private static final Comparator<Candidate> BY_LAST_NAME = CandidateComparator.byLastName();

    private List<Candidate> mockCandidates;

    @Before
    public void initializeMockDaoHolder() throws IOException {
        // Mock list with candidates.
        mockCandidates = new ArrayList<Candidate>();
        mockCandidates.add(new Candidate("a", "Maria", "Jolie", "123456789123", 9.12, 7.12));
        mockCandidates.add(new Candidate("b", "John", "Smith", "312456789123", 8.23, 8.13));
        mockCandidates.add(new Candidate("c", "Jane", "Ron", "345612789123", 9.67, 6.58));

        // Mock candidates dao.
        EntityDAO<Candidate> candidatesDao =
                Mockito.mock(EntityDAOImpl.class);
        Mockito
                .when(candidatesDao.getItems(null, BY_LAST_NAME))
                .thenReturn(mockCandidates);

        // Mock admission results dao.
        EntityDAO<AdmissionResult> admissionResultsDao = Mockito.mock(EntityDAOImpl.class);
        Mockito
                .when(admissionResultsDao.getItems(null, BY_FINAL_GRADE))
                .thenReturn(null);

        // Create DaoHolder with both mocks.
        DaoHolder.setCustomCandidatesDao(candidatesDao);
        DaoHolder.setCustomAdmissionResults(admissionResultsDao);
    }

    @After
    public void clearDao() {
        DaoHolder.clearDao();
    }

    @Test
    public void testGetCandidates() throws IOException {
        JsonRepresentation candidates = new CandidatesResource().getCandidates();
        String response = candidates.getText();

        assertTrue(response.contains("candidates"));

        long partsLength = 0;
        long parts = 0;
        for (Candidate c : mockCandidates) {
            for (String part : DummyJsonGenerator.jsonPartsFromCandidate(c)) {
                assertTrue(response.contains(part));
                partsLength += part.length();
                parts++;
            }
        }

        long expectedLength = "{\"candidates\":[]}".length()
                /* length of every part */+ partsLength
                /* curly braces for every candidate */+ "{}".length() * mockCandidates.size()
                /* commas between parts */+ ",".length() * (parts - 1);

        assertEquals(expectedLength, response.length());
    }
}
