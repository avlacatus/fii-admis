package ro.infoiasi.fiiadmis.service.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;

import ro.infoiasi.fiiadmis.service.rest.dao.DaoHolder;

public class MainApplicationTest {

    @After
    public void afterEveryTest() {
        DaoHolder.clearDao();
    }

    @Test
    public void testDaoHolder() {
        // Clear the DAO.
        DaoHolder.clearDao();

        // Checking the DAO before the initialization of the service.
        assertNull(DaoHolder.getCandidateDao());

        // Start the service.
        new MainApplication().createInboundRoot();

        // The DAO should be initialized.
        assertNotNull(DaoHolder.getCandidateDao());
    }
}
