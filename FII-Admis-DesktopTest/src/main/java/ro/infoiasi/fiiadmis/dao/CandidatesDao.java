package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.util.List;

/**
 * @author <a href="mailto:anad@amazon.com">Ana-Maria Daneliuc</a>
 */
public interface CandidatesDao {

    Candidate getCandidateById(String id);

    Candidate getCandidateBySocialNumber(String id);

    List<Candidate> getAllCandidates(Predicate<Candidate> filter);
}
