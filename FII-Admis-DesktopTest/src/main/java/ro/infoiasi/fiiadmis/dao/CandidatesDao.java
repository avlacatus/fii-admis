package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.IOException;
import java.util.List;

public interface CandidatesDao {

    Candidate getCandidateById(int id) throws IOException;

    Candidate getCandidateBySocialNumber(String socialNumber) throws IOException;

    List<Candidate> getAllCandidates(Predicate<Candidate> filter) throws IOException;

    int addCandidate(Candidate c);

    void updateCandidate(Candidate c);

    void deleteCandidate(int candidateId);
}
