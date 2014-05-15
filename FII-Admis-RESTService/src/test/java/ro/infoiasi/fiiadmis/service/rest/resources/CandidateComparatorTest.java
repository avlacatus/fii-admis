package ro.infoiasi.fiiadmis.service.rest.resources;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ro.infoiasi.fiiadmis.model.Candidate;

public class CandidateComparatorTest {

    @Test
    public void testEquals() {
        Candidate c = new Candidate(null, null, "Smith", null, 0, 0);
        assertEquals(0, CandidateComparator.byLastName().compare(c, c));
    }

    @Test
    public void testTwoEqualObjects() {
        Candidate c1 = new Candidate(null, null, "Smith", null, 0, 0);
        Candidate c2 = new Candidate(null, "something", "Smith", null, 0, 0);
        assertEquals(0, CandidateComparator.byLastName().compare(c1, c2));
    }

    @Test
    public void testDifferent() {
        Candidate c1 = new Candidate(null, null, "Smith", null, 0, 0);
        Candidate c2 = new Candidate(null, null, "Anthon", null, 0, 0);
        assertEquals(18, CandidateComparator.byLastName().compare(c1, c2));
        assertEquals(-18, CandidateComparator.byLastName().compare(c2, c1));
    }
}
