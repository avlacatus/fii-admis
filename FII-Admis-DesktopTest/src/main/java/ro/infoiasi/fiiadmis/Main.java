package ro.infoiasi.fiiadmis;

import ro.infoiasi.fiiadmis.dao.CandidatesDao;
import ro.infoiasi.fiiadmis.dao.CandidatesDaoImpl;
import ro.infoiasi.fiiadmis.dao.Filters;
import ro.infoiasi.fiiadmis.dao.parser.DefaultCandidateIO;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        CandidatesDao dao = new CandidatesDaoImpl("test.input", new DefaultCandidateIO());

        Candidate candidate = dao.getCandidateById(0);

        System.out.println(candidate);

        List<Candidate> result = dao.getAllCandidates(Filters.byFirstName("alex"));

        System.out.println(result);

        List<Candidate> allCandidates = dao.getAllCandidates(Filters.byGpaGrade(9.5, 0.06));

        System.out.println(allCandidates);
    }
}
