package ro.infoiasi.fiiadmis.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CandidateFiltersTest {

    private List<Candidate> testCandidates;

    @Before
    public void setUp() throws Exception {
        testCandidates = Lists.newArrayList();
        Candidate c1 = new Candidate("id1", "fn1", "ln1", "sid1", 9, 8.5);
        Candidate c2 = new Candidate("id2", "fn2", "ln2", "sid2", 6, 7.5);
        testCandidates.add(c1);
        testCandidates.add(c2);

    }

    @Test
    public void testAllFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.all()));

        assertEquals(testCandidates, filtered);
    }

    @Test
    public void testNoneFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.none()));

        assertEquals(0, filtered.size());
    }

    @Test
    public void testByIdFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byId("id1")));

        assertEquals(1, filtered.size());

        assertEquals(testCandidates.get(0), filtered.get(0));
    }

    @Test
    public void testByInexistentIdFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byId("no")));

        assertEquals(0, filtered.size());
    }

    @Test
    public void testBySocialIdFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.bySocialId("sid2")));

        assertEquals(1, filtered.size());

        assertEquals(testCandidates.get(1), filtered.get(0));
    }

    @Test
    public void testByFirstNameFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byFirstName("fn")));

        assertEquals(2, filtered.size());

        assertEquals(testCandidates, filtered);
    }

    @Test
    public void testByLastNameFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byLastName("ln1")));

        assertEquals(1, filtered.size());

        assertEquals(testCandidates.get(0), filtered.get(0));
    }

    @Test
    public void testByGpaGradeFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byGpaGrade(9, 0.01)));

        assertEquals(1, filtered.size());

        assertEquals(testCandidates.get(0), filtered.get(0));
    }

    @Test
    public void testByATestFilter() {

        List<Candidate> filtered = Lists.newArrayList(Iterables.filter(testCandidates, CandidateFilters.byATestGrade(8, 0.5)));

        assertEquals(2, filtered.size());

        assertEquals(testCandidates, filtered);
    }
}
