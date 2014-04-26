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

        Candidate candidate = dao.getCandidateById("15a9");

        System.out.println(candidate);

        List<Candidate> result = dao.getAllCandidates(Filters.byFirstName("alex"));

        System.out.println(result);

        List<Candidate> allCandidates = dao.getAllCandidates(Filters.byGpaGrade(9.5, 0.06));

        System.out.println(allCandidates);

        Candidate c = new Candidate();
        c.setFirstName("ana");
        c.setLastName("ionescu");
        c.setSocialId("347389523255435");
        c.setGpaGrade(9.00);
        c.setATestGrade(8.4);

        String s = dao.addCandidate(c);

        System.out.println(s);

        listAll(dao);

        dao.deleteCandidate(s);

        listAll(dao);

        s = dao.addCandidate(c);

        System.out.println(s);

        listAll(dao);

        candidate.setFirstName("mihai");

        dao.updateCandidate(candidate);

        listAll(dao);


    }

    private static void listAll(CandidatesDao dao) throws IOException {
        List<Candidate> candidateList;
        candidateList = dao.getAllCandidates(Filters.all());

        System.out.println(candidateList);
    }
}
