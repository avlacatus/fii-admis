package ro.infoiasi.fiiadmis.db.parser;

import org.junit.Before;
import org.junit.Test;
import ro.infoiasi.fiiadmis.model.Candidate;

import static org.junit.Assert.*;

public class DefaultCandidateFormatterTest {

    private DefaultCandidateFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new DefaultCandidateFormatter();
    }

    @Test
    public void testGetFieldSeparator() {

        assertEquals(":", formatter.getFieldSeparator());
    }

    @Test
    public void testReadNormal() {
        Candidate candidate = formatter.read("id:ana:maria:442343424:2.75:8");

        assertEquals("id", candidate.getId());
        assertEquals("ana", candidate.getFirstName());
        assertEquals("maria", candidate.getLastName());
        assertEquals("442343424", candidate.getSocialId());
        assertEquals(2.75, candidate.getGpaGrade(), 0.0);
        assertEquals(8.00, candidate.getATestGrade(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWrongGPAGradeFormat() {
        formatter.read("id:ana:maria:socialid:stupid:8");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWrongATestGradeFormat() {
        formatter.read("id:ana:maria:socialid:3:2.a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadInsufficientNumberOfFields() {
        formatter.read("id:ana:maria:socialid");
    }

    @Test
    public void testWrite() {
        Candidate candidate = new Candidate();
        candidate.setId("id");
        candidate.setFirstName("ioan");
        candidate.setLastName("marcu");
        candidate.setSocialId("3432545435");
        candidate.setATestGrade(9.77);
        candidate.setGpaGrade(8.345);

        String stringCandidate = formatter.write(candidate);

        assertEquals("id:ioan:marcu:3432545435:8.345:9.77", stringCandidate);
    }

}
