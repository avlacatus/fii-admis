package ro.infoiasi.fiiadmis.dao;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.DoubleMath;
import ro.infoiasi.fiiadmis.model.Candidate;

/**
 * @author <a href="mailto:anad@amazon.com">Ana-Maria Daneliuc</a>
 */
public class Filters {

    private Filters() {}

    public static Predicate<Candidate> all() {

        return Predicates.alwaysTrue();
    }

    public static Predicate<Candidate> none() {

        return Predicates.alwaysFalse();
    }

    public static Predicate<Candidate> byFirstName(final String name) {

        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return candidate.getFirstName().contains(name);
            }
        };
    }


    public static Predicate<Candidate> byLastName(final String name) {

        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return candidate.getLastName().contains(name);
            }
        };
    }

    public static Predicate<Candidate> byGpaGrade(final double grade, final double approximation) {

        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return DoubleMath.fuzzyEquals(candidate.getGpaGrade(), grade, approximation);
            }
        };
    }

    public static Predicate<Candidate> byATestGrade(final double grade, final double approximation) {

        return new Predicate<Candidate>() {
            @Override
            public boolean apply(Candidate candidate) {
                return DoubleMath.fuzzyEquals(candidate.getATestGrade(), grade, approximation);
            }
        };
    }


}
