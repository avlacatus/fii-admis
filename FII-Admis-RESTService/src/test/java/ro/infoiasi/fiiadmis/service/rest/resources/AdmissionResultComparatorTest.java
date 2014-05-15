package ro.infoiasi.fiiadmis.service.rest.resources;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ro.infoiasi.fiiadmis.model.AdmissionResult;

public class AdmissionResultComparatorTest {

    @Test
    public void testSameObject() {
        AdmissionResult r = new AdmissionResult(null, null, 7.18, null);
        assertEquals(0, AdmissionResultComparator.byFinalGrade().compare(r, r));
    }

    @Test
    public void testTwoEqualObjects() {
        AdmissionResult r1 = new AdmissionResult(null, null, 7.18, null);
        AdmissionResult r2 = new AdmissionResult(null, "something", 7.18, null);
        assertEquals(0, AdmissionResultComparator.byFinalGrade().compare(r1, r2));
    }

    @Test
    public void testDifferent() {
        AdmissionResult r1 = new AdmissionResult(null, null, 7.18, null);
        AdmissionResult r2 = new AdmissionResult(null, null, 6.18, null);
        assertEquals(-1, AdmissionResultComparator.byFinalGrade().compare(r1, r2));
        assertEquals(1, AdmissionResultComparator.byFinalGrade().compare(r2, r1));
    }
}
