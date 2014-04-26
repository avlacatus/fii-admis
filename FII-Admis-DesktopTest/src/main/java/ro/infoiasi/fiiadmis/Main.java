package ro.infoiasi.fiiadmis;

import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.db.dao.EntityDAOImpl;
import ro.infoiasi.fiiadmis.db.parser.DefaultAdmissionResultsFormatter;
import ro.infoiasi.fiiadmis.db.parser.DefaultCandidateFormatter;
import ro.infoiasi.fiiadmis.db.TextDatabase;
import ro.infoiasi.fiiadmis.db.TextDatabaseImpl;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResultFilters;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

//        candidateTest();

        admissionResultsTest();
    }

    private static void candidateTest() throws IOException {
        TextDatabase<Candidate> db = new TextDatabaseImpl<>("test.input", new DefaultCandidateFormatter());

        EntityDAO<Candidate> dao = new EntityDAOImpl<>(db);

        Candidate candidate = dao.getItemById("ad4e");

        System.out.println(candidate);
        List<Candidate> result = dao.getItems(CandidateFilters.byFirstName("alex"));
        System.out.println(result);
        List<Candidate> allCandidates = dao.getItems(CandidateFilters.byGpaGrade(9.5, 0.06));
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
        if (candidate != null) {

            candidate.setFirstName("mihai");

            dao.updateItem(candidate);

            listAll(dao);
        }

    }

    private static void listAll(EntityDAO<Candidate> dao) throws IOException {
        List<Candidate> candidateList;
        candidateList = dao.getItems(CandidateFilters.all());
        System.out.println(candidateList);
    }

    private static void admissionResultsTest() throws IOException {
        TextDatabase<AdmissionResult> db = new TextDatabaseImpl<>("test-admission.input", new DefaultAdmissionResultsFormatter());

        EntityDAO<AdmissionResult> dao = new EntityDAOImpl<>(db);

        AdmissionResult candidate = dao.getItemById("aaaa");
        System.out.println(candidate);
        List<AdmissionResult> result = dao.getItems(AdmissionResultFilters.byCandidateId("3xiy"));
        System.out.println(result);

        AdmissionResult r = new AdmissionResult();
        r.setAdmissionStatus(AdmissionResult.Status.TAX_FREE);
        r.setFinalGrade(9.00);
        r.setCandidateId("15a9");

        String s = dao.addItem(r);
        System.out.println(s);
        List<AdmissionResult> candidateList = dao.getItems(AdmissionResultFilters.all());
        System.out.println(candidateList);
    }
}
