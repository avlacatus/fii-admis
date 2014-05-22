package ro.infoiasi.fiiadmis.service.rest.dao;

import java.io.IOException;
import java.util.Comparator;

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
import ro.infoiasi.fiiadmis.service.rest.dao.business.AdmissionResultsProcessor;
import ro.infoiasi.fiiadmis.service.rest.resources.CandidatesResource;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

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

            LOG.debug("Initializing table test-admission.");
            admissionResultsDao = initAdmissionResults("test-admission");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't create database", e);
        }
    }

    @VisibleForTesting
    public static void setCustomCandidatesDao(EntityDAO<Candidate> candidateDaoCustom) {
        candidatesDao = candidateDaoCustom;
    }

    @VisibleForTesting
    public static void setCustomAdmissionResults(EntityDAO<AdmissionResult> admissionResultsDaoCustom) {
        admissionResultsDao = admissionResultsDaoCustom;
    }

    @VisibleForTesting
    public static void clearDao() {
        candidatesDao = null;
        admissionResultsDao = null;
    }

    private static EntityDAO<Candidate> initCandidateDao(String tableName) throws IOException {
        Preconditions.checkNotNull(tableName);
        Table<Candidate> candidatesTable = db.openTableOrCreateIfNotExists(tableName, new DefaultCandidateFormatter());
        return new EntityDAOImpl<>(candidatesTable);
    }

    private static EntityDAO<AdmissionResult> initAdmissionResults(String tableName) throws IOException {
        Preconditions.checkNotNull(tableName);
        Table<AdmissionResult> admissionResultsTable = db.openTableOrCreateIfNotExists(tableName,
                new DefaultAdmissionResultsFormatter());
        return new EntityDAOImpl<>(admissionResultsTable);
    }

    public static EntityDAO<AdmissionResult> startComputingAdmissionResults() {
        AdmissionResultsProcessor.get().processResults(getCandidateDao(), getAdmissionResultsDao(),
                new Comparator<AdmissionResult>() {
                    @Override
                    public int compare(AdmissionResult o1, AdmissionResult o2) {
                        return (-1) * Double.compare(o1.getFinalGrade(), o2.getFinalGrade());
                    }
                });
        return getAdmissionResultsDao();
    }
}
