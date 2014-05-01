package ro.infoiasi.fiiadmis;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import ro.infoiasi.fiiadmis.db.Table;
import ro.infoiasi.fiiadmis.db.TextDatabase;
import ro.infoiasi.fiiadmis.db.TextDatabaseImpl;
import ro.infoiasi.fiiadmis.db.dao.EntityDAO;
import ro.infoiasi.fiiadmis.db.dao.EntityDAOImpl;
import ro.infoiasi.fiiadmis.db.parser.DefaultAdmissionResultsFormatter;
import ro.infoiasi.fiiadmis.db.parser.DefaultCandidateFormatter;
import ro.infoiasi.fiiadmis.model.AdmissionResult;
import ro.infoiasi.fiiadmis.model.AdmissionResultFilters;
import ro.infoiasi.fiiadmis.model.Candidate;
import ro.infoiasi.fiiadmis.model.CandidateFilters;

public class Main {

    public static void main(String[] args) throws IOException {
        TextDatabase db = initDb();
        Table<Candidate> candidateTable = db.openTableOrCreateIfNotExists("test-candidates", new DefaultCandidateFormatter());
        candidateTest(candidateTable);

        Table<AdmissionResult> admissionResultTable = db.openTableOrCreateIfNotExists("test-admission", new DefaultAdmissionResultsFormatter());
        admissionResultsTest(admissionResultTable);
    }

    private static TextDatabase initDb() throws IOException {
        return new TextDatabaseImpl("fiiadmisdb");
    }

    private static void candidateTest(Table<Candidate> candidateTable) throws IOException {
        EntityDAO<Candidate> dao = new EntityDAOImpl<>(candidateTable);
        Candidate candidate = dao.getItemById("BDuu");
        System.out.println(candidate);
        
        List<Candidate> result = dao.getItems(CandidateFilters.byFirstName("Andrei"), null);
        System.out.println("Andrei results: " + result.toString());
        
        List<Candidate> allCandidates = dao.getItems(CandidateFilters.byGpaGrade(7.2, 2), new Comparator<Candidate>() {
			
			@Override
			public int compare(Candidate o1, Candidate o2) {
				return o1.getFirstName().compareTo(o2.getFirstName());
			}
		});
        System.out.println("GPA filter: " + allCandidates.toString());

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
        candidateList = dao.getItems(CandidateFilters.all(), null);
        System.out.println(candidateList);
    }

    private static void admissionResultsTest(Table<AdmissionResult> admissionResultTable) throws IOException {

        EntityDAO<AdmissionResult> dao = new EntityDAOImpl<>(admissionResultTable);

        AdmissionResult candidate = dao.getItemById("aaaa");
        System.out.println(candidate);
        List<AdmissionResult> result = dao.getItems(AdmissionResultFilters.byCandidateId("3xiy"), null);
        System.out.println(result);

        AdmissionResult r = new AdmissionResult();
        r.setAdmissionStatus(AdmissionResult.Status.TAX_FREE);
        r.setFinalGrade(9.00);
        r.setCandidateId("15a9");

        String s = dao.addItem(r);
        System.out.println(s);
        List<AdmissionResult> candidateList = dao.getItems(AdmissionResultFilters.all(), null);
        System.out.println(candidateList);
    }
}
