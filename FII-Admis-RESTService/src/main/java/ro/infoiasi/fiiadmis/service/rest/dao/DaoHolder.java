package ro.infoiasi.fiiadmis.service.rest.dao;

import java.io.IOException;

import org.apache.log4j.Logger;

import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.db.TextDatabase;
import ro.infoiasi.fiiadmis.db.TextDatabaseImpl;
import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.db.dao.EntityDAOImpl;
import ro.infoiasi.fiiadmis.db.parser.DefaultAdmissionResultsFormatter;
import ro.infoiasi.fiiadmis.db.parser.DefaultCandidateFormatter;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.service.rest.resources.CandidatesResource;

public class DaoHolder {

    private static final Logger LOG = Logger.getLogger(CandidatesResource.class);

    private static TextDatabase db;

    private static EntityDAO<Candidate> candidatesDao;
    private static EntityDAO<AdmissionResult> admissionResultsDao;

    private DaoHolder() {
    }

    public static EntityDAO<Candidate> getCandidateDao() {
        return candidatesDao;
    }

    public static EntityDAO<AdmissionResult> getAdmissionResultsDao() {
        return admissionResultsDao;
    }

    public static void initializeDb() {
        try {
            LOG.debug("Initializing database.");
            db = new TextDatabaseImpl("fiiadmisdb");

            LOG.debug("Initializing table test-candidates.");
            candidatesDao = initCandidateDao("test-candidates");

            LOG.debug("Initializing table test-admission-miralp.");
            admissionResultsDao = initAdmissionResults("test-admission-mirelap");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't create database", e);
        }
    }

    private static EntityDAO<Candidate> initCandidateDao(String tableName) throws IOException {
        Table<Candidate> candidatesTable = db.createTable(tableName, new DefaultCandidateFormatter(), false);
        return new EntityDAOImpl<>(candidatesTable);
    }

    private static EntityDAO<AdmissionResult> initAdmissionResults(String tableName) throws IOException {
        Table<AdmissionResult> admissionResultsTable = db.createTable(tableName,
                new DefaultAdmissionResultsFormatter(), false);
        return new EntityDAOImpl<>(admissionResultsTable);
    }

}
