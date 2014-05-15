package ro.infoiasi.fiiadmis.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdmissionResultFiltersTest {

    private List<AdmissionResult> admissionResults;

    @Before
    public void setUp() throws Exception {
        admissionResults = Lists.newArrayList();
        AdmissionResult c1 = new AdmissionResult("id1", "candid1", 9.4, AdmissionResult.Status.TAX_FREE);
        AdmissionResult c2 = new AdmissionResult("id2", "candid2", 7.5, AdmissionResult.Status.TAX);
        admissionResults.add(c1);
        admissionResults.add(c2);

    }

    @Test
    public void testAllFilter() {
        List<AdmissionResult> filtered = Lists.newArrayList(Iterables.filter(admissionResults, AdmissionResultFilters.all()));

        assertEquals(admissionResults, filtered);

    }

    @Test
    public void testNoneFilter() {
        List<AdmissionResult> filtered = Lists.newArrayList(Iterables.filter(admissionResults, AdmissionResultFilters.none()));

        assertEquals(0, filtered.size());

    }

    @Test
    public void testByCandidateIdFilter() {
        List<AdmissionResult> filtered = Lists.newArrayList(Iterables.filter(admissionResults, AdmissionResultFilters.byCandidateId("candid1")));

        assertEquals(1, filtered.size());

        assertEquals(admissionResults.get(0), filtered.get(0));

    }

    @Test
    public void testByFinalGrade() {
        List<AdmissionResult> filtered = Lists.newArrayList(Iterables.filter(admissionResults, AdmissionResultFilters.byFinalGrade(8, 0.5)));

        assertEquals(1, filtered.size());

        assertEquals(admissionResults.get(1), filtered.get(0));

    }

    @Test
    public void testByStatus() {
        List<AdmissionResult> filtered = Lists.newArrayList(Iterables.filter(admissionResults, AdmissionResultFilters.byStatus(AdmissionResult.Status.TAX)));

        assertEquals(1, filtered.size());

        assertEquals(admissionResults.get(1), filtered.get(0));

    }
}
