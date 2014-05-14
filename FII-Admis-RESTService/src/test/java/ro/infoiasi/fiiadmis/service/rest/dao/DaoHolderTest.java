package ro.infoiasi.fiiadmis.service.rest.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class DaoHolderTest {

    @After
    public void afterEveryTest() {
        DaoHolder.clearDao();
    }

    @Test
    public void testClearDao() {
        assertNull(DaoHolder.getCandidateDao());
        assertNull(DaoHolder.getAdmissionResultsDao());
    }

    @Test
    public void testInitializeDb() throws IOException {
        DaoHolder.initializeDb();

        assertNotNull(DaoHolder.getCandidateDao());
        assertNotNull(DaoHolder.getAdmissionResultsDao());
    }

    @Test
    public void testStartComputingAdmissionResults() throws IOException {
        DaoHolder.initializeDb();

        DaoHolder.startComputingAdmissionResults();

        assertNotNull(DaoHolder.getCandidateDao());
        assertNotNull(DaoHolder.getAdmissionResultsDao());
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class.forName(DaoHolder.class.getCanonicalName()).newInstance();
    }
}
