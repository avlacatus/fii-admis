package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.util.List;

/**
 * @author <a href="mailto:anad@amazon.com">Ana-Maria Daneliuc</a>
 */
public class CandidatesDaoImpl implements CandidatesDao {

    private final String dbFilename;

    public CandidatesDaoImpl(String dbFilename) {

        this.dbFilename = dbFilename;
    }

    @Override
    public Candidate getCandidateById(String id) {
        return null;
    }

    @Override
    public Candidate getCandidateBySocialNumber(String id) {
        return null;
    }

    @Override
    public List<Candidate> getAllCandidates(Predicate<Candidate> filter) {
        return null;
    }
}
