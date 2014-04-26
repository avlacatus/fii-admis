package ro.infoiasi.fiiadmis;

import com.google.common.base.Predicate;
import ro.infoiasi.fiiadmis.dao.AdmissionResultsDAOImpl;
import ro.infoiasi.fiiadmis.dao.CandidatesDaoImpl;
import ro.infoiasi.fiiadmis.dao.Filters;
import ro.infoiasi.fiiadmis.dao.parser.DefaultAdmissionResultsIO;
import ro.infoiasi.fiiadmis.dao.parser.DefaultCandidateIO;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.Candidate;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        CandidatesDaoImpl dao = new CandidatesDaoImpl("test.input", new DefaultCandidateIO());

        Candidate candidate = dao.getItemById("15a9");

        System.out.println(candidate);
        List<Candidate> result = dao.getItems(Filters.byFirstName("alex"));
        System.out.println(result);
        List<Candidate> allCandidates = dao.getItems(Filters.byGpaGrade(9.5, 0.06));
        System.out.println(allCandidates);

        Candidate c = new Candidate();
        c.setFirstName("ana");
        c.setLastName("ionescu");
        c.setSocialId("347389523255435");
        c.setGpaGrade(9.00);
        c.setATestGrade(8.4);

        String s = dao.addItem(c);
        System.out.println(s);

        listAll(dao);

        dao.deleteItem(s);

        listAll(dao);

        s = dao.addItem(c);

        System.out.println(s);

        listAll(dao);

        candidate.setFirstName("mihai");

        dao.updateItem(candidate);

        listAll(dao);

        admissionResultsTest(c);
    }

    private static void listAll(CandidatesDaoImpl dao) throws IOException {
        List<Candidate> candidateList;
        candidateList = dao.getItems(Filters.all());
        System.out.println(candidateList);
    }

    private static void admissionResultsTest(Candidate c) throws IOException {
        AdmissionResultsDAOImpl dao = new AdmissionResultsDAOImpl("test-admission.input", new DefaultAdmissionResultsIO());
        AdmissionResult candidate = dao.getItemById("0");
        System.out.println(candidate);
        List<AdmissionResult> result = dao.getItems(new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult admissionResult) {
                return true;
            }
        });
        System.out.println(result);

        AdmissionResult r = new AdmissionResult();
        r.setAdmissionStatus(AdmissionResult.Status.TAX_FREE);
        r.setFinalGrade(9.00);
        r.setCandidateId(c.getCandidateId());

        String s = dao.addItem(r);
        System.out.println(s);
        List<AdmissionResult> candidateList = dao.getItems(new Predicate<AdmissionResult>() {
            @Override
            public boolean apply(AdmissionResult admissionResult) {
                return true;
            }
        });
        System.out.println(candidateList);
    }
}
